package at.fhhagenberg.sqelevator.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import at.fhhagenberg.sqelevator.mock.MockElevator;
import at.fhhagenberg.sqelevator.mock.MockElevatorException;
import sqelevator.IElevator;

public class MockElevatorTest {
	private static final Integer ELEVATOR_CAPACITY = 10;
	private static final Integer NUM_ELEVATORS = 2;
	private static final Integer NUM_FLOORS = 3;
	private static final Integer FLOOR_HEIGHT = 5;

	private static final int ELEVATOR_0 = 0;
	private static final int ELEVATOR_1 = 1;

	public static final int FLOOR_0 = 0;
	public static final int FLOOR_1 = 1;
	public static final int FLOOR_2 = 2;

	private MockElevator mockElevator;

	@BeforeEach
	public void setup() {
		mockElevator = new MockElevator(NUM_ELEVATORS, NUM_FLOORS, FLOOR_HEIGHT, ELEVATOR_CAPACITY);
	}

	@Test
	void testCommittedDirectionInit() throws Exception {
		assertEquals(MockElevator.ELEVATOR_DIRECTION_UNCOMMITTED, mockElevator.getCommittedDirection(ELEVATOR_0));
		assertEquals(MockElevator.ELEVATOR_DIRECTION_UNCOMMITTED, mockElevator.getCommittedDirection(ELEVATOR_1));
	}

	@Test
	void testCommittedDirectionSetGet() throws Exception {
		mockElevator.setCommittedDirection(ELEVATOR_0, IElevator.ELEVATOR_DIRECTION_DOWN);
		mockElevator.setCommittedDirection(ELEVATOR_1, IElevator.ELEVATOR_DIRECTION_UP);

		assertEquals(MockElevator.ELEVATOR_DIRECTION_DOWN, mockElevator.getCommittedDirection(ELEVATOR_0));
		assertEquals(MockElevator.ELEVATOR_DIRECTION_UP, mockElevator.getCommittedDirection(ELEVATOR_1));

		mockElevator.setCommittedDirection(ELEVATOR_0, IElevator.ELEVATOR_DIRECTION_UNCOMMITTED);
		mockElevator.setCommittedDirection(ELEVATOR_1, IElevator.ELEVATOR_DIRECTION_UNCOMMITTED);

		assertEquals(MockElevator.ELEVATOR_DIRECTION_UNCOMMITTED, mockElevator.getCommittedDirection(ELEVATOR_0));
		assertEquals(MockElevator.ELEVATOR_DIRECTION_UNCOMMITTED, mockElevator.getCommittedDirection(ELEVATOR_1));
	}

	@Test
	void testCommittedDirectionInvalid() throws Exception {
		assertThrows(MockElevatorException.class, () -> {
			mockElevator.getCommittedDirection(ELEVATOR_0 - 1);
		});

		assertThrows(MockElevatorException.class, () -> {
			mockElevator.getCommittedDirection(NUM_ELEVATORS);
		});

		assertThrows(MockElevatorException.class, () -> {
			mockElevator.setCommittedDirection(ELEVATOR_0, MockElevator.ELEVATOR_DIRECTION_UP - 1);
		});

		assertThrows(MockElevatorException.class, () -> {
			mockElevator.setCommittedDirection(ELEVATOR_0, MockElevator.ELEVATOR_DIRECTION_UNCOMMITTED + 1);
		});
	}

	@Test
	void testTargetInit() throws Exception {
		assertEquals(FLOOR_0, mockElevator.getTarget(ELEVATOR_0));
		assertEquals(FLOOR_0, mockElevator.getTarget(ELEVATOR_1));
	}

	@Test
	void testTargetSetGet() throws Exception {
		mockElevator.setTarget(ELEVATOR_0, FLOOR_1);
		mockElevator.setTarget(ELEVATOR_1, FLOOR_2);

		assertEquals(FLOOR_1, mockElevator.getTarget(ELEVATOR_0));
		assertEquals(FLOOR_2, mockElevator.getTarget(ELEVATOR_1));
	}

	@Test
	void testServicesFloorsInit() throws Exception {
		assertEquals(true, mockElevator.getServicesFloors(ELEVATOR_0, FLOOR_0));
		assertEquals(true, mockElevator.getServicesFloors(ELEVATOR_1, FLOOR_0));

		assertEquals(true, mockElevator.getServicesFloors(ELEVATOR_0, FLOOR_1));
		assertEquals(true, mockElevator.getServicesFloors(ELEVATOR_1, FLOOR_1));

		assertEquals(true, mockElevator.getServicesFloors(ELEVATOR_0, FLOOR_2));
		assertEquals(true, mockElevator.getServicesFloors(ELEVATOR_1, FLOOR_2));
	}

	@Test
	void testServicesFloorsSetGet() throws Exception {
		mockElevator.setServicesFloors(ELEVATOR_0, FLOOR_0, false);
		assertEquals(false, mockElevator.getServicesFloors(ELEVATOR_0, FLOOR_0));
		assertEquals(true, mockElevator.getServicesFloors(ELEVATOR_1, FLOOR_0));

		mockElevator.setServicesFloors(ELEVATOR_1, FLOOR_1, false);
		assertEquals(true, mockElevator.getServicesFloors(ELEVATOR_0, FLOOR_1));
		assertEquals(false, mockElevator.getServicesFloors(ELEVATOR_1, FLOOR_1));

		mockElevator.setServicesFloors(ELEVATOR_0, FLOOR_0, false);
		mockElevator.setServicesFloors(ELEVATOR_0, FLOOR_1, false);
		mockElevator.setServicesFloors(ELEVATOR_0, FLOOR_2, false);
		assertEquals(false, mockElevator.getServicesFloors(ELEVATOR_0, FLOOR_0));
		assertEquals(false, mockElevator.getServicesFloors(ELEVATOR_0, FLOOR_1));
		assertEquals(false, mockElevator.getServicesFloors(ELEVATOR_0, FLOOR_2));
	}

