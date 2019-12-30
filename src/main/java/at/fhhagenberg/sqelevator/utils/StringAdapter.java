package at.fhhagenberg.sqelevator.utils;

import javafx.util.StringConverter;

public class StringAdapter<T> extends StringConverter<T> {
    @Override
    public String toString(T t) {
        return null;
    }

    @Override
    public T fromString(String s) {
        throw null;
    }
}
