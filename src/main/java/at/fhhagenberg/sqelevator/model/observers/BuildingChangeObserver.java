package at.fhhagenberg.sqelevator.model.observers;

import at.fhhagenberg.sqelevator.model.Building;

public interface BuildingChangeObserver {
    void update(Building building);
}
