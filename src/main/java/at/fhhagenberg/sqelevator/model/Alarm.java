package at.fhhagenberg.sqelevator.model;

public class Alarm {

    private boolean isError;
    private String message;

    public Alarm(String message, boolean isError) {
        this.isError = isError;
        this.message = message;
    }

    public boolean isError() {
        return isError;
    }

    public String getMessage() {
        return message;
    }

}
