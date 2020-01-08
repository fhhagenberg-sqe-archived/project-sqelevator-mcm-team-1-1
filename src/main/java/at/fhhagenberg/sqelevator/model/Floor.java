package at.fhhagenberg.sqelevator.model;

import java.util.Objects;

public class Floor {
    private int id = 0;

    private boolean upButtonActive = false;
    private boolean downButtonActive = false;

    public Floor(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isUpButtonActive() {
        return upButtonActive;
    }

    public void setUpButtonActive(boolean upButtonActive) {
        this.upButtonActive = upButtonActive;
    }

    public boolean isDownButtonActive() {
        return downButtonActive;
    }

    public void setDownButtonActive(boolean downButtonActive) {
        this.downButtonActive = downButtonActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Floor floor = (Floor) o;
        return id == floor.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
