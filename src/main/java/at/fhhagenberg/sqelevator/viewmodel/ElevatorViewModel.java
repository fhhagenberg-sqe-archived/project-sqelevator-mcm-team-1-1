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

public class ElevatorViewModel implements Observer<Elevator> {
    public static final int ELEVATOR_DIRECTION_UP = 0;
    public static final int ELEVATOR_DIRECTION_DOWN = 1;
    public static final int ELEVATOR_DIRECTION_UNCOMMITTED = 2;

    public static final int ELEVATOR_DOORS_OPEN = 1;
    public static final int ELEVATOR_DOORS_CLOSED = 2;
    public static final int ELEVATOR_DOORS_OPENING = 3;
    public static final int ELEVATOR_DOORS_CLOSING = 4;

    private SimpleBooleanProperty automaticMode = new SimpleBooleanProperty(false);

    private SimpleIntegerProperty acceleration = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty currentFloor = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty currentDirection = new SimpleIntegerProperty(ELEVATOR_DIRECTION_UNCOMMITTED);
    private SimpleIntegerProperty doorStatus = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty speed = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty targetFloor = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty weight = new SimpleIntegerProperty(0);
    //TODO: other properties (floorbuttonactive, servicesfloor)

    private SimpleStringProperty doorStatusText = new SimpleStringProperty("-");

    private Elevator elevatorModel;

    public ElevatorViewModel(Elevator elevatorModel) {
        this.elevatorModel = elevatorModel;

        this.elevatorModel.addObserver(this);

        automaticModeProperty().addListener((observableValue, oldValue, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                elevatorModel.setControlMode(ControlMode.AUTOMATIC);
            } else {
                elevatorModel.setControlMode(ControlMode.MANUAL);
            }
        });

        doorStatusProperty().addListener((observableValue, number, newStatus) -> {
            switch (newStatus.intValue()) {
                case ELEVATOR_DOORS_OPEN:
                    doorStatusText.set("Open");
                    break;
                case ELEVATOR_DOORS_CLOSED:
                    doorStatusText.set("Closed");
                    break;
                case ELEVATOR_DOORS_OPENING:
                    doorStatusText.set("Opening");
                    break;
                case ELEVATOR_DOORS_CLOSING:
                    doorStatusText.set("Closing");
                    break;
                default:
                    doorStatusText.set("Error");
            }
        });
    }

    public boolean isAutomaticMode() {
        return automaticMode.get();
    }

    public SimpleBooleanProperty automaticModeProperty() {
        return automaticMode;
    }

    public void setAutomaticMode(boolean automaticMode) {
        this.automaticMode.set(automaticMode);
    }

    public SimpleIntegerProperty accelerationProperty() {
        return acceleration;
    }

    public SimpleIntegerProperty currentFloorProperty() {
        return currentFloor;
    }

    public int getCurrentDirection() {
        return currentDirection.get();
    }

    public SimpleIntegerProperty currentDirectionProperty() {
        return currentDirection;
    }

    public int getDoorStatus() {
        return doorStatus.get();
    }

    public SimpleIntegerProperty doorStatusProperty() {
        return doorStatus;
    }

    public int getSpeed() {
        return speed.get();
    }

    public SimpleIntegerProperty speedProperty() {
        return speed;
    }

    public int getTargetFloor() {
        return targetFloor.get();
    }

    public SimpleIntegerProperty targetFloorProperty() {
        return targetFloor;
    }

    public int getWeight() {
        return weight.get();
    }

    public SimpleIntegerProperty weightProperty() {
        return weight;
    }

    public String getDoorStatusText() {
        return doorStatusText.get();
    }

    public SimpleStringProperty doorStatusTextProperty() {
        return doorStatusText;
    }

    public void setTargetAndDirection(int floor) {
        if (elevatorModel.gotoTarget(floor)) {
            if (floor < currentFloor.get()) {
                setDirection(ElevatorViewModel.ELEVATOR_DIRECTION_DOWN);
            } else {
                setDirection(ElevatorViewModel.ELEVATOR_DIRECTION_UP);
            }
        } else {
            AlarmsService.getInstance().addAlert("Failed to go to target", true);
        }
    }


    public void setDirection(int elevatorDirectionDown) {
        if (!elevatorModel.sendCommittedDirection(elevatorDirectionDown)) {
            AlarmsService.getInstance().addAlert("Failed to set committed direction", true);
        }
    }

    @Override
    public void update(Observable<Elevator> observable) {
        var elevator = observable.getValue();

        Platform.runLater(() -> {   //run update in UI thread
            acceleration.set(elevator.getAcceleration());
            currentFloor.set(elevator.getCurrentFloor());
            currentDirection.set(elevator.getDirection());
            doorStatus.set(elevator.getDoorStatus());
            speed.set(elevator.getSpeed());
            targetFloor.set(elevator.getTargetFloor());
            weight.set(elevator.getWeight());
            //TODO: floorbuttonactive, servicesfloor
        });
    }
}
