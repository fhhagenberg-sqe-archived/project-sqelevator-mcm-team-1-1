package at.fhhagenberg.sqelevator.services;

import sqelevator.IElevator;

import java.rmi.Naming;

public class RMIElevatorServiceFactory implements IElevatorServiceFactory{

    @Override
    public IElevator getElevatorService() throws Exception {
            return (IElevator) Naming.lookup("rmi://localhost/ElevatorSim");
    }
}
