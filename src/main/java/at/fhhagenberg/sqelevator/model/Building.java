package at.fhhagenberg.sqelevator.model;

public class Building {
    private int numElevators;
    private int numFloors;

    public Building(int numElevators, int numFloors) {
        this.numElevators = numElevators;
        this.numFloors = numFloors;
    }

    public int getNumElevators() {
        return numElevators;
    }

    public int getNumFloors() {
        return numFloors;
    }
}