	@Test
	void testServicesFloorsInvalid() throws Exception {
		assertThrows(MockElevatorException.class, () -> {
			mockElevator.getServicesFloors(ELEVATOR_0 - 1, FLOOR_0);
		});

		assertThrows(MockElevatorException.class, () -> {
			mockElevator.getServicesFloors(NUM_ELEVATORS, FLOOR_0);
		});

		assertThrows(MockElevatorException.class, () -> {
			mockElevator.getServicesFloors(ELEVATOR_0, FLOOR_0 - 1);
		});

		assertThrows(MockElevatorException.class, () -> {
			mockElevator.getServicesFloors(ELEVATOR_0, NUM_FLOORS);
		});
	}

	@Test
	void testDoorStatusSetGet() throws Exception {
		mockElevator.getElevators().get(ELEVATOR_0).setDoorStatus(MockElevator.ELEVATOR_DOORS_OPENING);
		mockElevator.getElevators().get(ELEVATOR_1).setDoorStatus(MockElevator.ELEVATOR_DOORS_CLOSING);

		assertEquals(MockElevator.ELEVATOR_DOORS_OPENING, mockElevator.getElevatorDoorStatus(ELEVATOR_0));
		assertEquals(MockElevator.ELEVATOR_DOORS_CLOSING, mockElevator.getElevatorDoorStatus(ELEVATOR_1));

		mockElevator.getElevators().get(ELEVATOR_0).setDoorStatus(MockElevator.ELEVATOR_DOORS_OPEN);
		mockElevator.getElevators().get(ELEVATOR_1).setDoorStatus(MockElevator.ELEVATOR_DOORS_CLOSED);

		assertEquals(MockElevator.ELEVATOR_DOORS_OPEN, mockElevator.getElevatorDoorStatus(ELEVATOR_0));
		assertEquals(MockElevator.ELEVATOR_DOORS_CLOSED, mockElevator.getElevatorDoorStatus(ELEVATOR_1));
	}

	@Test
	void testDoorStatusInvalid() throws Exception {
		assertThrows(MockElevatorException.class, () -> {
			mockElevator.getElevators().get(ELEVATOR_0).setDoorStatus(MockElevator.ELEVATOR_DOORS_OPEN - 1);
		});

		assertThrows(MockElevatorException.class, () -> {
			mockElevator.getElevators().get(ELEVATOR_0).setDoorStatus(MockElevator.ELEVATOR_DOORS_CLOSING + 1);
		});
	}

	@Test
	void testFloorButtonSetGet() throws Exception {
		mockElevator.getElevators().get(ELEVATOR_0).setFloorButtonActive(FLOOR_0, true);
		mockElevator.getElevators().get(ELEVATOR_1).setFloorButtonActive(FLOOR_1, true);

		assertEquals(true, mockElevator.getElevatorButton(ELEVATOR_0, FLOOR_0));
		assertEquals(false, mockElevator.getElevatorButton(ELEVATOR_0, FLOOR_1));
		assertEquals(false, mockElevator.getElevatorButton(ELEVATOR_0, FLOOR_2));

		assertEquals(false, mockElevator.getElevatorButton(ELEVATOR_1, FLOOR_0));
		assertEquals(true, mockElevator.getElevatorButton(ELEVATOR_1, FLOOR_1));
		assertEquals(false, mockElevator.getElevatorButton(ELEVATOR_1, FLOOR_2));
	}

	@Test
	void testFloorButtonInvalid() throws Exception {
		assertThrows(MockElevatorException.class, () -> {
			mockElevator.getElevators().get(ELEVATOR_0).setFloorButtonActive(FLOOR_0 - 1, true);
		});

		assertThrows(MockElevatorException.class, () -> {
			mockElevator.getElevators().get(ELEVATOR_1).setFloorButtonActive(FLOOR_2 + 1, true);
		});
	}

	@Test
	void testElevatorPositionGet() throws Exception {
		assertEquals(0, mockElevator.getElevatorPosition(ELEVATOR_0));
		assertEquals(0, mockElevator.getElevatorPosition(ELEVATOR_1));

		mockElevator.setTarget(ELEVATOR_0, FLOOR_1);
		assertEquals(FLOOR_HEIGHT * FLOOR_1, mockElevator.getElevatorPosition(ELEVATOR_0));

		mockElevator.setTarget(ELEVATOR_1, FLOOR_2);
		assertEquals(FLOOR_HEIGHT * FLOOR_2, mockElevator.getElevatorPosition(ELEVATOR_1));
	}

	@Test
	void testClockTickGet() throws Exception {
		assertEquals(MockElevator.CLOCK_TICK_MOCK_VALUE, mockElevator.getClockTick());
	}

	@Test
	void testFloorHeightGet() throws Exception {
		assertEquals(FLOOR_HEIGHT, mockElevator.getFloorHeight());
	}
}
