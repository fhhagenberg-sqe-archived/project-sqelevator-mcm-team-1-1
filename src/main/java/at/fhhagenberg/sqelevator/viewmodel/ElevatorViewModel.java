package at.fhhagenberg.sqelevator.viewmodel;

import at.fhhagenberg.sqelevator.model.IElevatorController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ElevatorViewModel {
    private int id;

    private SimpleBooleanProperty automaticMode = new SimpleBooleanProperty(false);

    private SimpleIntegerProperty acceleration = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty currentFloor = new SimpleIntegerProperty(0);
    //TODO: other properties

    private IElevatorController elevatorController;

    public ElevatorViewModel(int id, IElevatorController elevatorController) {
        this.id = id;
        this.elevatorController = elevatorController;
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

    public int getAcceleration() {
        return acceleration.get();
    }

    public SimpleIntegerProperty accelerationProperty() {
        return acceleration;
    }

    public void setAcceleration(int acceleration) {
        this.acceleration.set(acceleration);
    }

    public int getCurrentFloor() {
        return currentFloor.get();
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor.set(currentFloor);
    }

    public SimpleIntegerProperty currentFloorProperty() {
        return currentFloor;
    }

    public void setTarget(int floor) {
        elevatorController.setTarget(id, floor);
    }


}
