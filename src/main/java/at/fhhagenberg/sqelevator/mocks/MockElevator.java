package at.fhhagenberg.sqelevator.mocks;

import at.fhhagenberg.sqelevator.IElevator;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;


/** @noinspection RedundantThrows, WeakerAccess*/
public class MockElevator implements IElevator {

    private static final int FLOOR_HEIGHT = 7;
    private static final int CLOCK_TICK = 42;

    private List<Elevator> elevators;
    private List<Floor> floors;

    public MockElevator(int numElevators, int numFloors)
    {
        elevators = new ArrayList<>(numElevators);
        floors = new ArrayList<>(numFloors);

        for (int i = 0; i < numElevators; i++)
        {
            elevators.add(new Elevator(numFloors));
        }

        for (int i = 0; i < numFloors; i++)
        {
            floors.add(new Floor());
        }

        setupServicedFloors(numElevators, numFloors);
    }

    private void setupServicedFloors(int numElevators, int numFloors) {
        for (int elevator = 0; elevator < numElevators; elevator++)
        {
            for (int floor = 0; floor < numFloors; floor++)
            {
                elevators.get(elevator).setFloorButtonActive(floor,false);

                if(elevator % 2 == 0)
                {
                    if(floor % 2 == 0)
                    {
                        elevators.get(elevator).setServicesFloor(floor, true);
                    }
                    else
                    {
                        elevators.get(elevator).setServicesFloor(floor, false);
                    }
                }
            }
        }
    }

    @Override
    public int getCommittedDirection(int elevatorNumber) throws RemoteException
    {
        return elevators.get(elevatorNumber).getDirection();
    }

    @Override
    public int getElevatorAccel(int elevatorNumber) throws RemoteException
    {
        return elevators.get(elevatorNumber).getAcceleration();
    }

    @Override
    public boolean getElevatorButton(int elevatorNumber, int floor) throws RemoteException
    {
        return elevators.get(elevatorNumber).isFloorButtonActive(floor);
    }

    @Override
    public int getElevatorDoorStatus(int elevatorNumber) throws RemoteException
    {
        return elevators.get(elevatorNumber).getDoorStatus();
    }

    @Override
    public int getElevatorFloor(int elevatorNumber) throws RemoteException
    {
        return elevators.get(elevatorNumber).getCurrentFloor();
    }

    @Override
    public int getElevatorNum() throws RemoteException
    {
        return elevators.size();
    }

    @Override
    public int getElevatorPosition(int elevatorNumber) throws RemoteException
    {
        return elevators.get(elevatorNumber).getCurrentFloor() * FLOOR_HEIGHT;
    }

    @Override
    public int getElevatorSpeed(int elevatorNumber) throws RemoteException
    {
        return elevators.get(elevatorNumber).getSpeed();
    }

    @Override
    public int getElevatorWeight(int elevatorNumber) throws RemoteException
    {
        return elevators.get(elevatorNumber).getWeight();
    }

    @Override
    public int getElevatorCapacity(int elevatorNumber) throws RemoteException
    {
        return elevators.get(elevatorNumber).getCapacity();
    }

    @Override
    public boolean getFloorButtonDown(int floor) throws RemoteException
    {
        return floors.get(floor).isDownButtonActive();
    }

    @Override
    public boolean getFloorButtonUp(int floor) throws RemoteException
    {
        return floors.get(floor).isUpButtonActive();
    }

    @Override
    public int getFloorHeight() throws RemoteException
    {
        return FLOOR_HEIGHT;
    }

    @Override
    public int getFloorNum() throws RemoteException
    {
        return floors.size();
    }

    @Override
    public boolean getServicesFloors(int elevatorNumber, int floor) throws RemoteException {

        return elevators.get(elevatorNumber).getServicesFloors(floor);
    }

    @Override
    public int getTarget(int elevatorNumber) throws RemoteException
    {
        return elevators.get(elevatorNumber).getTargetFloor();
    }

    @Override
    public void setCommittedDirection(int elevatorNumber, int direction) throws RemoteException
    {
        elevators.get(elevatorNumber).setDirection(direction);
    }

    @Override
    public void setServicesFloors(int elevatorNumber, int floor, boolean service) throws RemoteException
    {
        elevators.get(elevatorNumber).setServicesFloor(floor, service);
    }

    @Override
    public void setTarget(int elevatorNumber, int target) throws RemoteException
    {
        elevators.get(elevatorNumber).setTargetFloor(target);

        elevators.get(elevatorNumber).setCurrentFloor(target);
    }

    @Override
    public long getClockTick() throws RemoteException
    {
        return CLOCK_TICK;
    }

    private class Floor
    {
        private boolean upButtonActive = false;
        private boolean downButtonActive = false;

        public boolean isUpButtonActive()
        {
            return upButtonActive;
        }

        public void setUpButtonActive(boolean upButtonActive)
        {
            this.upButtonActive = upButtonActive;
        }

        public boolean isDownButtonActive()
        {
            return downButtonActive;
        }

        public void setDownButtonActive(boolean downButtonActive)
        {
            this.downButtonActive = downButtonActive;
        }
    }

    private class Elevator
    {
        private static final int ELEVATOR_CAPACITY = 10;

        private int direction = ELEVATOR_DIRECTION_UNCOMMITTED;
        private int acceleration = 0;
        private int doorStatus = ELEVATOR_DOORS_CLOSED;
        private int currentFloor = 0;
        private int targetFloor = 0;
        private int speed = 0;
        private int weight = 0;

        private List<Boolean> servicedFloors;
        private List<Boolean> floorButtons;

        public Elevator(int numFloors) {
            servicedFloors = new ArrayList<>(numFloors);
            floorButtons = new ArrayList<>(numFloors);

            for (int i = 0; i < numFloors; i++)
            {
                servicedFloors.add(i, true);
                floorButtons.add(i, false);
            }
        }

        public int getTargetFloor()
        {
            return targetFloor;
        }

        public void setTargetFloor(int targetFloor)
        {
            this.targetFloor = targetFloor;
        }


        public boolean isFloorButtonActive(int floor) {
            return floorButtons.get(floor);
        }

        public void setFloorButtonActive(int floor, boolean active)
        {
            floorButtons.set(floor, active);
        }

        public boolean getServicesFloors(int floor) {
            return servicedFloors.get(floor);
        }

        public void setServicesFloor(int floor, boolean service)
        {
            servicedFloors.set(floor, service);
        }

        public int getDirection()
        {
            return direction;
        }

        public void setDirection(int direction)
        {
            this.direction = direction;
        }

        public int getAcceleration()
        {
            return acceleration;
        }

        public void setAcceleration(int acceleration)
        {
            this.acceleration = acceleration;
        }

        public int getDoorStatus()
        {
            return doorStatus;
        }

        public void setDoorStatus(int doorStatus)
        {
            this.doorStatus = doorStatus;
        }

        public int getCurrentFloor()
        {
            return currentFloor;
        }

        public void setCurrentFloor(int currentFloor)
        {
            this.currentFloor = currentFloor;
        }

        public int getSpeed()
        {
            return speed;
        }

        public void setSpeed(int speed)
        {
            this.speed = speed;
        }

        public int getWeight()
        {
            return weight;
        }

        public void setWeight(int weight)
        {
            this.weight = weight;
        }

        public int getCapacity()
        {
            return ELEVATOR_CAPACITY;
        }
    }
}
