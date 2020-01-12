package at.fhhagenberg.sqelevator.viewmodel;

import at.fhhagenberg.sqelevator.model.ControlMode;
import at.fhhagenberg.sqelevator.model.Elevator;
import at.fhhagenberg.sqelevator.model.observers.Observable;
import at.fhhagenberg.sqelevator.model.observers.Observer;
import at.fhhagenberg.sqelevator.utils.UpdateIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ElevatorViewModel implements Observer<Elevator> {
    private SimpleBooleanProperty automaticMode = new SimpleBooleanProperty(false);

    private UpdateIntegerProperty acceleration = new UpdateIntegerProperty(0);
    private UpdateIntegerProperty currentFloor = new UpdateIntegerProperty(0);
    //TODO: other properties

    private Elevator elevatorModel;

    public ElevatorViewModel(Elevator elevatorModel) {
        this.elevatorModel = elevatorModel;

        this.elevatorModel.addObserver(this);

        automaticModeProperty().addListener((observableValue, oldValue, newValue) -> {
            if(newValue){
                elevatorModel.setControlMode(ControlMode.Automatic);
            }
            else {
                elevatorModel.setControlMode(ControlMode.Manual);
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

    public void setTarget(int floor) {
        if(!elevatorModel.gotoTarget(floor)){
            //add alarm
        }
    }

    @Override
    public void update(Observable<Elevator> observable) {
        var elevator = observable.getValue();

        acceleration.update(elevator.getAcceleration());
        currentFloor.update(elevator.getCurrentFloor());
        //TODO: update other properties
    }
}
