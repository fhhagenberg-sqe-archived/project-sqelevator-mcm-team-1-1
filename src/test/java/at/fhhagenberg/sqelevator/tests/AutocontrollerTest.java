package at.fhhagenberg.sqelevator.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.rmi.RemoteException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

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

		MockElevatorState elevatorState = elevatorService.getElevators().get(ELEVATOR_0);
		elevatorState.setFloorButtonActive(FLOOR_1, true);
		elevatorState.setDoorStatus(IElevator.ELEVATOR_DOORS_OPEN);

		Elevator elevator = elevatorController.getCurrentState().getElevator(ELEVATOR_0);
		elevator.updateFromService();

		assertEquals(FLOOR_1, elevatorState.getCurrentFloor());

	}

	// To cover line 122, but not sure what to assert
	@Test
	public void testUpdateElevatorAutomaticDoorStatusClosed() throws RemoteException {

		MockElevatorState elevatorState = elevatorService.getElevators().get(ELEVATOR_0);
		elevatorState.setFloorButtonActive(FLOOR_1, true);
		// elevatorState.setDoorStatus(IElevator.ELEVATOR_DOORS_OPEN);

		elevatorService.getFloors().get(FLOOR_1).setUpButtonActive(true);

		Elevator elevator = elevatorController.getCurrentState().getElevator(ELEVATOR_0);
		elevator.updateFromService();
		// assert?
	}

	@Test
	public void testUpdateElevatorManual() throws RemoteException {
		MockElevatorState elevatorState = elevatorService.getElevators().get(ELEVATOR_0);
		elevatorState.setFloorButtonActive(FLOOR_1, true);
		elevatorState.setDoorStatus(IElevator.ELEVATOR_DOORS_OPEN);

		elevatorService.getFloors().get(FLOOR_1).setUpButtonActive(true);

		Elevator elevator = elevatorController.getCurrentState().getElevator(ELEVATOR_0);
		elevator.setControlMode(ControlMode.MANUAL);
		elevator.updateFromService(); // should not update because of manual mode

		assertEquals(FLOOR_0, elevatorState.getCurrentFloor());
	}

	@Test
	public void testUpdateFloorAutomatic() throws RemoteException {
		MockElevatorState elevatorState = elevatorService.getElevators().get(ELEVATOR_0);
		elevatorState.setDoorStatus(IElevator.ELEVATOR_DOORS_OPEN);

		// Up
		elevatorService.getFloors().get(FLOOR_1).setUpButtonActive(true);

		var building = elevatorController.getCurrentState();
		building.getElevator(ELEVATOR_0).updateFromService();

		var floor1 = building.getFloor(FLOOR_1);
		floor1.updateFromService();

		assertEquals(FLOOR_1, elevatorService.getElevators().get(ELEVATOR_0).getCurrentFloor());

		// Down
		elevatorService.getFloors().get(FLOOR_0).setDownButtonActive(true);

		var floor0 = building.getFloor(FLOOR_0);
		building.getElevator(ELEVATOR_0).updateFromService();
		floor0.updateFromService();

		assertEquals(FLOOR_0, elevatorService.getElevators().get(ELEVATOR_0).getCurrentFloor());
	}

	@Test
	public void testUpdateFloorManual() throws RemoteException {
		MockElevatorState elevatorState = elevatorService.getElevators().get(ELEVATOR_0);
		elevatorState.setDoorStatus(IElevator.ELEVATOR_DOORS_OPEN);

		elevatorService.getFloors().get(FLOOR_1).setUpButtonActive(true);

		var building = elevatorController.getCurrentState();
		building.getElevator(ELEVATOR_0).setControlMode(ControlMode.MANUAL);
		building.getElevator(ELEVATOR_0).updateFromService();

		var floor1 = building.getFloor(FLOOR_1);
		floor1.updateFromService(); // should not update because of manual mode

		assertEquals(FLOOR_0, elevatorService.getElevators().get(ELEVATOR_0).getCurrentFloor());
	}

}
