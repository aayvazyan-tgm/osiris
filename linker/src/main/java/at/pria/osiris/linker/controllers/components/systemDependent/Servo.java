package at.pria.osiris.linker.controllers.components.systemDependent;

import org.andrix.low.NotConnectedException;

public interface Servo {

	public void moveToAngle(int angle) throws NotConnectedException;

}
