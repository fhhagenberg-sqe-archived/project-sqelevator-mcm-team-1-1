package at.fhhagenberg.sqelevator.model.autocontroller;

import java.util.logging.Level;
import java.util.logging.Logger;

import at.fhhagenberg.sqelevator.model.ControlMode;
import at.fhhagenberg.sqelevator.model.Elevator;
import at.fhhagenberg.sqelevator.model.Floor;
import at.fhhagenberg.sqelevator.model.IElevatorController;
import at.fhhagenberg.sqelevator.model.observers.IBuildingInitializedObserver;
import at.fhhagenberg.sqelevator.model.observers.Observable;
import at.fhhagenberg.sqelevator.model.observers.Observer;
import sqelevator.IElevator;

public class SimpleControlAlgorithm implements IControlAlgorithm, IBuildingInitializedObserver {

	private static final Logger LOGGER = Logger.getLogger(SimpleControlAlgorithm.class.getName());

	private class ElevatorObserver implements Observer<Elevator> {

		private SimpleControlAlgorithm sca;

		public ElevatorObserver(SimpleControlAlgorithm sca) {
			this.sca = sca;
		}

		@Override
		public void update(Observable<Elevator> observable) {

			var elevator = observable.getValue();

			sca.updateElevator(elevator);
		}
	}

	private class FloorObserver implements Observer<Floor> {

		private SimpleControlAlgorithm sca;

		public FloorObserver(SimpleControlAlgorithm sca) {
			this.sca = sca;
		}

		@Override
		public void update(Observable<Floor> observable) {

			var floor = observable.getValue();

			sca.updateFloor(floor);
		}
	}

	private ElevatorObserver elevatorObserver;
	private FloorObserver floorObserver;

	private IElevatorController elevatorController;

	@Override
	public void setElevatorController(IElevatorController elevatorController) {
		this.elevatorController = elevatorController;
	}

	@Override
	public void start() {
		this.elevatorController.addInitializedObserver(this);
	}

	@Override
	public void stop() {
		var building = elevatorController.getCurrentState();

		for (Floor floor : building.getFloors()) {
			floor.removeObserver(floorObserver);
		}
		for (Elevator elevator : building.getElevators()) {
			elevator.removeObserver(elevatorObserver);
		}
	}

	@Override
	public void initializationDone() {
		var building = elevatorController.getCurrentState();

		elevatorObserver = new ElevatorObserver(this);
		floorObserver = new FloorObserver(this);

		building.getFloors().forEach(floor -> floor.addObserver(floorObserver));
		building.getElevators().forEach(elevator -> elevator.addObserver(elevatorObserver));
	}

	/**
	 * Sets the target floor if elevator is available and someone called an
	 * elevator.
	 * 
	 * @param elevator
	 */
	public void updateElevator(Elevator elevator) {
		if (elevator.getControlMode() == ControlMode.MANUAL) {
			return; // skip elevators in manual mode
		}

		var building = elevatorController.getCurrentState();
		int targetfloor = -1;
		boolean isFloorButtonActive = false;

		if (elevator.getDoorStatus() == IElevator.ELEVATOR_DOORS_OPEN) {
			for (int i = 0; i < building.getNumFloors(); i++) {
				if (elevator.isFloorButtonActive(i)) {
					targetfloor = i;
					elevator.gotoTargetAndSendDirection(targetfloor);
					isFloorButtonActive = true;
					break;
				}
			}
		}

		if (targetfloor == -1 && isFloorButtonActive) {
			updateFloor(building.getFloor(0)); // Time for a floor button
		}
	}

	/**
	 * Sends an elevator to the specified floor, if there is one available.
	 * 
	 * @param floor
	 */
	public void updateFloor(Floor floor) {
		var building = elevatorController.getCurrentState();
		Elevator targetElevator = null;

		// Iterate through all elevators
		for (Elevator e : building.getElevators()) {
			LOGGER.log(Level.INFO, "Serviced floor: {0}", e.getServicesFloors(floor.getId()));

			// only make use of this elevator when this is a floor that that elevator is
			// servicing and the mode is automatic and no other elevator is sent to this
			// floor
			if (isElevatorAvailable(e, floor) && (canGoDown(floor, e) || canGoUp(floor, e))) {
				targetElevator = e;
				break;
			}
		}

		// If still no elevator handles this floor then use first available one
		if (targetElevator == null) {
			for (Elevator e : building.getElevators()) {
				if (isElevatorAvailable(e, floor) && (floor.isDownButtonActive() || floor.isUpButtonActive())) {
					targetElevator = e;
					break;
				}
			}
		}

		if (targetElevator != null) {
			targetElevator.gotoTargetAndSendDirection(floor.getId());
			LOGGER.log(Level.INFO, "Sending elevator {0} to floor {1}",
					new Object[] { targetElevator.getId(), floor.getId() });
		}
		// else floor ignored - is handled by a next elevator event that is free
	}

	/**
	 * send this elevator when floor downbutton is active and this elevator is above
	 * this floor and going down or the elevator has no direction / not going
	 * anywhere
	 * 
	 * @param floor
	 * @param e
	 * @return
	 */
	private boolean canGoDown(Floor floor, Elevator e) {
		return floor.isDownButtonActive()
				&& ((e.getCurrentFloor() > floor.getId() && e.getDirection() == IElevator.ELEVATOR_DIRECTION_DOWN)
						|| (e.getDirection() == IElevator.ELEVATOR_DIRECTION_UNCOMMITTED));
	}

	/**
	 * send this elevator when floor upbutton is active and this elevator is below
	 * this floor and going up or the elevator has no direction / not going anywhere
	 * 
	 * @param floor
	 * @param e
	 * @return
	 */
	private boolean canGoUp(Floor floor, Elevator e) {
		return floor.isUpButtonActive()
				&& ((e.getCurrentFloor() < floor.getId() && e.getDirection() == IElevator.ELEVATOR_DIRECTION_UP)
						|| (e.getDirection() == IElevator.ELEVATOR_DIRECTION_UNCOMMITTED));
	}

	private boolean isElevatorAvailable(Elevator e, Floor floor) {
		return e.getControlMode() == ControlMode.AUTOMATIC && e.getServicesFloors(floor.getId())
				&& e.getDoorStatus() == IElevator.ELEVATOR_DOORS_OPEN;
	}
}
