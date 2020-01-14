package at.fhhagenberg.sqelevator.services;

import at.fhhagenberg.sqelevator.tests.MockElevator;
import sqelevator.IElevator;

public class MockElevatorServiceFactory implements IElevatorServiceFactory {

    @Override
    public IElevator getElevatorService(){
            return new MockElevator(5,7,5,10);
    }
}
