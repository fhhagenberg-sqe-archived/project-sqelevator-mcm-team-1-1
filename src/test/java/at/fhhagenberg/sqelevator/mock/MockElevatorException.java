package at.fhhagenberg.sqelevator.mock;

import java.rmi.RemoteException;

public class MockElevatorException extends RemoteException {
    public MockElevatorException(String msg) {
        super(msg);
    }
}
