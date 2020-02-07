package at.fhhagenberg.sqelevator.model;

import at.fhhagenberg.sqelevator.model.observers.ObservableAdapter;
import sqelevator.IElevator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Elevator extends ObservableAdapter<Elevator> {
    private int id = 0;
    private IElevator elevatorService;

    private ControlMode controlMode = ControlMode.AUTOMATIC;
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

    private int numFloors = 0;

    public Elevator(int id, int numFloors, IElevator elevatorService) {
        this.id = id;
        this.elevatorService = elevatorService;

        servicedFloors = new ArrayList<>(numFloors);
        floorButtons = new ArrayList<>(numFloors);
        this.numFloors = numFloors;

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
        notifyListeners();
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

        var newAcceleration = elevatorService.getElevatorAccel(id);
        if (newAcceleration != acceleration) {
            changed = true;
            acceleration = newAcceleration;
        }

        var newCurrentFloor = elevatorService.getElevatorFloor(id);
        if (newCurrentFloor != currentFloor) {
            changed = true;
            currentFloor = newCurrentFloor;
        }

        var newDirection = elevatorService.getCommittedDirection(id);
        if (newDirection != direction) {
            changed = true;
            direction = newDirection;
        }

        var newDoorStatus = elevatorService.getElevatorDoorStatus(id);
        if (newDoorStatus != doorStatus) {
            changed = true;
            doorStatus = newDoorStatus;
        }

        var newSpeed = elevatorService.getElevatorSpeed(id);
        if (newSpeed != speed) {
            changed = true;
            speed = newSpeed;
        }

        var newTargetFloor = elevatorService.getTarget(id);
        if (newTargetFloor != targetFloor) {
            changed = true;
            targetFloor = newTargetFloor;
        }

        var newWeight = elevatorService.getElevatorWeight(id);
        if (newWeight != weight) {
            changed = true;
            weight = newWeight;
        }

        for (int i = 0; i < floorButtons.size(); i++) {
            var newServicedFloor = elevatorService.getServicesFloors(id, i);
            if (newServicedFloor != servicedFloors.get(i)) {
                changed = true;
                servicedFloors.set(i, newServicedFloor);
            }
            var newFloorButton = elevatorService.getElevatorButton(id, i);
            if (newFloorButton != floorButtons.get(i)) {
                changed = true;
                floorButtons.set(i, newFloorButton);
            }
        }

        if (changed) {
            if (currentFloor == targetFloor) {
                // special case to reset direction status
                sendCommittedDirection(IElevator.ELEVATOR_DIRECTION_UNCOMMITTED);
            }
            notifyListeners();
        }
    }

    public boolean sendCommittedDirection(int direction) {
        try {
            elevatorService.setCommittedDirection(id, direction);
            return true;
        } catch (RemoteException e) {
            AlarmsService.getInstance().addWarning(e.getMessage());
            return false;
        }
    }

    public boolean sendServicesFloors(int floor, boolean service) {
        try {
            elevatorService.setServicesFloors(id, floor, service);
            return true;
        } catch (RemoteException e) {
            AlarmsService.getInstance().addWarning(e.getMessage());
            return false;
        }
    }

    public boolean gotoTarget(int target) {
        try {
            elevatorService.setTarget(id, target);
            return true;
        } catch (RemoteException e) {
            AlarmsService.getInstance().addWarning(e.getMessage());
            return false;
        }
    }

    public boolean gotoTargetAndSendDirection(int floor) {
        if (!gotoTarget(floor)) {
            return false;
        }

        int direction = floor < currentFloor ?
                IElevator.ELEVATOR_DIRECTION_DOWN :
                IElevator.ELEVATOR_DIRECTION_UP;

        if (!sendCommittedDirection(direction)) {
            return false;
        }

        return true;
    }

    @Override
    public Elevator getValue() {
        return this;
    }

    public int getNumFloors() {
        return this.numFloors;
    }
}
