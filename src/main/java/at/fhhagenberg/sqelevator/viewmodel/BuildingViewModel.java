package at.fhhagenberg.sqelevator.viewmodel;

import at.fhhagenberg.sqelevator.model.Building;
import at.fhhagenberg.sqelevator.model.Elevator;
import at.fhhagenberg.sqelevator.model.Floor;
import at.fhhagenberg.sqelevator.model.observers.BuildingChangeObserver;
import at.fhhagenberg.sqelevator.model.observers.ElevatorChangeObserver;
import at.fhhagenberg.sqelevator.model.observers.FloorChangeObserver;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.ObservableMap;

public class BuildingViewModel implements BuildingChangeObserver, FloorChangeObserver, ElevatorChangeObserver {
    private SimpleMapProperty<Integer, ElevatorViewModel> elevators = new SimpleMapProperty<>();
    private SimpleMapProperty<Integer, FloorViewModel> floors = new SimpleMapProperty<>();

    private SimpleBooleanProperty automaticMode = new SimpleBooleanProperty();

    public ObservableMap<Integer, ElevatorViewModel> getElevators() {
        return elevators.get();
    }

    public SimpleMapProperty<Integer, ElevatorViewModel> elevatorsProperty() {
        return elevators;
    }

    public ObservableMap<Integer, FloorViewModel> getFloors() {
        return floors.get();
    }

    public SimpleMapProperty<Integer, FloorViewModel> floorsProperty() {
        return floors;
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

    public void toggleAutomaticMode() {
        automaticMode.set(!automaticMode.get());
    }

    @Override
    public void update(Building building) {
        elevators.clear();

        for(int i=0;i<building.getNumElevators();i++){
            elevators.put(i, new ElevatorViewModel());
        }

        floors.clear();

        for(int i=0;i<building.getNumFloors();i++){
            floors.put(i, new FloorViewModel());
        }
    }

    @Override
    public void update(Elevator elevator) {
        if(elevators.size() == 0){
            return; //building not yet initialized
        }

        elevators.get(elevator.getId()).setAcceleration(elevator.getAcceleration());
        //TODO: update all properties
    }

    @Override
    public void update(Floor floor) {
        if(floors.size() == 0){
            return; //building not yet initialized
        }

        floors.get(floor.getId()).setDownButtonActive(floor.isDownButtonActive());
        floors.get(floor.getId()).setUpButtonActive(floor.isUpButtonActive());
    }
}
