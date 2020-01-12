package at.fhhagenberg.sqelevator.model;

import at.fhhagenberg.sqelevator.model.observers.IAlarmsChangeObserver;
import at.fhhagenberg.sqelevator.model.observers.IBuildingInitializedObserver;
import at.fhhagenberg.sqelevator.model.observers.IElevatorStateChangedObserver;
import sqelevator.IElevator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ElevatorController implements IElevatorController {
    private IElevator elevatorService;

    private Building building;

    private int numElevators;
    private int numFloors;

    private List<Alarm> alarms;

    private List<IElevatorStateChangedObserver> elevatorChangeObservers;
    private List<IBuildingInitializedObserver> buildingInitializedObservers;
    private List<IAlarmsChangeObserver> alarmsChangeObservers;

    public ElevatorController(IElevator elevatorService) {
        this.elevatorService = elevatorService;

        alarms = new ArrayList<>();

        elevatorChangeObservers = new ArrayList<>();
        buildingInitializedObservers = new ArrayList<>();
        alarmsChangeObservers = new ArrayList<>();
    }

    public boolean isInitialized(){
        return building != null;
    }

    public void update() {
        try {
            updateInternal();
        } catch (RemoteException e) {
            e.printStackTrace();

            addAlert(e.getMessage());
        }
    }

    @Override
    public void addElevatorChangeObserver(IElevatorStateChangedObserver elevatorChangeObserver) {
        elevatorChangeObservers.add(elevatorChangeObserver);
    }

    @Override
    public void addBuildingInitializedObserver(IBuildingInitializedObserver buildingInitializedObserver) {
        buildingInitializedObservers.add(buildingInitializedObserver);
    }

    @Override
    public Building getCurrentState() {
        return building;
    }

    public void addAlarmsChangeObserver(IAlarmsChangeObserver alarmsChangeObserver) {
        alarmsChangeObservers.add(alarmsChangeObserver);
    }

    @Override
    public boolean setCommittedDirection(int elevatorNumber, int direction) {
        try {
            elevatorService.setCommittedDirection(elevatorNumber, direction);

            return true;
        } catch (RemoteException e) {
            e.printStackTrace();

            return false;
        }
    }

    @Override
    public boolean setServicesFloors(int elevatorNumber, int floor, boolean service) {
        try {
            elevatorService.setServicesFloors(elevatorNumber, floor, service);

            return true;
        } catch (RemoteException e) {
            e.printStackTrace();

            return false;
        }
    }

    @Override
    public boolean setTarget(int elevatorNumber, int target) {
        try {
            elevatorService.setTarget(elevatorNumber, target);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();

            return false;
        }
    }


    public void addAlert(String message) {
        addAlert(message, true);
    }

    public void addAlert(String message, boolean isError) {
        alarms.add(new Alarm(message, isError));

        notifyAlarmsChanged(new Alarm(message, isError));
    }

    public void initialize() {
        try {
            numElevators = elevatorService.getElevatorNum();
            numFloors = elevatorService.getFloorNum();

            building = new Building(numElevators, numFloors);

            for(Elevator elevator : building.getElevators()) {
                var eId = elevator.getId();
                elevator.setCapacity(elevatorService.getElevatorCapacity(eId)); //capacity is constant after initialize
            }
        } catch (RemoteException e) {
            e.printStackTrace();

            numElevators = 0;
            numFloors = 0;

            addAlert(e.getMessage());
        }

        notifyBuildingInitialized();
    }

    private void updateInternal() throws RemoteException {
        for(Elevator elevator : building.getElevators()){
            var eId = elevator.getId();
            elevator.setAcceleration(elevatorService.getElevatorAccel(eId));
            elevator.setCurrentFloor(elevatorService.getElevatorFloor(eId));
            elevator.setDirection(elevatorService.getCommittedDirection(eId));
            elevator.setDoorStatus(elevatorService.getElevatorDoorStatus(eId));
            elevator.setSpeed(elevatorService.getElevatorSpeed(eId));
            elevator.setTargetFloor(elevatorService.getElevatorFloor(eId));
            elevator.setWeight(elevatorService.getElevatorWeight(eId));

            //TODO:
            //elevator.setFloorButtonActive();
            //elevator.setServicesFloor();
        }

        for(Floor floor : building.getFloors()){
            var fId = floor.getId();
            floor.setDownButtonActive(elevatorService.getFloorButtonDown(fId));
            floor.setUpButtonActive(elevatorService.getFloorButtonUp(fId));
        }

        notifyElevatorStateChanged();
    }

    private void notifyBuildingInitialized(){
        for(IBuildingInitializedObserver observer : buildingInitializedObservers){
            observer.initializationDone();
        }
    }

    private void notifyElevatorStateChanged() {
        if(!isInitialized()){
            return;
        }

        for (IElevatorStateChangedObserver observer : elevatorChangeObservers) {
            observer.updateState();
        }
    }

    private void notifyAlarmsChanged(Alarm alarm) {
        for (IAlarmsChangeObserver observer : alarmsChangeObservers) {
            observer.newAlarm(alarm);
        }
    }
}
