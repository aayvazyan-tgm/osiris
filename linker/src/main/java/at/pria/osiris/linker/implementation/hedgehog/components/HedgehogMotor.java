package at.pria.osiris.linker.implementation.hedgehog.components;

import org.andrix.low.NotConnectedException;
import org.andrix.motors.Motor;

/**
 * A class to control the Hedgehog-Motor
 *
 * @author Ari Ayvazyan
 * @version 2015-01-26
 */
public class HedgehogMotor extends Motor implements at.pria.osiris.linker.controllers.components.systemDependent.Motor {

    public HedgehogMotor(int port) throws NotConnectedException {
        super(port);
    }

    /**
     * @see at.pria.osiris.linker.controllers.components.systemDependent.Motor#moveAtPower(int)
     */
    public void moveAtPower(int power) {
        try {
            super.moveAtPower(power);
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }
}
