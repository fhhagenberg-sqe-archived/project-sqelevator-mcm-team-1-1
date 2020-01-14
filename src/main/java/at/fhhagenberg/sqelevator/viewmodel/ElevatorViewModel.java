package at.fhhagenberg.sqelevator.viewmodel;

import at.fhhagenberg.sqelevator.model.AlarmsService;
import at.fhhagenberg.sqelevator.model.ControlMode;
import at.fhhagenberg.sqelevator.model.Elevator;
import at.fhhagenberg.sqelevator.model.observers.Observable;
import at.fhhagenberg.sqelevator.model.observers.Observer;
import at.fhhagenberg.sqelevator.utils.UpdateBooleanProperty;
import at.fhhagenberg.sqelevator.utils.UpdateIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ElevatorViewModel implements Observer<Elevator> {
    public final static int ELEVATOR_DIRECTION_UP = 0;
    public final static int ELEVATOR_DIRECTION_DOWN = 1;
    public final static int ELEVATOR_DIRECTION_UNCOMMITTED = 2;

    public final static int ELEVATOR_DOORS_OPEN = 1;
    public final static int ELEVATOR_DOORS_CLOSED = 2;
    public final static int ELEVATOR_DOORS_OPENING = 3;
    public final static int ELEVATOR_DOORS_CLOSING = 4;

    private SimpleBooleanProperty automaticMode = new SimpleBooleanProperty(false);

    private UpdateIntegerProperty acceleration = new UpdateIntegerProperty(0);
    private UpdateIntegerProperty currentFloor = new UpdateIntegerProperty(0);
    private UpdateIntegerProperty currentDirection = new UpdateIntegerProperty(ELEVATOR_DIRECTION_UNCOMMITTED);
    private UpdateIntegerProperty doorStatus = new UpdateIntegerProperty(ELEVATOR_DOORS_CLOSED);
    private UpdateIntegerProperty speed = new UpdateIntegerProperty(0);
    private UpdateIntegerProperty targetFloor = new UpdateIntegerProperty(0);
    private UpdateIntegerProperty weight = new UpdateIntegerProperty(0);
    //TODO: other properties (floorbuttonactive, servicesfloor)

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

    public int getCurrentDirection() {
        return currentDirection.get();
    }

    public UpdateIntegerProperty currentDirectionProperty() {
        return currentDirection;
    }

    public int getDoorStatus() {
        return doorStatus.get();
    }

    public UpdateIntegerProperty doorStatusProperty() {
        return doorStatus;
    }

    public int getSpeed() {
        return speed.get();
    }

    public UpdateIntegerProperty speedProperty() {
        return speed;
    }

    public int getTargetFloor() {
        return targetFloor.get();
    }

    public UpdateIntegerProperty targetFloorProperty() {
        return targetFloor;
    }

    public int getWeight() {
        return weight.get();
    }

    public UpdateIntegerProperty weightProperty() {
        return weight;
    }

    public void setTarget(int floor) {
        if(!elevatorModel.gotoTarget(floor)){
        	AlarmsService.getInstance().addAlert("Failed to go to target", true);	
        }
    }

    public void setDirection(int elevatorDirectionDown) {
        if(!elevatorModel.sendCommittedDirection(elevatorDirectionDown)){
        	AlarmsService.getInstance().addAlert("Failed to set committed direction", true);	
        }
    }

    @Override
    public void update(Observable<Elevator> observable) {
    	
        var elevator = observable.getValue();
        System.out.println("update " + elevator.getTargetFloor());

        acceleration.update(elevator.getAcceleration());
        currentFloor.update(elevator.getCurrentFloor());
        currentDirection.update(elevator.getDirection());
        doorStatus.update(elevator.getDoorStatus());
        speed.update(elevator.getSpeed());
        targetFloor.update(elevator.getTargetFloor());
        weight.update(elevator.getWeight());
       

        //TODO: floorbuttonactive, servicesfloor
    }
}
