package at.fhhagenberg.sqelevator.mock;

import java.rmi.RemoteException;

@SuppressWarnings("serial")
public class MockElevatorException extends RemoteException {
    public MockElevatorException(String msg) {
        super(msg);
    }
}
