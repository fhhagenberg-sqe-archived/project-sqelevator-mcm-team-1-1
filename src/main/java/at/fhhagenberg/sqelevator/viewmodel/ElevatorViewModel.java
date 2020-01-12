package at.fhhagenberg.sqelevator.viewmodel;

import at.fhhagenberg.sqelevator.model.Elevator;
import at.fhhagenberg.sqelevator.model.IElevatorController;
import at.fhhagenberg.sqelevator.utils.UpdateIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ElevatorViewModel {
    private int id;

    private SimpleBooleanProperty automaticMode = new SimpleBooleanProperty(false);

    private UpdateIntegerProperty acceleration = new UpdateIntegerProperty(0);
    private UpdateIntegerProperty currentFloor = new UpdateIntegerProperty(0);
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

    public SimpleIntegerProperty accelerationProperty() {
        return acceleration;
    }

    public SimpleIntegerProperty currentFloorProperty() {
        return currentFloor;
    }

    public void setTarget(int floor) {
        elevatorController.setTarget(id, floor);
    }

    public void updateWith(Elevator elevator) {
        acceleration.update(elevator.getAcceleration());
        currentFloor.update(elevator.getCurrentFloor());
        //TODO: update other properties
    }
}
