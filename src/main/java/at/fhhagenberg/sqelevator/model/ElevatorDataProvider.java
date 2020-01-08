package at.fhhagenberg.sqelevator.model;

import at.fhhagenberg.sqelevator.IElevator;
import at.fhhagenberg.sqelevator.model.observers.BuildingChangeObserver;
import at.fhhagenberg.sqelevator.model.observers.ElevatorChangeObserver;
import at.fhhagenberg.sqelevator.model.observers.FloorChangeObserver;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ElevatorDataProvider {
    private IElevator elevatorService;

    private int numElevators;
    private int numFloors;

    private List<Alarm> alarms;

    private List<ElevatorChangeObserver> elevatorChangeObservers;
    private List<FloorChangeObserver> floorChangeObservers;
    private List<BuildingChangeObserver> buildingChangeObservers;

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

    public void addElevatorChangeObserver(ElevatorChangeObserver elevatorChangeObserver) {
        elevatorChangeObservers.add(elevatorChangeObserver);
    }

    public void addFloorChangeObserver(FloorChangeObserver floorChangeObserver) {
        floorChangeObservers.add(floorChangeObserver);
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
        for (ElevatorChangeObserver observer : elevatorChangeObservers) {
            observer.update(elevator);
        }
    }

    private void notifyFloorChanged(Floor floor) {
        for (FloorChangeObserver observer : floorChangeObservers) {
            observer.update(floor);
        }
    }

    private void notifyBuildingChanged(Building building) {
        for (BuildingChangeObserver observer : buildingChangeObservers) {
            observer.update(building);
        }
    }
}
