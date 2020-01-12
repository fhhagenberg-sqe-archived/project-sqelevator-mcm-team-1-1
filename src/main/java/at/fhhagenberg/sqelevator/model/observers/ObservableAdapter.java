package at.fhhagenberg.sqelevator.model.observers;

import java.util.ArrayList;
import java.util.List;

public abstract class ObservableAdapter<T extends Observable<T>> implements Observable<T> {

    private List<Observer<T>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    public void notifyListeners(){
        for(Observer<T> observer:observers){
            observer.update(this);
        }
    }
}
