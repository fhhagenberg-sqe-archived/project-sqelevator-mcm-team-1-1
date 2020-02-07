package at.fhhagenberg.sqelevator.model;

import at.fhhagenberg.sqelevator.model.observers.ObservableAdapter;
import sqelevator.IElevator;

import java.rmi.RemoteException;

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
		if (newUpButtonActive != upButtonActive) {
			changed = true;
			upButtonActive = newUpButtonActive;
		}
		var newDownButtonActive = elevatorService.getFloorButtonDown(id);
		if (newDownButtonActive != downButtonActive) {
			changed = true;
			downButtonActive = newDownButtonActive;
		}

		if (changed) {
			notifyListeners();
		}
	}

	@Override
	public Floor getValue() {
		return this;
	}
}
