package at.pria.osiris.linker.implementation.hedgehog.components;

import org.andrix.low.NotConnectedException;
import org.andrix.motors.Servo;

/**
 * A class to control the hedgehog servo
 *
 * @author Helmuth Brunner
 * @version 2015-01-26
 */
public class HedgehogServo extends Servo implements at.pria.osiris.linker.controllers.components.systemDependent.Servo {


    public HedgehogServo(int port) throws NotConnectedException {
        super(port);
    }

    /**
     * @see at.pria.osiris.linker.controllers.components.systemDependent.Servo#moveToAngle(int)
     */
    public void moveToAngle(int angle) {
        try {
            super.setPosition(angle);
        } catch (NotConnectedException e) {
            throw new RuntimeException(e);
        }
    }

}
