package at.fhhagenberg.sqelevator.viewmodel;

import javafx.beans.property.SimpleIntegerProperty;

public class ElevatorViewModel {
    private SimpleIntegerProperty acceleration = new SimpleIntegerProperty(0);
    //TODO: other properties

    public int getAcceleration() {
        return acceleration.get();
    }

    public SimpleIntegerProperty accelerationProperty() {
        return acceleration;
    }

    public void setAcceleration(int acceleration) {
        this.acceleration.set(acceleration);
    }
}
