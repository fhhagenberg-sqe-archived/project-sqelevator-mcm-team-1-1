package at.fhhagenberg.sqelevator.utils;

import javafx.beans.property.SimpleIntegerProperty;

public class UpdateIntegerProperty extends SimpleIntegerProperty {
    public UpdateIntegerProperty() {
    }

    public UpdateIntegerProperty(int i) {
        super(i);
    }

    public void update(int value){
        if(value != this.get()){
            this.set(value);
        }
    }
}
