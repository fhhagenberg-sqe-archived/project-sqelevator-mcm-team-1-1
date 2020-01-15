package at.fhhagenberg.sqelevator.services;

import sqelevator.IElevator;

public interface IElevatorServiceFactory {
    IElevator getElevatorService() throws Exception;
}
