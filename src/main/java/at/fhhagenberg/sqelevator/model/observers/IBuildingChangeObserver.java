package at.fhhagenberg.sqelevator.model.observers;

import at.fhhagenberg.sqelevator.model.Building;

public interface IBuildingChangeObserver {
    void update(Building building);
}
