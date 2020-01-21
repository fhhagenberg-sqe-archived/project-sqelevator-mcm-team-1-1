package at.fhhagenberg.sqelevator.mock;

import sqelevator.IElevator;

import java.util.ArrayList;
import java.util.List;

public class MockElevatorState {
    private int direction = IElevator.ELEVATOR_DIRECTION_UNCOMMITTED;
    private int acceleration = 0;
    private int doorStatus = IElevator.ELEVATOR_DOORS_CLOSED;
    private int currentFloor = 0;
    private int targetFloor = 0;
    private int speed = 0;
    private int weight = 0;
    private int capacity;

    private int numFloors;
    private List<Boolean> servicedFloors;
    private List<Boolean> floorButtons;

    public MockElevatorState(int numFloors) {
        this.numFloors = numFloors;

        servicedFloors = new ArrayList<>(numFloors);
        floorButtons = new ArrayList<>(numFloors);

        for (int i = 0; i < numFloors; i++) {
            servicedFloors.add(i, true);
            floorButtons.add(i, false);
        }
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public void setTargetFloor(int targetFloor) {
        this.targetFloor = targetFloor;
    }

    public boolean isFloorButtonActive(int floor) throws MockElevatorException {
        checkFloorNumber(floor);
        return floorButtons.get(floor);
    }

    public void setFloorButtonActive(int floor, boolean active) throws MockElevatorException {
        checkFloorNumber(floor);
        floorButtons.set(floor, active);
    }

    public boolean getServicesFloors(int floor) throws MockElevatorException {
        checkFloorNumber(floor);
        return servicedFloors.get(floor);
    }

    public void setServicesFloors(int floor, boolean service) throws MockElevatorException {
        checkFloorNumber(floor);
        servicedFloors.set(floor, service);
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(int acceleration) {
        this.acceleration = acceleration;
    }

    public int getDoorStatus() {
        return doorStatus;
    }

    public void setDoorStatus(int doorStatus) throws MockElevatorException {
        checkDoorStatus(doorStatus);
        this.doorStatus = doorStatus;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    private void checkFloorNumber(int floorNumber) throws MockElevatorException {
        if (floorNumber < 0 || floorNumber >= numFloors) {
            throw new MockElevatorException("MockFloorState number is invalid!");
        }
    }

    private void checkDoorStatus(int doorStatus) throws MockElevatorException {
        if (doorStatus != IElevator.ELEVATOR_DOORS_OPEN &&
                doorStatus != IElevator.ELEVATOR_DOORS_CLOSED &&
                doorStatus != IElevator.ELEVATOR_DOORS_OPENING &&
                doorStatus != IElevator.ELEVATOR_DOORS_CLOSING) {
            throw new MockElevatorException("Direction number is invalid!");
        }
    }
}
