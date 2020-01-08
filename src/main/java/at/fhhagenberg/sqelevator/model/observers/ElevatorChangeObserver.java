package at.fhhagenberg.sqelevator.model.observers;

import at.fhhagenberg.sqelevator.model.Elevator;

public interface ElevatorChangeObserver {
    void update(Elevator elevator);
}
