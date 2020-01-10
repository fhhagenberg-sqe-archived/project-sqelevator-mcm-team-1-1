package at.fhhagenberg.sqelevator.viewmodel;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ElevatorViewModel {
    private SimpleBooleanProperty automaticMode = new SimpleBooleanProperty(false);

    private SimpleIntegerProperty acceleration = new SimpleIntegerProperty(0);
    //TODO: other properties


    public boolean isAutomaticMode() {
        return automaticMode.get();
    }

    public SimpleBooleanProperty automaticModeProperty() {
        return automaticMode;
    }

    public void setAutomaticMode(boolean automaticMode) {
        this.automaticMode.set(automaticMode);
    }

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
