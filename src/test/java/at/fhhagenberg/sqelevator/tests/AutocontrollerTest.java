package at.fhhagenberg.sqelevator.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.rmi.RemoteException;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import at.fhhagenberg.sqelevator.mock.MockElevator;
import at.fhhagenberg.sqelevator.mock.MockElevatorState;
import at.fhhagenberg.sqelevator.model.ControlMode;
import at.fhhagenberg.sqelevator.model.Elevator;
import at.fhhagenberg.sqelevator.model.ElevatorController;
import at.fhhagenberg.sqelevator.model.autocontroller.SimpleControlAlgorithm;
import sqelevator.IElevator;

public class AutocontrollerTest {

	private static final Integer ELEVATOR_CAPACITY = 10;
	private static final Integer NUM_ELEVATORS = 1;
	private static final Integer NUM_FLOORS = 3;
	private static final Integer FLOOR_HEIGHT = 5;

	private static final int ELEVATOR_0 = 0;
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

	/**
	 * Checks if the algorithm controls the elevator. After setting the target floor, the elevator should reach the destination and change it's current floor. 
	 * @param elevator
	 * @param targetFloor
	 * @throws RemoteException
	 */
	@ParameterizedTest
	@MethodSource("paramsElevatorTests")
	public void testUpdateElevatorAutomatic(int elevator, int targetFloor) throws RemoteException {
		updateElevator(elevator, targetFloor, IElevator.ELEVATOR_DOORS_OPEN, ControlMode.AUTOMATIC);
		assertEquals(targetFloor, elevatorService.getElevators().get(elevator).getCurrentFloor());
	}

	// To cover line 122, but not sure what to assert
	@Test
	public void testUpdateElevatorAutomaticDoorStatusClosed() throws RemoteException {
		updateElevator(ELEVATOR_0, FLOOR_1, IElevator.ELEVATOR_DOORS_CLOSED, ControlMode.AUTOMATIC);
		// assert?
	}

	/**
	 * Checks if the control algorithm handles the manual mode correctly, it should not take over the elevator. 
	 * @param elevator
	 * @param targetFloor
	 * @throws RemoteException
	 */
	@ParameterizedTest
	@MethodSource("paramsElevatorTests")
	public void testUpdateElevatorManual(int elevator, int targetFloor) throws RemoteException {
		// should not update because of manual mode
		updateElevator(elevator, targetFloor, IElevator.ELEVATOR_DOORS_OPEN, ControlMode.MANUAL);
		assertEquals(FLOOR_0, elevatorService.getElevators().get(elevator).getCurrentFloor());
	}


	@ParameterizedTest
	@MethodSource("paramsFloorTests")
	public void testUpdateFloorAutomatic(int elevator, int targetFloorUp, int targetFloorDown) throws RemoteException {
		updateFloor(elevator, targetFloorUp, IElevator.ELEVATOR_DOORS_OPEN, ControlMode.AUTOMATIC); // up
		assertEquals(targetFloorUp, elevatorService.getElevators().get(elevator).getCurrentFloor());

		updateFloor(elevator, targetFloorDown, IElevator.ELEVATOR_DOORS_OPEN, ControlMode.AUTOMATIC); // down
		assertEquals(targetFloorDown, elevatorService.getElevators().get(elevator).getCurrentFloor());
	}

	@ParameterizedTest
	@MethodSource("paramsFloorTests")
	public void testUpdateFloorManual(int elevator, int targetFloor) throws RemoteException {
		// should not update because of manual mode
		updateFloor(elevator, targetFloor, IElevator.ELEVATOR_DOORS_OPEN, ControlMode.MANUAL);
		assertEquals(FLOOR_0, elevatorService.getElevators().get(elevator).getCurrentFloor());
	}

	private static Stream<Arguments> paramsElevatorTests() {
		return Stream.of( //
				Arguments.of(ELEVATOR_0, FLOOR_0), //
				Arguments.of(ELEVATOR_0, FLOOR_1), //
				Arguments.of(ELEVATOR_0, FLOOR_2) //
		);
	}

	private static Stream<Arguments> paramsFloorTests() {
		return Stream.of( //
				Arguments.of(ELEVATOR_0, FLOOR_0, FLOOR_0), //
				Arguments.of(ELEVATOR_0, FLOOR_1, FLOOR_0), //
				Arguments.of(ELEVATOR_0, FLOOR_2, FLOOR_0), //
				Arguments.of(ELEVATOR_0, FLOOR_2, FLOOR_1), //
				Arguments.of(ELEVATOR_0, FLOOR_2, FLOOR_2));
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
