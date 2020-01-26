package at.fhhagenberg.sqelevator.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.rmi.RemoteException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import at.fhhagenberg.sqelevator.mock.MockElevator;
import at.fhhagenberg.sqelevator.mock.MockElevatorState;
import at.fhhagenberg.sqelevator.model.ControlMode;
import at.fhhagenberg.sqelevator.model.Elevator;
import at.fhhagenberg.sqelevator.model.ElevatorController;
import at.fhhagenberg.sqelevator.model.autocontroller.SimpleControlAlgorithm;
import sqelevator.IElevator;

public class AutocontrollerTest {

	private static final Integer ELEVATOR_CAPACITY = 10;
	private static final Integer NUM_ELEVATORS = 2;
	private static final Integer NUM_FLOORS = 3;
	private static final Integer FLOOR_HEIGHT = 5;

	private static final int ELEVATOR_0 = 0;
	private static final int ELEVATOR_1 = 1;

	public static final int FLOOR_0 = 0;
	public static final int FLOOR_1 = 1;
	public static final int FLOOR_2 = 2;

	private MockElevator elevatorService;
	private ElevatorController elevatorController;
	private SimpleControlAlgorithm controlAlgorithm;

	@BeforeEach
	public void setup() {
		elevatorService = new MockElevator(NUM_ELEVATORS, NUM_FLOORS, FLOOR_HEIGHT, ELEVATOR_CAPACITY);
		elevatorController = new ElevatorController(elevatorService);

		controlAlgorithm = new SimpleControlAlgorithm();
		controlAlgorithm.setElevatorController(elevatorController);

		elevatorController.addInitializedObserver(controlAlgorithm);
		elevatorController.initialize();
		controlAlgorithm.start();
	}

	@AfterEach
	public void tearDown() {
		controlAlgorithm.stop();
	}

	@Test
	public void testUpdateElevatorAutomatic() throws RemoteException {
		updateElevator(ELEVATOR_0, FLOOR_1, IElevator.ELEVATOR_DOORS_OPEN, ControlMode.AUTOMATIC);
		assertEquals(FLOOR_1, elevatorService.getElevators().get(ELEVATOR_0).getCurrentFloor());
	}

	// To cover line 122, but not sure what to assert
	@Test
	public void testUpdateElevatorAutomaticDoorStatusClosed() throws RemoteException {
		updateElevator(ELEVATOR_0, FLOOR_1, IElevator.ELEVATOR_DOORS_CLOSED, ControlMode.AUTOMATIC);
		// assert?
	}

	@Test
	public void testUpdateElevatorManual() throws RemoteException {
		// should not update because of manual mode
		updateElevator(ELEVATOR_0, FLOOR_1, IElevator.ELEVATOR_DOORS_OPEN, ControlMode.MANUAL);
		assertEquals(FLOOR_0, elevatorService.getElevators().get(ELEVATOR_0).getCurrentFloor());
	}

	@Test
	public void testUpdateFloorAutomatic() throws RemoteException {
		updateFloor(ELEVATOR_0, FLOOR_1, IElevator.ELEVATOR_DOORS_OPEN, ControlMode.AUTOMATIC); // up
		assertEquals(FLOOR_1, elevatorService.getElevators().get(ELEVATOR_0).getCurrentFloor());

		updateFloor(ELEVATOR_0, FLOOR_0, IElevator.ELEVATOR_DOORS_OPEN, ControlMode.AUTOMATIC); // down
		assertEquals(FLOOR_0, elevatorService.getElevators().get(ELEVATOR_0).getCurrentFloor());
	}

	@Test
	public void testUpdateFloorManual() throws RemoteException {
		// should not update because of manual mode
		updateFloor(ELEVATOR_0, FLOOR_0, IElevator.ELEVATOR_DOORS_OPEN, ControlMode.MANUAL);
		assertEquals(FLOOR_0, elevatorService.getElevators().get(ELEVATOR_0).getCurrentFloor());
	}

	private void updateElevator(int elevator, int targetFloor, int doorStatus, ControlMode mode)
			throws RemoteException {
		MockElevatorState elevatorState = elevatorService.getElevators().get(elevator);
		elevatorState.setFloorButtonActive(targetFloor, true);
		elevatorState.setDoorStatus(doorStatus);

		Elevator e = elevatorController.getCurrentState().getElevator(elevator);
		e.setControlMode(mode);
		e.updateFromService();
	}

	private void updateFloor(int elevator, int targetFloor, int doorStatus, ControlMode mode) throws RemoteException {
		MockElevatorState elevatorState = elevatorService.getElevators().get(elevator);
		elevatorState.setDoorStatus(doorStatus);

		if (elevatorState.getCurrentFloor() < targetFloor) {
			elevatorService.getFloors().get(targetFloor).setUpButtonActive(true);
		} else {
			elevatorService.getFloors().get(targetFloor).setDownButtonActive(true);
		}

		var building = elevatorController.getCurrentState();
		Elevator e = building.getElevator(elevator);
		e.setControlMode(mode);
		e.updateFromService();

		var floor = building.getFloor(targetFloor);
		floor.updateFromService();
	}
}
