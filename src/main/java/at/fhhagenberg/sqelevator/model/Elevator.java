package at.fhhagenberg.sqelevator.model;

import at.fhhagenberg.sqelevator.model.observers.ObservableAdapter;
import sqelevator.IElevator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Elevator extends ObservableAdapter<Elevator> {
    private int id = 0;
    private IElevator elevatorService;

    private ControlMode controlMode = ControlMode.Automatic;
    private int direction = IElevator.ELEVATOR_DIRECTION_UNCOMMITTED;
    private int acceleration = 0;
    private int doorStatus = IElevator.ELEVATOR_DOORS_CLOSED;
    private int currentFloor = 0;
    private int targetFloor = 0;
    private int speed = 0;
    private int weight = 0;
    private int capacity;

    private List<Boolean> servicedFloors;
    private List<Boolean> floorButtons;

    public Elevator(int id, int numFloors, IElevator elevatorService) {
        this.id = id;
        this.elevatorService = elevatorService;

        servicedFloors = new ArrayList<>(numFloors);
        floorButtons = new ArrayList<>(numFloors);

        for (int i = 0; i < numFloors; i++) {
            servicedFloors.add(i, true);
            floorButtons.add(i, false);
        }
    }

    public int getId() {
        return id;
    }

    public ControlMode getControlMode() {
        return controlMode;
    }

    public void setControlMode(ControlMode controlMode) {
        this.controlMode = controlMode;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public boolean isFloorButtonActive(int floor) {
        return floorButtons.get(floor);
    }

    public boolean getServicesFloors(int floor) {
        return servicedFloors.get(floor);
    }

    public int getDirection() {
        return direction;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public int getDoorStatus() {
        return doorStatus;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public int getSpeed() {
        return speed;
    }

    public int getWeight() {
        return weight;
    }

    public int getCapacity() {
        return capacity;
    }

    public void updateFromService() throws RemoteException {
        var changed = false;

        var newCapacity = elevatorService.getElevatorCapacity(id);
        if (newCapacity != capacity) {
            changed = true;
            capacity = newCapacity;
        }

        var newAccelaration = elevatorService.getElevatorAccel(id);
        if (newAccelaration != acceleration) {
            changed = true;
            acceleration = newAccelaration;
        }

        var newCurrentFloor = elevatorService.getElevatorFloor(id);
        if (newCurrentFloor != currentFloor) {
            changed = true;
            currentFloor = newCurrentFloor;
        }

        var newDirection = elevatorService.getCommittedDirection(id);
        if(newDirection != direction){
            changed = true;
            direction = newDirection;
        }

        if (changed) {
            notifyListeners();
        }

        //TODO:
        // direction, doorstatus, speed, targetfloor, weight, floorbuttonactive, servicesfloor
        // (like above)
    }

    public boolean sendCommittedDirection(int direction) {
        try {
            elevatorService.setCommittedDirection(id, direction);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean sendServicesFloors(int floor, boolean service) {
        try {
            elevatorService.setServicesFloors(id, floor, service);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();

            return false;
        }
    }

    public boolean gotoTarget(int target) {
        try {
            elevatorService.setTarget(id, target);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();

            return false;
        }
    }

    @Override
    public Elevator getValue() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elevator elevator = (Elevator) o;
        return id == elevator.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
