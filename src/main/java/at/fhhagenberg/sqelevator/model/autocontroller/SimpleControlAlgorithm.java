package at.fhhagenberg.sqelevator.model.autocontroller;

import at.fhhagenberg.sqelevator.model.ControlMode;
import at.fhhagenberg.sqelevator.model.Floor;
import at.fhhagenberg.sqelevator.model.IElevatorController;
import at.fhhagenberg.sqelevator.model.observers.IBuildingInitializedObserver;
import at.fhhagenberg.sqelevator.model.observers.Observable;
import at.fhhagenberg.sqelevator.model.observers.Observer;

public class SimpleControlAlgorithm implements IControlAlgorithm, IBuildingInitializedObserver, Observer<Floor> {
    private IElevatorController elevatorController;

    @Override
    public void setElevatorController(IElevatorController elevatorController) {
        this.elevatorController = elevatorController;
    }

    @Override
    public void start() {
        this.elevatorController.addInitializedObserver(this);
    }

    @Override
    public void stop() {
        var building = elevatorController.getCurrentState();

        for (Floor floor : building.getFloors()) {
            floor.removeObserver(this);
        }
    }


    @Override
    public void initializationDone() {
        var building = elevatorController.getCurrentState();

        for (Floor floor : building.getFloors()) {
            floor.addObserver(this);
        }
    }

    @Override
    public void update(Observable<Floor> observable) {
        var building = elevatorController.getCurrentState();
        var floor = observable.getValue();

        if (floor.isDownButtonActive() && building.getElevator(1).getControlMode() == ControlMode.AUTOMATIC) {
            building.getElevator(1).gotoTarget(floor.getId());

            System.out.println("Sending elevator to floor " + floor.getId());
        }
    }
}
