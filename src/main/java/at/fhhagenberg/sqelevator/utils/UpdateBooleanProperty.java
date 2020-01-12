package at.fhhagenberg.sqelevator.utils;

import javafx.beans.property.SimpleBooleanProperty;

public class UpdateBooleanProperty extends SimpleBooleanProperty {
    public UpdateBooleanProperty() {
    }

    public UpdateBooleanProperty(boolean b) {
        super(b);
    }

    public void update(boolean value){
        if(value != this.get()){
            this.set(value);
        }
    }
}
