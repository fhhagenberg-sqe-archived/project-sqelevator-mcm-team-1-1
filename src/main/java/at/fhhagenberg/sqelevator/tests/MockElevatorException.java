package at.fhhagenberg.sqelevator.tests;

import java.rmi.RemoteException;

public class MockElevatorException extends RemoteException {
    public MockElevatorException(String msg) {
        super(msg);
    }
}
