package at.fhhagenberg.sqelevator.model;

import at.fhhagenberg.sqelevator.model.observers.IBuildingInitializedObserver;

public interface IElevatorController {
    void addInitializedObserver(IBuildingInitializedObserver buildingInitializedObserver);

    Building getCurrentState();
}
