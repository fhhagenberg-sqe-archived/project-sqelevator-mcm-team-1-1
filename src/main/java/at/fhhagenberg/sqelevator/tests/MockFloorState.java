package at.fhhagenberg.sqelevator.tests;

public class MockFloorState {
    private boolean upButtonActive = false;
    private boolean downButtonActive = false;

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
}
