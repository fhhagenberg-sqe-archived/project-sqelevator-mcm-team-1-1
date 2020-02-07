package at.fhhagenberg.sqelevator.model.observers;

public interface Observer<T extends Observable<T>> {
	void update(Observable<T> observable);
}
