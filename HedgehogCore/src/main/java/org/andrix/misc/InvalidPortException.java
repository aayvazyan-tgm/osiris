/*
 * COPYRIGHT 2013 Christoph Krofitsch
 * Practical Robotics Institute Austria
 */

package org.andrix.misc;

import org.andrix.motors.Motor;
import org.andrix.motors.Servo;
import org.andrix.sensors.Analog;
import org.andrix.sensors.Digital;

/**
 * Class InvalidPortException<br>
 * {@link RuntimeException} which will be thrown when a low component {@link Analog}, {@link Digital}, {@link Motor} or
 * {@link Servo} was initialized with a port which doesn't exist on the Arduino. This Exception won't be handled by
 * AndriX and will be thrown to the caller.
 * 
 * @author Christoph Krofitsch
 * @version 21.09.2012
 */
public class InvalidPortException extends RuntimeException {

	private static final long serialVersionUID = -5079014776247385432L;

	public InvalidPortException() {
		super();
	}

	public InvalidPortException(String detailMessage) {
		super(detailMessage);
	}
}
