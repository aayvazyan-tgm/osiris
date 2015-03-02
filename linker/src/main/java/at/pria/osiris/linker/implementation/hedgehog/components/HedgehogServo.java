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


    private int maximumAngle;
    private final long timePerDegreeInMilli;

    public HedgehogServo(int port,int maximumAngle, long timePerDegreeInMilli) throws NotConnectedException {
        super(port);
        this.maximumAngle = maximumAngle;
        this.timePerDegreeInMilli = timePerDegreeInMilli;
        this.on();
    }

    /**
     * Moves to a certain position
     *
     * @param position the system dependent position value
     */
    @Override
    public void moveToExactPosition(int position) {
        try {
            super.setPosition(position);
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
