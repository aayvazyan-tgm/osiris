package at.pria.osiris.linker.controllers.hedgehog;

import org.andrix.low.NotConnectedException;
import org.andrix.motors.Motor;

/**
 * A class to control the Hedgehog-Motor
 *
 * @author Helmuth Brunner
 * @version 2015-01-26
 */
public class HedgehogMotor implements at.pria.osiris.linker.controllers.components.systemDependent.Motor {

	private Motor actualMotor;

	public HedgehogMotor(int port) throws NotConnectedException {
		actualMotor= new Motor(port);
	}

	/**
	 * @see at.pria.osiris.linker.controllers.components.systemDependent.Motor#moveAtPower(int)
	 */
	public void moveAtPower(int power) throws NotConnectedException {
		actualMotor.moveAtPower(power);
	}

}
