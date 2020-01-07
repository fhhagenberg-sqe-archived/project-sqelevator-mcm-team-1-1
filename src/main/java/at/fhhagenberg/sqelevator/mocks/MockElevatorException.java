package at.fhhagenberg.sqelevator.mocks;

import java.rmi.RemoteException;

public class MockElevatorException extends RemoteException {
    public MockElevatorException(String msg) {
        super(msg);
    }
}
