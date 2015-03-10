package at.pria.osiris.linker.implementation.hedgehog.components;

import at.pria.osiris.linker.controllers.components.systemDependent.Servo;
import org.andrix.low.NotConnectedException;

/**
 * @author Ari Ayvazyan
 * @version 10.03.2015
 */
public class HedgehogDoubleServo implements Servo {
    private final long timePerDegreeInMilli;
    private HedgehogServo andrixServo1;
    private HedgehogServo andrixServo2;
    private int maximumAngle;

    /**
     * servo1 is used as the reference servo
     * servo2 is modified to move together with servo1 in one motion
     * if servo 2 would be null, servo 1 will perform as a normal Servo would do.
     *
     * @param port1                the port for servo1
     * @param port2                the port for servo2
     * @param maximumAngle         the maximum angle for both
     * @param timePerDegreeInMilli the time this servos take to move by one degree
     * @throws NotConnectedException
     */
    public HedgehogDoubleServo(int port1, int port2, int maximumAngle, long timePerDegreeInMilli, int initialPosition) throws NotConnectedException {
        //The servo1 is used as a reference for its position and is used as a usual servo
        this.andrixServo1 = new HedgehogServo(port1, maximumAngle, timePerDegreeInMilli, initialPosition);
        this.andrixServo2 = new HedgehogServo(port2, maximumAngle, timePerDegreeInMilli, maximumAngle - initialPosition);
        this.maximumAngle = maximumAngle;
        this.timePerDegreeInMilli = timePerDegreeInMilli;
        this.andrixServo1.on();
        this.andrixServo2.on();
    }

    /**
     * Getter for property 'andrixServo1'.
     *
     * @return Value for property 'andrixServo1'.
     */
    public org.andrix.motors.Servo getAndrixServo1() {
        return andrixServo1;
    }

    /**
     * Getter for property 'andrixServo2'.
     *
     * @return Value for property 'andrixServo2'.
     */
    public org.andrix.motors.Servo getAndrixServo2() {
        return andrixServo2;
    }

    /**
     * Moves to a certain angle in degrees
     *
     * @param angle
     */
    @Override
    public void moveToAngle(int angle) {
        andrixServo1.moveToAngle(angle);
        andrixServo2.moveToAngle(andrixServo2.getMaximumAngle() - angle);
    }

    /**
     * @return Returns the position of Servo1
     */
    @Override
    public int getPositionInDegrees() {
        //The servo1 is used as a reference for its position
        return andrixServo1.getPositionInDegrees();
    }

    /**
     * @return returns the Maximum angle
     */
    @Override
    public int getMaximumAngle() {
        return maximumAngle;
    }

    /**
     * @return returns the time required for the servo to move by one degree
     */
    @Override
    public long getTimePerDegreeInMilli() {
        return timePerDegreeInMilli;
    }
}
