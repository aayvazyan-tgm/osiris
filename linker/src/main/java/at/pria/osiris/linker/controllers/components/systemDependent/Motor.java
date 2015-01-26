package at.pria.osiris.linker.controllers.components.systemDependent;

import org.andrix.low.NotConnectedException;

public interface Motor {

	public void moveAtPower(int power) throws NotConnectedException;

}
