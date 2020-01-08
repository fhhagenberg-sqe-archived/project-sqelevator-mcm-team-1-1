package at.fhhagenberg.sqelevator.model.observers;

import at.fhhagenberg.sqelevator.model.Elevator;

public interface IElevatorChangeObserver {
    void update(Elevator elevator);
}
