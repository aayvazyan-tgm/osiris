package at.pria.osiris.osiris.controllers;

/**
 * Created by helmuthbrunner on 09/02/15.
 */
public class NoSetupException extends Exception {

    private String message;

    public NoSetupException(String message) {
        super();
        this.message= message;
    }

    public String getMessage() {
        return message;
    }

}
