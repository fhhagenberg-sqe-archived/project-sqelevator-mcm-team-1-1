package at.fhhagenberg.sqelevator.model.observers;

import at.fhhagenberg.sqelevator.model.Alarm;

public interface IAlarmsChangeObserver {
    void newAlarm(Alarm alarm);
}
