package at.fhhagenberg.sqelevator.mock;

import sqelevator.IElevator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


/**
 * @noinspection RedundantThrows, WeakerAccess
 */
public class MockElevator implements IElevator {
    public static final int CLOCK_TICK_MOCK_VALUE = 42;
    public static final int ELEVATOR_ACCELERATION_MOCK_VALUE = 10;
    public static final int ELEVATOR_SPEED_MOCK_VALUE = 20;
    public static final int ELEVATOR_WEIGHT_MOCK_VALUE = 100;

    private int numElevators;
    private int numFloors;
    private int floorHeight;
    private List<MockElevatorState> elevators;
    private List<MockFloorState> floors;

    public MockElevator(int numElevators, int numFloors, int floorHeight, int elevatorCapacity) {
        this.numElevators = numElevators;
        this.numFloors = numFloors;
        this.floorHeight = floorHeight;

        elevators = new ArrayList<>(numElevators);
        floors = new ArrayList<>(numFloors);

        for (int i = 0; i < numElevators; i++) {
            var elevator = new MockElevatorState(numFloors);
            elevator.setCapacity(elevatorCapacity);
            elevator.setAcceleration(ELEVATOR_ACCELERATION_MOCK_VALUE);
            elevator.setSpeed(ELEVATOR_SPEED_MOCK_VALUE);
            elevator.setWeight(ELEVATOR_WEIGHT_MOCK_VALUE);
            elevators.add(elevator);
        }

        for (int i = 0; i < numFloors; i++) {
            floors.add(new MockFloorState());
        }
    }

    public List<MockElevatorState> getElevators() {
        return elevators;   //expose elevators for mocking
    }

    public List<MockFloorState> getFloors() {
        return floors;   //expose floors for mocking
    }

    @Override
    public int getCommittedDirection(int elevatorNumber) throws RemoteException {
        checkElevatorNumber(elevatorNumber);

        return elevators.get(elevatorNumber).getDirection();
    }

    @Override
    public int getElevatorAccel(int elevatorNumber) throws RemoteException {
        checkElevatorNumber(elevatorNumber);

        return elevators.get(elevatorNumber).getAcceleration();
    }

    @Override
    public boolean getElevatorButton(int elevatorNumber, int floor) throws RemoteException {
        checkElevatorNumber(elevatorNumber);
        checkFloorNumber(floor);

        return elevators.get(elevatorNumber).isFloorButtonActive(floor);
    }

    @Override
    public int getElevatorDoorStatus(int elevatorNumber) throws RemoteException {
        checkElevatorNumber(elevatorNumber);

        return elevators.get(elevatorNumber).getDoorStatus();
    }

    @Override
    public int getElevatorFloor(int elevatorNumber) throws RemoteException {
        checkElevatorNumber(elevatorNumber);

        return elevators.get(elevatorNumber).getCurrentFloor();
    }

    @Override
    public int getElevatorNum() throws RemoteException {
        return numElevators;
    }

    @Override
    public int getElevatorPosition(int elevatorNumber) throws RemoteException {
        checkElevatorNumber(elevatorNumber);

        return elevators.get(elevatorNumber).getCurrentFloor() * floorHeight;
    }

    @Override
    public int getElevatorSpeed(int elevatorNumber) throws RemoteException {
        checkElevatorNumber(elevatorNumber);

        return elevators.get(elevatorNumber).getSpeed();
    }

    @Override
    public int getElevatorWeight(int elevatorNumber) throws RemoteException {
        checkElevatorNumber(elevatorNumber);

        return elevators.get(elevatorNumber).getWeight();
    }

    @Override
    public int getElevatorCapacity(int elevatorNumber) throws RemoteException {
        checkElevatorNumber(elevatorNumber);

        return elevators.get(elevatorNumber).getCapacity();
    }

    @Override
    public boolean getFloorButtonDown(int floor) throws RemoteException {
        checkFloorNumber(floor);

        return floors.get(floor).isDownButtonActive();
    }

    @Override
    public boolean getFloorButtonUp(int floor) throws RemoteException {
        checkFloorNumber(floor);

        return floors.get(floor).isUpButtonActive();
    }

    @Override
    public int getFloorHeight() throws RemoteException {
        return floorHeight;
    }

    @Override
    public int getFloorNum() throws RemoteException {
        return numFloors;
    }

    @Override
    public boolean getServicesFloors(int elevatorNumber, int floor) throws RemoteException {
        checkElevatorNumber(elevatorNumber);
        checkFloorNumber(floor);

        return elevators.get(elevatorNumber).getServicesFloors(floor);
    }

    @Override
    public int getTarget(int elevatorNumber) throws RemoteException {
        checkElevatorNumber(elevatorNumber);

        return elevators.get(elevatorNumber).getTargetFloor();
    }

    @Override
    public void setCommittedDirection(int elevatorNumber, int direction) throws RemoteException {
        checkElevatorNumber(elevatorNumber);
        checkDirection(direction);

        elevators.get(elevatorNumber).setDirection(direction);
    }

    @Override
    public void setServicesFloors(int elevatorNumber, int floor, boolean service) throws RemoteException {
        checkElevatorNumber(elevatorNumber);
        checkFloorNumber(floor);

        elevators.get(elevatorNumber).setServicesFloors(floor, service);
    }

    @Override
    public void setTarget(int elevatorNumber, int target) throws RemoteException {
        checkElevatorNumber(elevatorNumber);
        checkFloorNumber(target);

        elevators.get(elevatorNumber).setTargetFloor(target);
        //sleep()
        elevators.get(elevatorNumber).setCurrentFloor(target);
    }

    @Override
    public long getClockTick() throws RemoteException {
        return CLOCK_TICK_MOCK_VALUE;
    }

    private void checkElevatorNumber(int elevatorNumber) throws MockElevatorException {
        if (elevatorNumber < 0 || elevatorNumber >= numElevators) {
            throw new MockElevatorException("MockElevatorState number is invalid!");
        }
    }

    private void checkFloorNumber(int floorNumber) throws MockElevatorException {
        if (floorNumber < 0 || floorNumber >= numFloors) {
            throw new MockElevatorException("MockFloorState number is invalid!");
        }
    }

    private void checkDirection(int direction) throws MockElevatorException {
        if (direction != IElevator.ELEVATOR_DIRECTION_DOWN &&
                direction != IElevator.ELEVATOR_DIRECTION_UNCOMMITTED &&
                direction != IElevator.ELEVATOR_DIRECTION_UP) {
            throw new MockElevatorException("Direction number is invalid!");
        }
    }

}