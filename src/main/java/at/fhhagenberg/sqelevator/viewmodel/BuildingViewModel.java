package at.fhhagenberg.sqelevator.viewmodel;

import at.fhhagenberg.sqelevator.model.*;
import at.fhhagenberg.sqelevator.model.observers.IBuildingInitializedObserver;
import at.fhhagenberg.sqelevator.model.observers.Observable;
import at.fhhagenberg.sqelevator.model.observers.Observer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

public class BuildingViewModel implements IBuildingInitializedObserver, Observer<AlarmsService> {
	private Map<Integer, ElevatorViewModel> elevatorViewModels = new HashMap<>();
	private Map<Integer, FloorViewModel> floorViewModels = new HashMap<>();

	private SimpleObjectProperty<Object> buildingConfiguration = new SimpleObjectProperty<Object>();

	private SimpleBooleanProperty enableEditMode = new SimpleBooleanProperty(false);

	private ObservableList<AlarmViewModel> observableList = FXCollections.observableArrayList();
	private SimpleListProperty<AlarmViewModel> alarmViewModels = new SimpleListProperty<>(observableList);

	private SimpleStringProperty callInfo = new SimpleStringProperty();

	private IElevatorController elevatorController;

	public BuildingViewModel(IElevatorController elevatorController) {
		this.elevatorController = elevatorController;

		elevatorController.addInitializedObserver(this);

		AlarmsService.getInstance().addObserver(this);
	}

	public Map<Integer, ElevatorViewModel> getElevatorViewModels() {
		return elevatorViewModels;
	}

	public Map<Integer, FloorViewModel> getFloorViewModels() {
		return floorViewModels;
	}

	public SimpleObjectProperty<Object> buildingConfigurationProperty() {
		return buildingConfiguration;
	}

	public boolean isEnableEditMode() {
		return enableEditMode.get();
	}

	public SimpleBooleanProperty enableEditModeProperty() {
		return enableEditMode;
	}

	public void setEnableEditMode(boolean enableEditMode) {
		this.enableEditMode.set(enableEditMode);
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

	@Override
	public void initializationDone() {
		var building = elevatorController.getCurrentState();

		elevatorViewModels.clear();

		for (Elevator elevator : building.getElevators()) {
			var eId = elevator.getId();
			elevatorViewModels.put(eId, new ElevatorViewModel(elevator));
		}

		floorViewModels.clear();

		for (Floor floor : building.getFloors()) {
			floorViewModels.put(floor.getId(), new FloorViewModel(floor));
		}

		buildingConfigurationProperty().set(new Object());
	}

	@Override
	public void update(Observable<AlarmsService> observable) {
		var alarmsService = observable.getValue();

		alarmViewModels.clear();

		for (Alarm alarm : alarmsService.getAlarms()) {
			alarmViewModels.add(new AlarmViewModel(alarm.getMessage(), alarm.isError()));
		}
	}
}
