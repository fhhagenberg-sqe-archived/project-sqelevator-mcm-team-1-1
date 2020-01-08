package at.fhhagenberg.sqelevator.model;

import at.fhhagenberg.sqelevator.IElevator;
import at.fhhagenberg.sqelevator.model.observers.IBuildingChangeObserver;
import at.fhhagenberg.sqelevator.model.observers.IElevatorChangeObserver;
import at.fhhagenberg.sqelevator.model.observers.IFloorChangeObserver;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ElevatorDataProvider implements IElevatorController{
    private IElevator elevatorService;

    private int numElevators;
    private int numFloors;

    private List<Alarm> alarms;

    private List<IElevatorChangeObserver> elevatorChangeObservers;
    private List<IFloorChangeObserver> floorChangeObservers;
    private List<IBuildingChangeObserver> buildingChangeObservers;

    public ElevatorDataProvider(IElevator elevatorService) {
        this.elevatorService = elevatorService;

        elevatorChangeObservers = new ArrayList<>();
        floorChangeObservers = new ArrayList<>();
        buildingChangeObservers = new ArrayList<>();

        initialize();
    }

    public void update(){
        try {
            updateInternal();
        } catch (RemoteException e) {
            e.printStackTrace();

            addAlert(e.getMessage());
        }
    }

    public void addElevatorChangeObserver(IElevatorChangeObserver elevatorChangeObserver) {
        elevatorChangeObservers.add(elevatorChangeObserver);
    }

    public void addFloorChangeObserver(IFloorChangeObserver floorChangeObserver) {
        floorChangeObservers.add(floorChangeObserver);
    }

    @Override
    public boolean setCommittedDirection(int elevatorNumber, int direction) {
        return false;
    }

    @Override
    public boolean setServicesFloors(int elevatorNumber, int floor, boolean service) {
        return false;
    }

    @Override
    public boolean setTarget(int elevatorNumber, int target) {
        return false;
    }

    private void initialize() {
        try {
            numElevators = elevatorService.getElevatorNum();
            numFloors = elevatorService.getFloorNum();
        } catch (RemoteException e) {
            e.printStackTrace();

            numElevators = 0;
            numFloors = 0;

            addAlert(e.getMessage());
        }

        var building = new Building(numElevators, numFloors);
        notifyBuildingChanged(building);
    }

    private void updateInternal() throws RemoteException {
        //TODO: store elevators/floors in a list and only notify if something changed

        for (int i = 0; i < numElevators; i++) {
            var elevator = new Elevator(i, numFloors, elevatorService.getElevatorCapacity(i));

            elevator.setAcceleration(elevatorService.getElevatorAccel(i));
            elevator.setCurrentFloor(elevatorService.getElevatorFloor(i));
            elevator.setDirection(elevatorService.getCommittedDirection(i));
            elevator.setDoorStatus(elevatorService.getElevatorDoorStatus(i));
            elevator.setSpeed(elevatorService.getElevatorSpeed(i));
            elevator.setTargetFloor(elevatorService.getElevatorFloor(i));
            elevator.setWeight(elevatorService.getElevatorWeight(i));

            //TODO:
            //elevator.setFloorButtonActive();
            //elevator.setServicesFloor();

            notifyElevatorChanged(elevator);
        }

        for (int i = 0; i < numFloors; i++) {
            var floor = new Floor(i);

            floor.setDownButtonActive(elevatorService.getFloorButtonDown(i));
            floor.setUpButtonActive(elevatorService.getFloorButtonUp(i));

            notifyFloorChanged(floor);
        }
    }

    private void addAlert(String message) {
        alarms.add(new Alarm(message, true));
    }

    private void notifyElevatorChanged(Elevator elevator) {
        for (IElevatorChangeObserver observer : elevatorChangeObservers) {
            observer.update(elevator);
        }
    }

    private void notifyFloorChanged(Floor floor) {
        for (IFloorChangeObserver observer : floorChangeObservers) {
            observer.update(floor);
        }
    }

    private void notifyBuildingChanged(Building building) {
        for (IBuildingChangeObserver observer : buildingChangeObservers) {
            observer.update(building);
        }
    }

}
