package at.pria.osiris.linker.controllers.hedgehog;

import org.andrix.low.NotConnectedException;
import org.andrix.motors.Servo;

/**
 * A class to control the hedgehog servo
 *
 * @author Helmuth Brunner
 * @version 2015-01-26
 *
 */
public class HedgehogServo implements at.pria.osiris.linker.controllers.components.systemDependent.Servo {

	private Servo actualServo;

	public HedgehogServo(int port) throws NotConnectedException {
		actualServo= new Servo(port);
	}

	/**
	 * @see at.pria.osiris.linker.controllers.components.systemDependent.Servo#moveToAngle(int)
	 */
	public void moveToAngle(int angle) throws NotConnectedException {
		actualServo.setPosition(angle);
	}

}
