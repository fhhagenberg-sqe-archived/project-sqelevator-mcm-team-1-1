package at.fhhagenberg.sqelevator.model.autocontroller;

import at.fhhagenberg.sqelevator.model.IElevatorController;

public interface IControlAlgorithm {
	void setElevatorController(IElevatorController elevatorController);

	void start();

	void stop();
}
