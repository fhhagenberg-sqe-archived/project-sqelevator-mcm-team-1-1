package at.fhhagenberg.sqelevator.model;

import sqelevator.IElevator;

import java.util.ArrayList;
import java.util.List;

public class Building {
	private List<Elevator> elevators;
	private List<Floor> floors;

	public Building(int numElevators, int numFloors, IElevator elevatorService) {
		elevators = new ArrayList<>(numElevators);
		floors = new ArrayList<>(numFloors);

		for (int i = 0; i < numElevators; i++) {
			var elevator = new Elevator(i, numFloors, elevatorService);
			elevators.add(elevator);
		}

		for (int i = 0; i < numFloors; i++) {
			var floor = new Floor(i, elevatorService);
			floors.add(floor);
		}
	}

	public int getNumElevators() {
		return elevators.size();
	}

	public int getNumFloors() {
		return floors.size();
	}

	public List<Elevator> getElevators() {
		return elevators;
	}

	public List<Floor> getFloors() {
		return floors;
	}

	public Elevator getElevator(int id) {
		return elevators.get(id);
	}

	public Floor getFloor(int id) {
		return floors.get(id);
	}
}
