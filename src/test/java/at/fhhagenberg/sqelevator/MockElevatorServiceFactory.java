package at.fhhagenberg.sqelevator;

import at.fhhagenberg.sqelevator.mock.MockElevator;
import at.fhhagenberg.sqelevator.services.IElevatorServiceFactory;
import sqelevator.IElevator;

public class MockElevatorServiceFactory implements IElevatorServiceFactory {

    @Override
    public IElevator getElevatorService(){
            return new MockElevator(5,7,5,10);
    }
}
