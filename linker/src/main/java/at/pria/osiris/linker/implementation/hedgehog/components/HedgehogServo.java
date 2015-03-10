package at.pria.osiris.linker.implementation.hedgehog.components;

import org.andrix.low.NotConnectedException;
import org.andrix.motors.Servo;

/**
 * A class to control the hedgehog servo
 *
 * @author Ari Ayvazyan
 * @version 2015-01-26
 */
public class HedgehogServo extends Servo implements at.pria.osiris.linker.controllers.components.systemDependent.Servo {
    private final long timePerDegreeInMilli;
    private int maximumAngle;

    public HedgehogServo(int port, int maximumAngle, long timePerDegreeInMilli, int initialPosition) throws NotConnectedException {
        super(port);
        if (maximumAngle == 0) throw new RuntimeException("the maximum Angle may not be 0");
        this.timePerDegreeInMilli = timePerDegreeInMilli;
        this.maximumAngle = maximumAngle;
        //Set the servo to its initial position before the startup to prevent unintended movements
        this.setPosition(initialPosition);
        this.on();
    }

    /**
     * Moves to a certain angle in degrees
     *
     * @param angle
     */
    @Override
    public void moveToExactPosition(int angle) {
        if (angle < 0 || angle > maximumAngle)
            throw new RuntimeException("The maximum angle: " + angle + " of this servo has been excesses: " + angle);
        try {
            //The maximum Value for Hedgehog Servos is 255
            super.setPosition((angle / maximumAngle) * 255);
        } catch (NotConnectedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getMaximumAngle() {
        return maximumAngle;
    }

    @Override
    public long getTimePerDegreeInMilli() {
        return timePerDegreeInMilli;
    }
}
