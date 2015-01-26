package at.pria.osiris.linker.controllers.hedgehog;

import org.andrix.low.NotConnectedException;
import org.andrix.motors.Servo;

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
