package at.fhhagenberg.sqelevator.model.observers;

import at.fhhagenberg.sqelevator.model.Floor;

public interface FloorChangeObserver {
    void update(Floor floor);
}
