package at.fhhagenberg.sqelevator.viewmodel;

import javafx.beans.property.SimpleBooleanProperty;

public class FloorViewModel {
    private SimpleBooleanProperty upButtonActive = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty downButtonActive = new SimpleBooleanProperty(false);

    public boolean isUpButtonActive() {
        return upButtonActive.get();
    }

    public SimpleBooleanProperty upButtonActiveProperty() {
        return upButtonActive;
    }

    public void setUpButtonActive(boolean upButtonActive) {
        this.upButtonActive.set(upButtonActive);
    }

    public boolean isDownButtonActive() {
        return downButtonActive.get();
    }

    public SimpleBooleanProperty downButtonActiveProperty() {
        return downButtonActive;
    }

    public void setDownButtonActive(boolean downButtonActive) {
        this.downButtonActive.set(downButtonActive);
    }
}
