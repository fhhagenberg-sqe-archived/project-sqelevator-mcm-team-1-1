package at.fhhagenberg.sqelevator.viewmodel;

import at.fhhagenberg.sqelevator.model.*;
import at.fhhagenberg.sqelevator.model.observers.IBuildingInitializedObserver;
import at.fhhagenberg.sqelevator.model.observers.Observable;
import at.fhhagenberg.sqelevator.model.observers.Observer;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class BuildingViewModel implements IBuildingInitializedObserver, Observer<AlarmsService> {
    private HashMap<Integer, ElevatorViewModel> elevatorViewModels = new HashMap<>();
    private HashMap<Integer, FloorViewModel> floorViewModels = new HashMap<>();

    SimpleObjectProperty buildingConfiguration = new SimpleObjectProperty();

    private ObservableList<AlarmViewModel> observableList = FXCollections.observableArrayList();
    private SimpleListProperty<AlarmViewModel> alarmViewModels = new SimpleListProperty<>(observableList);

    private SimpleStringProperty callInfo = new SimpleStringProperty();

    private IElevatorController elevatorController;

    public BuildingViewModel(IElevatorController elevatorController) {
        this.elevatorController = elevatorController;

        elevatorController.addInitializedObserver(this);

        AlarmsService.getInstance().addObserver(this);
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

    public ObservableList<AlarmViewModel> getAlarmViewModels() {
        return alarmViewModels.get();
    }

    public SimpleListProperty<AlarmViewModel> alarmViewModelsProperty() {
        return alarmViewModels;
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
//    public void gotoTarget(int elevatorNumber, int target){
//        elevatorController.gotoTarget(elevatorNumber, target);
//    }
//
//    //TODO: add setCommittedDirection and setServicesFloors methods

    @Override
    public void initializationDone() {
        var building = elevatorController.getCurrentState();

        elevatorViewModels.clear();

        for(Elevator elevator : building.getElevators()){
            var eId = elevator.getId();
            elevatorViewModels.put(eId, new ElevatorViewModel(elevator));
        }

        floorViewModels.clear();

        for(Floor floor : building.getFloors()){
            floorViewModels.put(floor.getId(), new FloorViewModel(floor));
        }

        buildingConfigurationProperty().set(new Object());
    }

    @Override
    public void update(Observable<AlarmsService> observable) {
        var alarmsService = observable.getValue();

        alarmViewModels.clear();

        for(Alarm alarm : alarmsService.getAlarms()){
            alarmViewModels.add(new AlarmViewModel(alarm.getMessage(), alarm.isError()));
        }
    }
}
