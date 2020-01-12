package at.fhhagenberg.sqelevator.model.observers;

public interface Observable<T extends Observable<T>> {
    void addObserver(Observer<T> observer);
    void removeObserver(Observer<T> observer);

    T getValue();
}
