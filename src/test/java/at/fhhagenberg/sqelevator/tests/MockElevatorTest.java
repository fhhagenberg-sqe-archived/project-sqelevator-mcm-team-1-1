package at.fhhagenberg.sqelevator.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MockElevatorTest {
    private static final Integer ELEVATOR_CAPACITY = 10;
    private static final Integer NUM_ELEVATORS = 3;
    private static final Integer NUM_FLOORS = 4;
    private static final Integer FLOOR_HEIGHT = 5;

    private static final int ELEVATOR_0 = 0;
    private static final int ELEVATOR_1 = 1;
    private static final int ELEVATOR_2 = 2;

    public static final int FLOOR_0 = 0;
    public static final int FLOOR_1 = 1;
    public static final int FLOOR_2 = 2;
    public static final int FLOOR_3 = 3;

    private MockElevator mockElevator;

    @BeforeEach
    public void setup() {
        mockElevator = new MockElevator(NUM_ELEVATORS, NUM_FLOORS, FLOOR_HEIGHT, ELEVATOR_CAPACITY);
    }

    @Test
    void testGetCommittedDirection() throws Exception {
        assertEquals(MockElevator.ELEVATOR_DIRECTION_UNCOMMITTED, mockElevator.getCommittedDirection(ELEVATOR_0));
    }

    @Test
    void testGetCommittedDirectionInvalidElevator() {
        assertThrows(MockElevatorException.class, ()->{
            mockElevator.getCommittedDirection(ELEVATOR_0-1);
        });

        assertThrows(MockElevatorException.class, ()->{
            mockElevator.getCommittedDirection(NUM_ELEVATORS);
        });
    }

    @Test
    void testGetServicesFloors() throws Exception {
        assertEquals(true, mockElevator.getServicesFloors(ELEVATOR_0, FLOOR_0));
        assertEquals(true, mockElevator.getServicesFloors(ELEVATOR_1, FLOOR_0));
        assertEquals(true, mockElevator.getServicesFloors(ELEVATOR_2, FLOOR_0));

        assertEquals(true, mockElevator.getServicesFloors(ELEVATOR_0, FLOOR_1));
        assertEquals(true, mockElevator.getServicesFloors(ELEVATOR_1, FLOOR_1));
        assertEquals(false, mockElevator.getServicesFloors(ELEVATOR_2, FLOOR_1));

        assertEquals(true, mockElevator.getServicesFloors(ELEVATOR_0, FLOOR_2));
        assertEquals(false, mockElevator.getServicesFloors(ELEVATOR_1, FLOOR_2));
        assertEquals(true, mockElevator.getServicesFloors(ELEVATOR_2, FLOOR_2));

        assertEquals(true, mockElevator.getServicesFloors(ELEVATOR_0, FLOOR_3));
        assertEquals(true, mockElevator.getServicesFloors(ELEVATOR_1, FLOOR_3));
        assertEquals(false, mockElevator.getServicesFloors(ELEVATOR_2, FLOOR_3));
    }

    @Test
    void testGetServicesFloorsInvalidFloor() {
        assertThrows(MockElevatorException.class, ()->{
            mockElevator.getServicesFloors(ELEVATOR_0,FLOOR_0-1);
        });

        assertThrows(MockElevatorException.class, ()->{
            mockElevator.getServicesFloors(ELEVATOR_0, NUM_FLOORS);
        });
    }
}
