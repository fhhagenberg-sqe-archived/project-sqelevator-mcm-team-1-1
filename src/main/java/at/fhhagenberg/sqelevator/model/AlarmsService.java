package at.fhhagenberg.sqelevator.model;

import at.fhhagenberg.sqelevator.model.observers.ObservableAdapter;
import at.fhhagenberg.sqelevator.model.observers.Observer;

import java.util.ArrayList;
import java.util.List;

public class AlarmsService extends ObservableAdapter<AlarmsService> {
    private static AlarmsService instance = null;

    private List<Alarm> alarms;

    private AlarmsService() {
        alarms = new ArrayList<>();
    }

    public static AlarmsService getInstance() {
        if (instance == null)
            instance = new AlarmsService();

        return instance;
    }

    @Override
    public void addObserver(Observer<AlarmsService> observer) {
        super.addObserver(observer);

        observer.update(this);  //send all existing alarms as update to new observers
    }

    public void addAlert(String message) {
        addAlert(message, true);
    }

    public void addAlert(String message, boolean isError) {
        alarms.add(new Alarm(message, isError));

        notifyListeners();
    }

    public void removeAlert(){
        //TODO:
    }

    @Override
    public AlarmsService getValue() {
        return this;
    }

    public List<Alarm> getAlarms() {
        return alarms;
    }
}
