package at.fhhagenberg.sqelevator.viewmodel;

import at.fhhagenberg.sqelevator.model.AlarmsService;
import at.fhhagenberg.sqelevator.model.ControlMode;
import at.fhhagenberg.sqelevator.model.Elevator;
import at.fhhagenberg.sqelevator.model.observers.Observable;
import at.fhhagenberg.sqelevator.model.observers.Observer;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.converter.NumberStringConverter;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class ElevatorViewModel implements Observer<Elevator> {

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("elevatorCC");
	public static final int ELEVATOR_DIRECTION_UP = 0;
	public static final int ELEVATOR_DIRECTION_DOWN = 1;
	public static final int ELEVATOR_DIRECTION_UNCOMMITTED = 2;

	public static final int ELEVATOR_DOORS_OPEN = 1;
	public static final int ELEVATOR_DOORS_CLOSED = 2;
	public static final int ELEVATOR_DOORS_OPENING = 3;
	public static final int ELEVATOR_DOORS_CLOSING = 4;

	private SimpleBooleanProperty manualMode = new SimpleBooleanProperty(false);

	private SimpleIntegerProperty acceleration = new SimpleIntegerProperty(Integer.MIN_VALUE);
	private SimpleIntegerProperty currentFloor = new SimpleIntegerProperty(Integer.MIN_VALUE);
	private SimpleIntegerProperty currentDirection = new SimpleIntegerProperty(Integer.MIN_VALUE);
	private SimpleIntegerProperty doorStatus = new SimpleIntegerProperty(Integer.MIN_VALUE);
	private SimpleIntegerProperty speed = new SimpleIntegerProperty(Integer.MIN_VALUE);
	private SimpleIntegerProperty targetFloor = new SimpleIntegerProperty(Integer.MIN_VALUE);
	private SimpleIntegerProperty weight = new SimpleIntegerProperty(Integer.MIN_VALUE);

	private ArrayList<SimpleBooleanProperty> floorbuttonActive = new ArrayList<>();
	private ArrayList<SimpleBooleanProperty> servicedfloorActive = new ArrayList<>();

	private SimpleStringProperty doorStatusText = new SimpleStringProperty("-");
	private SimpleStringProperty targetFloorText = new SimpleStringProperty("-");

	private Elevator elevatorModel;

	public ElevatorViewModel(Elevator elevatorModel) {
		this.elevatorModel = elevatorModel;

		this.elevatorModel.addObserver(this);

		for (int i = 0; i < elevatorModel.getNumFloors(); i++) {
			this.floorbuttonActive.add(i, new SimpleBooleanProperty(false));
			this.servicedfloorActive.add(i, new SimpleBooleanProperty(false));

			final var floor = i;
			this.servicedfloorActive.get(i).addListener((observableValue, oldValue, newValue) -> {
				this.setServicesFloor(floor, newValue);
			});
		}

		manualModeProperty().addListener((observableValue, oldValue, newValue) -> {
			if (Boolean.TRUE.equals(newValue)) {
				elevatorModel.setControlMode(ControlMode.MANUAL);
			} else {
				elevatorModel.setControlMode(ControlMode.AUTOMATIC);
			}
		});

		doorStatusProperty().addListener((observableValue, number, newStatus) -> {
			switch (newStatus.intValue()) {
			case ELEVATOR_DOORS_OPEN:
				doorStatusText.set(RESOURCE_BUNDLE.getString("status_open"));
				break;
			case ELEVATOR_DOORS_CLOSED:
				doorStatusText.set(RESOURCE_BUNDLE.getString("status_closed"));
				break;
			case ELEVATOR_DOORS_OPENING:
				doorStatusText.set(RESOURCE_BUNDLE.getString("status_opening"));
				break;
			case ELEVATOR_DOORS_CLOSING:
				doorStatusText.set(RESOURCE_BUNDLE.getString("status_closing"));
				break;
			default:
				doorStatusText.set(RESOURCE_BUNDLE.getString("error"));
			}
		});

		targetFloorProperty().addListener((observableValue, s, newValue) -> {
			var nsc = new NumberStringConverter();
			targetFloorText.set(nsc.toString(convertFloorNumberForUi(newValue.intValue())));
		});
	}

	public boolean isManualMode() {
		return manualMode.get();
	}

	public SimpleBooleanProperty manualModeProperty() {
		return manualMode;
	}

	public SimpleBooleanProperty floorbuttonActiveProperty(int floor) {
		return floorbuttonActive.get(floor);
	}

	public SimpleBooleanProperty servicedfloorActiveProperty(int floor) {
		return servicedfloorActive.get(floor);
	}

	public SimpleIntegerProperty accelerationProperty() {
		return acceleration;
	}

	public SimpleIntegerProperty currentFloorProperty() {
		return currentFloor;
	}

	public SimpleIntegerProperty currentDirectionProperty() {
		return currentDirection;
	}

	public SimpleIntegerProperty doorStatusProperty() {
		return doorStatus;
	}

	public SimpleIntegerProperty speedProperty() {
		return speed;
	}

	public SimpleIntegerProperty targetFloorProperty() {
		return targetFloor;
	}

	public SimpleIntegerProperty weightProperty() {
		return weight;
	}

	public SimpleStringProperty doorStatusTextProperty() {
		return doorStatusText;
	}

	public SimpleStringProperty targetFloorTextProperty() {
		return targetFloorText;
	}

	public void setTargetAndDirection(int floor) {
		if (elevatorModel.gotoTarget(floor)) {
			if (floor < currentFloor.get()) {
				setDirection(ElevatorViewModel.ELEVATOR_DIRECTION_DOWN);
			} else {
				setDirection(ElevatorViewModel.ELEVATOR_DIRECTION_UP);
			}
		} else {
			AlarmsService.getInstance().addAlert(RESOURCE_BUNDLE.getString("error_go_to_target"), true);
		}
	}

	public void setDirection(int elevatorDirectionDown) {
		if (!elevatorModel.sendCommittedDirection(elevatorDirectionDown)) {
			AlarmsService.getInstance().addAlert(RESOURCE_BUNDLE.getString("error_committed_direction"), true);
		}
	}

	public void setServicesFloor(int floor, boolean service) {
		if (!elevatorModel.sendServicesFloors(floor, service)) {
			AlarmsService.getInstance().addAlert(RESOURCE_BUNDLE.getString("error_set_service_floor"), true);
		}
	}

	@Override
	public void update(Observable<Elevator> observable) {
		var elevator = observable.getValue();

		Platform.runLater(() -> { // run update in UI thread
			acceleration.set(elevator.getAcceleration());
			currentFloor.set(elevator.getCurrentFloor());
			currentDirection.set(elevator.getDirection());
			doorStatus.set(elevator.getDoorStatus());
			speed.set(elevator.getSpeed());
			targetFloor.set(elevator.getTargetFloor());
			weight.set(elevator.getWeight());
			manualMode.set(elevator.getControlMode().equals(ControlMode.MANUAL));

			for (int i = 0; i < elevatorModel.getNumFloors(); i++) {
				floorbuttonActive.get(i).set(elevator.isFloorButtonActive(i));
				servicedfloorActive.get(i).set(elevator.getServicesFloors(i));
			}
		});
	}

	private int convertFloorNumberForUi(int floor) {
		return floor + 1;
	}
}
