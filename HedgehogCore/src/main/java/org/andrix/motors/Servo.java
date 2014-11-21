/*
 * COPYRIGHT 2013 Christoph Krofitsch
 * Practical Robotics Institute Austria
 */

package org.andrix.motors;

import org.andrix.AXCP;
import org.andrix.listeners.ComponentListener;
import org.andrix.low.AXCPAccessor;
import org.andrix.low.HardwareController;
import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;
import org.andrix.misc.InvalidPortException;

/**
 * Class Servo<br>
 * This class represents a Servo which is connected to the Controller.
 * 
 * @author Christoph Krofitsch
 * @version 21.09.2012
 */
public class Servo {
	private static int NUMBER_OF_SERVOS = 0xFF;

	private int port;
	private int position;

	/**
	 * Creates a new servo which is connected to the specified port and turns it
	 * on.
	 * 
	 * @param port
	 *            the port the servo is connected to.
	 * @throws InvalidPortException
	 *             if the specified port doesn't exist.
	 */
	public Servo(int port) throws InvalidPortException, NotConnectedException {
		this.port = port;
		if (port >= getNumberOfServos() || port < 0)
			throw new InvalidPortException("Valid servo port numbers: 0 - " + (NUMBER_OF_SERVOS - 1));
	}

	/**
	 * Returns the port specified in creation of this servo
	 * 
	 * @return the port this servo is connected to.
	 */
	public int getPort() {
		return port;
	}

	public void on() throws NotConnectedException {
		try {
			AXCP.command(AXCP.SERVO_ONOFF_ACTION, port, true);
		} catch (RequestTimeoutException e) {
		}
		for (ComponentListener listener : ComponentListener._l_component)
			listener.servoOnOff(port, true);
	}

	public void off() throws NotConnectedException{
		try {
			AXCP.command(AXCP.SERVO_ONOFF_ACTION, port, false);
		} catch (RequestTimeoutException e) {
		}
		for (ComponentListener listener : ComponentListener._l_component)
			listener.servoOnOff(port, false);
	}

	/**
	 * Move the Servo to a specified position. The position is specified in pwm
	 * duty from 0 to 255.
	 * 
	 * @param pos
	 *            The position value from 0 to 180 to move to
	 */
	public void setPosition(int position) throws NotConnectedException {
		this.position = position;
		try {
			AXCP.command(AXCP.SERVO_DRIVE_ACTION, port, position);
		} catch (RequestTimeoutException e) {
		}
		for (ComponentListener listener : ComponentListener._l_component)
			listener.servoSetPosition(port, position);
	}

	/**
	 * Gets the current (last set) servo's position.
	 * 
	 * @return the current position of the servo from 0 to 255.
	 */
	public int getPosition() {
		return position;
	}

	private static int getNumberOfServos() throws NotConnectedException {
		if (NUMBER_OF_SERVOS == 0xFF) {
			HardwareController hwc = AXCPAccessor.getInstance().getConnectedHWType();
			if (hwc == null)
				throw new NotConnectedException();
			NUMBER_OF_SERVOS = AXCP.hwTypeToServos.get(hwc.type);
		}
		return NUMBER_OF_SERVOS;
	}
}
