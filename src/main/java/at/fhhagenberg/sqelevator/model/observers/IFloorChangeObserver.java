package at.fhhagenberg.sqelevator.model.observers;

import at.fhhagenberg.sqelevator.model.Floor;

public interface IFloorChangeObserver {
    void update(Floor floor);
}
