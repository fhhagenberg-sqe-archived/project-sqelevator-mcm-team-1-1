package at.fhhagenberg.sqelevator.model;

import at.fhhagenberg.sqelevator.model.observers.ObservableAdapter;
import sqelevator.IElevator;

import java.rmi.RemoteException;
import java.util.Objects;

public class Floor extends ObservableAdapter<Floor> {
    private int id = 0;
    private final IElevator elevatorService;

    private boolean upButtonActive = false;
    private boolean downButtonActive = false;

    public Floor(int id, IElevator elevatorService) {
        this.id = id;
        this.elevatorService = elevatorService;
    }

    public int getId() {
        return id;
    }

    public boolean isUpButtonActive() {
        return upButtonActive;
    }

    public boolean isDownButtonActive() {
        return downButtonActive;
    }

    public void updateFromService() throws RemoteException {
        var changed = false;

        var newUpButtonActive = elevatorService.getFloorButtonUp(id);
        if(newUpButtonActive != upButtonActive){
            changed = true;
            upButtonActive = newUpButtonActive;
        }
        var newDownButtonActive = elevatorService.getFloorButtonDown(id);
        if(newDownButtonActive != downButtonActive){
            changed = true;
            downButtonActive = newDownButtonActive;
        }

        if(changed){
            notifyListeners();
        }
    }

    @Override
    public Floor getValue() {
        return this;
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
