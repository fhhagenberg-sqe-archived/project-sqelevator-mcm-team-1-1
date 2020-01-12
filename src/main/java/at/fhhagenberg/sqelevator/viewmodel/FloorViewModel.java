package at.fhhagenberg.sqelevator.viewmodel;

import at.fhhagenberg.sqelevator.model.Floor;
import at.fhhagenberg.sqelevator.model.observers.Observable;
import at.fhhagenberg.sqelevator.model.observers.Observer;
import at.fhhagenberg.sqelevator.utils.UpdateBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class FloorViewModel implements Observer<Floor> {
    private UpdateBooleanProperty upButtonActive = new UpdateBooleanProperty(false);
    private UpdateBooleanProperty downButtonActive = new UpdateBooleanProperty(false);

    public FloorViewModel(Floor floor) {
        floor.addObserver(this);
    }

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

    @Override
    public void update(Observable<Floor> observable) {
        var floor = observable.getValue();

        upButtonActive.update(floor.isUpButtonActive());
        downButtonActive.update(floor.isDownButtonActive());
    }
}
