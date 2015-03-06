package at.pria.osiris.osiris.util;

import at.pria.osiris.osiris.controllers.Controller;

import java.util.IdentityHashMap;

/**
 * A class which stores the current controller to the robot.
 *
 * Created by helmuthbrunner on 06/03/15.
 */
public class Storeage {

    private static Storeage INSTANCE;
    private Controller robotController;

    /**
     * Returns a instance from this class
     * @return a object from this class
     */
    public static Storeage getInstance() {
        if(INSTANCE==null)
            INSTANCE= new Storeage();
        return INSTANCE;
    }

    // private constructor
    private Storeage() {
    }

    /**
     * Set the controller
     * @param robotController
     */
    public void setRobotController(Controller robotController) {
        this.robotController= robotController;
    }

    /**
     * Get the controller
     * @return
     */
    public Controller getRobotController() {
        return this.robotController;
    }

}
