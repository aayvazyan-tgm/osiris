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
    private final Object lock = false;
    private int offsetFrom0;
    private int offsetFromTop;
    private int maximumAngle;
    private int positionInDegrees;

    public HedgehogServo(int port,  int maximumAngle, long timePerDegreeInMilli, int initialPosition) throws NotConnectedException {
        super(port);
        if (maximumAngle == 0) throw new RuntimeException("the maximum Angle may not be 0");
        this.timePerDegreeInMilli = timePerDegreeInMilli;
        this.maximumAngle = maximumAngle;
        this.positionInDegrees = initialPosition;
        //Set the servo to its initial position before the startup to prevent unintended movements
        this.setPosition(initialPosition);
        this.on();
    }

    public HedgehogServo(int port, int offsetFrom0, int offsetFromTop, int maximumAngle, long timePerDegreeInMilli, int initialPosition) throws NotConnectedException {
        this(port, maximumAngle, timePerDegreeInMilli, initialPosition);
        this.offsetFrom0 = offsetFrom0;
        this.offsetFromTop = offsetFromTop;
    }

    /**
     * Moves to a certain angle in degrees
     *
     * @param angle
     */
    @Override
    public void moveToAngle(int angle) {
        synchronized (lock) {
            if (angle < 0 || angle > maximumAngle)
                throw new RuntimeException("The maximum angle: " + maximumAngle + " of this servo has been excessed: " + angle);
            try {
                //The maximum Value for Hedgehog Servos is 255
                double maxHardwarePosition = 255d;
                int positionWithoutOffset = (int) (((double) angle / (double) getMaximumAngle()) * maxHardwarePosition);
                double resultingRange = maxHardwarePosition - offsetFrom0 - offsetFromTop;
                double resultingPosWithoutMinOffset = (((double) positionWithoutOffset) / 255d) * resultingRange;
                double resultPos = resultingPosWithoutMinOffset + offsetFrom0;
                super.setPosition((int) resultPos);
                this.positionInDegrees = angle;
            } catch (NotConnectedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int getMaximumAngle() {
        synchronized (lock) {
            return maximumAngle;
        }
    }

    @Override
    public long getTimePerDegreeInMilli() {
        return timePerDegreeInMilli;
    }

    /**
     * @return returns the current position in degrees
     */
    @Override
    public int getPositionInDegrees() {
        //Hedgehog servos work with a maximum value of 255
        return positionInDegrees;
    }
}
