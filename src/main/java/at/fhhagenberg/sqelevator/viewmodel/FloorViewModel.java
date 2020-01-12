package at.fhhagenberg.sqelevator.viewmodel;

import at.fhhagenberg.sqelevator.model.Floor;
import at.fhhagenberg.sqelevator.utils.UpdateBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class FloorViewModel {
    private UpdateBooleanProperty upButtonActive = new UpdateBooleanProperty(false);
    private UpdateBooleanProperty downButtonActive = new UpdateBooleanProperty(false);

    public boolean isUpButtonActive() {
        return upButtonActive.get();
    }

    public SimpleBooleanProperty upButtonActiveProperty() {
        return upButtonActive;
    }

    public boolean isDownButtonActive() {
        return downButtonActive.get();
    }

    public SimpleBooleanProperty downButtonActiveProperty() {
        return downButtonActive;
    }

    public void updateWith(Floor floor) {
        upButtonActive.update(floor.isUpButtonActive());
        downButtonActive.update(floor.isDownButtonActive());
    }
}
