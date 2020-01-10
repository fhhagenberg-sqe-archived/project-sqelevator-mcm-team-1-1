package at.fhhagenberg.sqelevator.viewmodel;

import at.fhhagenberg.sqelevator.model.*;
import at.fhhagenberg.sqelevator.model.observers.IAlarmsChangeObserver;
import at.fhhagenberg.sqelevator.model.observers.IBuildingChangeObserver;
import at.fhhagenberg.sqelevator.model.observers.IElevatorChangeObserver;
import at.fhhagenberg.sqelevator.model.observers.IFloorChangeObserver;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class BuildingViewModel implements IBuildingChangeObserver, IFloorChangeObserver, IElevatorChangeObserver, IAlarmsChangeObserver {
    private HashMap<Integer, ElevatorViewModel> elevators = new HashMap<>();
    private HashMap<Integer, FloorViewModel> floors = new HashMap<>();

    SimpleObjectProperty elevatorFloorConfiguration = new SimpleObjectProperty();

    private ObservableList<AlarmViewModel> observableList = FXCollections.observableArrayList();
    private SimpleListProperty<AlarmViewModel> alarms = new SimpleListProperty<>(observableList);

    private SimpleStringProperty callInfo = new SimpleStringProperty();

    private IElevatorController elevatorController;

    public BuildingViewModel(IElevatorController elevatorController) {
        this.elevatorController = elevatorController;
    }

    public HashMap<Integer, ElevatorViewModel> getElevators() {
        return elevators;
    }

    public HashMap<Integer, FloorViewModel> getFloors() {
        return floors;
    }

    public Object getElevatorFloorConfiguration() {
        return elevatorFloorConfiguration.get();
    }

    public SimpleObjectProperty elevatorFloorConfigurationProperty() {
        return elevatorFloorConfiguration;
    }

    public ObservableList<AlarmViewModel> getAlarms() {
        return alarms.get();
    }

    public SimpleListProperty<AlarmViewModel> alarmsProperty() {
        return alarms;
    }

	public String getCallInfo() {
		return callInfo.get();
	}

	public SimpleStringProperty callInfoProperty() {
		return callInfo;
	}

	public void setCallInfo(String callInfo) {
		this.callInfo.set(callInfo);
	}
    
    //to set the target in manual mode
    public void setTarget(int elevatorNumber, int target){
        elevatorController.setTarget(elevatorNumber, target);
    }

    //TODO: add setCommittedDirection and setServicesFloors methods

    @Override
    public void update(Building building) {
        elevators.clear();

        for(int i=0;i<building.getNumElevators();i++){
            elevators.put(i, new ElevatorViewModel(i, elevatorController));
        }

        floors.clear();

        for(int i=0;i<building.getNumFloors();i++){
            floors.put(i, new FloorViewModel());
        }

        elevatorFloorConfigurationProperty().set(new Object());
    }

    @Override
    public void update(Elevator elevator) {
        if(elevators.size() == 0){
            return; //elevatorFloorConfiguration not yet initialized
        }

        elevators.get(elevator.getId()).setAcceleration(elevator.getAcceleration());
        elevators.get(elevator.getId()).setCurrentFloor(elevator.getCurrentFloor());

        //TODO: update all properties
    }

    @Override
    public void update(Floor floor) {
        if(floors.size() == 0){
            return; //elevatorFloorConfiguration not yet initialized
        }

        floors.get(floor.getId()).setDownButtonActive(floor.isDownButtonActive());
        floors.get(floor.getId()).setUpButtonActive(floor.isUpButtonActive());
    }

    @Override
    public void newAlarm(Alarm alarm) {
        alarms.add(new AlarmViewModel(alarm.getMessage(), alarm.isError()));
    }
}
