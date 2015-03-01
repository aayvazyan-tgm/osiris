package at.pria.osiris.linker.controllers.components.Axes;


/**
 * @author Ari Michael Ayvazyan
 * @version 21.02.2015
 */
public abstract class Axis {

    private String name;

    public Axis(String axisName) {
        this.name = axisName;
    }

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {
        return name;
    }

    public String getName(int axisID) {
        return name;
    }

    /**
     * Returns the value of the Sensor where -1 is a undefined state
     *
     * @return the Sensor value
     */
    public abstract int getSensorValue();

    /**
     * Moves to the desired angle.
     *
     * @param angle the desired angle in degrees
     */
    public abstract void moveToAngle(int angle);

    /**
     * moves the Axis at the desired power
     *
     * @param power the power
     */
    public abstract void moveAtPower(int power);

}
