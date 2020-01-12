package at.fhhagenberg.sqelevator.viewmodel;

import at.fhhagenberg.sqelevator.model.*;
import at.fhhagenberg.sqelevator.model.observers.IAlarmsChangeObserver;
import at.fhhagenberg.sqelevator.model.observers.IBuildingInitializedObserver;
import at.fhhagenberg.sqelevator.model.observers.IElevatorStateChangedObserver;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class BuildingViewModel implements IBuildingInitializedObserver, IElevatorStateChangedObserver, IAlarmsChangeObserver {
    private HashMap<Integer, ElevatorViewModel> elevatorViewModels = new HashMap<>();
    private HashMap<Integer, FloorViewModel> floorViewModels = new HashMap<>();

    SimpleObjectProperty buildingConfiguration = new SimpleObjectProperty();

    private ObservableList<AlarmViewModel> observableList = FXCollections.observableArrayList();
    private SimpleListProperty<AlarmViewModel> alarms = new SimpleListProperty<>(observableList);

    private SimpleStringProperty callInfo = new SimpleStringProperty();

    private IElevatorController elevatorController;

    public BuildingViewModel(IElevatorController elevatorController) {
        this.elevatorController = elevatorController;

        elevatorController.addBuildingInitializedObserver(this);
        elevatorController.addElevatorChangeObserver(this);
    }

    public HashMap<Integer, ElevatorViewModel> getElevatorViewModels() {
        return elevatorViewModels;
    }

    public HashMap<Integer, FloorViewModel> getFloorViewModels() {
        return floorViewModels;
    }

    public SimpleObjectProperty buildingConfigurationProperty() {
        return buildingConfiguration;
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
//
//    //to set the target in manual mode
//    public void setTarget(int elevatorNumber, int target){
//        elevatorController.setTarget(elevatorNumber, target);
//    }
//
//    //TODO: add setCommittedDirection and setServicesFloors methods

    @Override
    public void initializationDone() {
        var building = elevatorController.getCurrentState();

        elevatorViewModels.clear();

        for(Elevator elevator : building.getElevators()){
            var eId = elevator.getId();
            elevatorViewModels.put(eId, new ElevatorViewModel(eId, elevatorController));
        }

        floorViewModels.clear();

        for(Floor floor : building.getFloors()){
            floorViewModels.put(floor.getId(), new FloorViewModel());
        }

        buildingConfigurationProperty().set(new Object());
    }

    @Override
    public void updateState() {
        var building = elevatorController.getCurrentState();

        for(Elevator elevator : building.getElevators()){
            elevatorViewModels.get(elevator.getId()).updateWith(elevator);
        }

        for(Floor floor : building.getFloors()){
            floorViewModels.get(floor.getId()).updateWith(floor);
        }
    }

    @Override
    public void newAlarm(Alarm alarm) {
        alarms.add(new AlarmViewModel(alarm.getMessage(), alarm.isError()));
    }
}
