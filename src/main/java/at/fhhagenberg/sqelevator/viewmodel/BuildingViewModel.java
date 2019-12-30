package at.fhhagenberg.sqelevator.viewmodel;

import javafx.beans.property.SimpleBooleanProperty;

public class BuildingViewModel {

    private SimpleBooleanProperty automaticMode = new SimpleBooleanProperty();

    public boolean getAutomaticMode() {
        return automaticMode.get();
    }

    public SimpleBooleanProperty automaticModeProperty() {
        return automaticMode;
    }

    public void setAutomaticMode(boolean automaticMode) {
        this.automaticMode.set(automaticMode);
    }

    public void toggleAutomaticMode(){
        automaticMode.set(!automaticMode.get());
    }
}
