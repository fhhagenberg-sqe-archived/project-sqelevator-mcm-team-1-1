package at.fhhagenberg.sqelevator.model;

import at.fhhagenberg.sqelevator.model.observers.IBuildingInitializedObserver;
import sqelevator.IElevator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ElevatorController implements IElevatorController {
	private Timer timer;

	private IElevator elevatorService;

	private Building building;

	private long updateInterval = 1000;

	private List<IBuildingInitializedObserver> buildingInitializedObservers;

	public ElevatorController(IElevator elevatorService) {
		this.elevatorService = elevatorService;

		buildingInitializedObservers = new ArrayList<>();
	}

	public boolean isInitialized() {
		return building != null;
	}

	public void startPeriodicUpdates() {
		if (timer != null) {
			return;
		}

		timer = new Timer();

		var updateTask = new TimerTask() {
			@Override
			public void run() {
				update();
			}
		};

		timer.scheduleAtFixedRate(updateTask, 0, updateInterval);
	}

	public void stopUpdates() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	private void update() {
		if (!isInitialized()) {
			return;
		}

		try {
			updateInternal();
		} catch (RemoteException e) {
			AlarmsService.getInstance().addWarning(e.getMessage());
		}
	}

	@Override
	public void addInitializedObserver(IBuildingInitializedObserver buildingInitializedObserver) {
		buildingInitializedObservers.add(buildingInitializedObserver);
	}

	@Override
	public Building getCurrentState() {
		return building;
	}

	public void setUpdateInterval(long updateInterval) {
		this.updateInterval = updateInterval;
	}

	public void initialize() {
		if (elevatorService == null) {
			AlarmsService.getInstance().addError("Elevator Service not initialized");

			return;
		}

		try {
			var numElevators = elevatorService.getElevatorNum();
			var numFloors = elevatorService.getFloorNum();

			building = new Building(numElevators, numFloors, elevatorService);
		} catch (RemoteException e) {
			AlarmsService.getInstance().addError(e.getMessage());
		}

		notifyBuildingInitialized();
	}

	private void updateInternal() throws RemoteException {
		for (Elevator elevator : building.getElevators()) {
			elevator.updateFromService();
		}

		for (Floor floor : building.getFloors()) {
			floor.updateFromService();
		}
	}

	private void notifyBuildingInitialized() {
		for (IBuildingInitializedObserver observer : buildingInitializedObservers) {
			observer.initializationDone();
		}
	}
}
