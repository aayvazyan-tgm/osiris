/*
 * COPYRIGHT 2013 Christoph Krofitsch
 * Practical Robotics Institute Austria
 */

package org.andrix.motors;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.andrix.AXCP;
import org.andrix.listeners.ComponentListener;
import org.andrix.low.AXCPAccessor;
import org.andrix.low.HardwareController;
import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;
import org.andrix.misc.InvalidPortException;

/**
 * Class Motor<br>
 * This class represents a Motor which is connected to the Controller.
 * 
 * @author Christoph Krofitsch
 * @version 21.09.2012
 */
public class Motor {

	// private final static int MIN_PERCENT = 12;
	// private final static int MAX_PERCENT = 90;
	// private double convertPercent(int percent) {
	// if (percent > 0)
	// return MIN_PERCENT + (percent * ((MAX_PERCENT - MIN_PERCENT) / 100f));
	// else if (percent < 0)
	// return -MIN_PERCENT + (percent * ((MAX_PERCENT - MIN_PERCENT) / 100f));
	// else
	// return 0;
	// }

	private static Map<Integer, Motor> motors = new TreeMap<Integer, Motor>();

	public static Motor getInstance(int port) {
		return motors.get(port);
	}

	private static int NUMBER_OF_MOTORS = 0xFF;

	private static Map<PositionListener, int[]> positionListeners = new HashMap<PositionListener, int[]>();
	private static Map<VelocityListener, int[]> velocityListeners = new HashMap<VelocityListener, int[]>();

	private int port;

	private boolean done;

	/**
	 * Creates a new motor which is connected to the specified port.
	 * 
	 * @param port
	 *            the port the motor is connected to.
	 * @throws InvalidPortException
	 *             if the specified port doesn't exist.
	 */
	public Motor(int port) throws InvalidPortException, NotConnectedException {
		this.port = port;
		done = true;
		if (port >= getNumberOfMotors() || port < 0)
			throw new InvalidPortException("Valid motor port numbers: 0 - " + (NUMBER_OF_MOTORS - 1));
		motors.put(port, this);
	}

	/**
	 * Returns the port specified in creation of this motor
	 * 
	 * @return the port this motor is connected to.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 
	 * Set motor power in percent from -255 (full backward) to 255 (full
	 * forward). It's recommended you use a Back-EMF based function instead. A
	 * Back-EMF function is one that reads the voltage return from the motor so
	 * you can better determine the speed and increase the power if neccesary.
	 * 
	 * @param percent
	 *            the percentage of power to drive the motor
	 * 
	 * @see #forward
	 * @see #backward
	 * @see #moveAtVelocity
	 */
	public synchronized void moveAtPower(int power) throws NotConnectedException {
		try {
			AXCP.command(AXCP.MOTOR_POWER_ACTION, port, Boolean.valueOf(power > 0), Math.abs(power));
		} catch (RequestTimeoutException e) {
		}
		done = true;
		for (ComponentListener listener : ComponentListener._l_component)
			listener.motorPower(port, power);
	}

	/**
	 * Moves the motor at a velocity in hundredths of revolutions per second.
	 * This is a Back-EMF function.
	 * 
	 * @param velocity
	 *            The velocity to move in hundredths of revolutions per second.
	 *            -255 as the minimum and 255 as the maximum of one revolution
	 *            per second
	 */
	public synchronized void moveAtVelocity(int velocity) throws NotConnectedException {
		try {
			AXCP.command(AXCP.MOTOR_VELOCITY_ACTION, port, Boolean.valueOf(velocity > 0), Math.abs(velocity));
		} catch (RequestTimeoutException e) {
		}
		done = true;
		for (ComponentListener listener : ComponentListener._l_component)
			listener.motorVelocity(port, velocity);
	}

	public synchronized void moveAbsolutePositionPower(int power, int goalPos) throws NotConnectedException {
		try {
			AXCP.command(AXCP.MOTOR_POWER_ABSOLUTE_POSITION_ACTION, port, Math.abs(power), goalPos);
		} catch (RequestTimeoutException e) {
		}
		done = false;
		for (ComponentListener listener : ComponentListener._l_component)
			listener.motorPowerPosition(port, power, true, goalPos);
	}

	public synchronized void moveRelativePositionPower(int power, int deltaPos) throws NotConnectedException {
		try {
			AXCP.command(AXCP.MOTOR_POWER_RELATIVE_POSITION_ACTION, port, Math.abs(power), deltaPos);
		} catch (RequestTimeoutException e) {
		}
		done = false;
		for (ComponentListener listener : ComponentListener._l_component)
			listener.motorPowerPosition(port, power, false, deltaPos);
	}

	public synchronized void moveAbsolutePositionVelocity(int velocity, int goalPos) throws NotConnectedException {
		try {
			AXCP.command(AXCP.MOTOR_VELOCITY_ABSOLUTE_POSITION_ACTION, port, Math.abs(velocity), goalPos);
		} catch (RequestTimeoutException e) {
		}
		done = false;
		for (ComponentListener listener : ComponentListener._l_component)
			listener.motorVelocityPosition(port, velocity, true, goalPos);
	}

	public synchronized void moveRelativePositionVelocity(int velocity, int deltaPos) throws NotConnectedException {
		try {
			AXCP.command(AXCP.MOTOR_VELOCITY_RELATIVE_POSITION_ACTION, port, Math.abs(velocity), deltaPos);
		} catch (RequestTimeoutException e) {
		}
		done = false;
		for (ComponentListener listener : ComponentListener._l_component)
			listener.motorVelocityPosition(port, velocity, false, deltaPos);
	}

	public synchronized void positionReached() {
		this.notify();
		done = true;
		for (ComponentListener listener : ComponentListener._l_component)
			listener.motorPositionReached(port);
	}

	/**
	 * Turns the motor on full in the forward direction. Equivalent to {@link
	 * motor(100)}
	 */
	public void forward() throws NotConnectedException, RequestTimeoutException {
		moveAtPower(255);
	}

	/**
	 * Turns the motor on full in the backward direction. Equivalent to {@link
	 * motor(-100)}.
	 */
	public void backward() throws NotConnectedException, RequestTimeoutException {
		moveAtPower(-255);
	}

	/**
	 * Blocks the program until the motor is done moving to it's goal.
	 */
	public synchronized void blockMotorDone() {
		if (!done) {
			try {
				this.wait();
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Checks if the motor is moving toward a goal.
	 * 
	 * @return true if the motor is moving toward a goal, false if it isn't
	 */
	public synchronized boolean isDone() {
		return done;
	}

	/**
	 * Cut power to this motor. The motor doesn't block and can move freely.
	 * 
	 * @see #freeze()
	 */
	public void off() throws NotConnectedException {
		try {
			AXCP.command(AXCP.MOTOR_OFF_ACTION, port);
		} catch (RequestTimeoutException e) {
		}
		done = true;
		for (ComponentListener listener : ComponentListener._l_component)
			listener.motorOff(port);
	}

	/**
	 * Actively attempts to keep the motor at the current position (Warning:
	 * continues to draw current) Imagine the motors are your legs,
	 * <code>freeze()</code> is you attempting to stand in place and resist
	 * someone pushing you.
	 * 
	 * @see #off()
	 * 
	 */
	public synchronized void freeze() throws NotConnectedException {
		try {
			AXCP.command(AXCP.MOTOR_FREEZE_ACTION, port);
		} catch (RequestTimeoutException e) {
		}
		done = true;
		for (ComponentListener listener : ComponentListener._l_component)
			listener.motorFreeze(port);
	}

	public synchronized void brakeMotor(int power) throws NotConnectedException {
		try {
			AXCP.command(AXCP.MOTOR_BRAKE_ACTION, port, Math.abs(power));
		} catch (RequestTimeoutException e) {
		}
		done = true;
		for (ComponentListener listener : ComponentListener._l_component)
			listener.motorBrake(port, power);
	}

	public int getPosition() throws NotConnectedException, RequestTimeoutException {
		int value = (Integer) AXCP.command(AXCP.MOTOR_POSITION_REQUEST, port);
		for (ComponentListener listener : ComponentListener._l_component)
			listener.motorGetPosition(port, value);
		return value;
	}

	public int getVelocity() throws NotConnectedException, RequestTimeoutException {
		int value = (Integer) AXCP.command(AXCP.MOTOR_VELOCITY_REQUEST, port);
		for (ComponentListener listener : ComponentListener._l_component)
			listener.motorGetVelocity(port, value);
		return value;
	}

	/**
	 * Clears the position counter of the motor. The position is counted in
	 * ticks.
	 */
	public void clearPositionCounter() throws NotConnectedException {
		try {
			AXCP.command(AXCP.MOTOR_CLEAR_POSITION_ACTION, port);
		} catch (RequestTimeoutException e) {
		}
		for (ComponentListener listener : ComponentListener._l_component)
			listener.motorClearPosition(port);
	}
	
	
	public void registerPositionListener(PositionListener listener) throws NotConnectedException {
		registerCompoundPositionListener(listener, port);
	}

	public void unregisterPositionListener(PositionListener listener) throws NotConnectedException {
		unregisterCompoundPositionListener(listener);
	}

	public static void registerCompoundPositionListener(PositionListener listener, int... ports) throws NotConnectedException {
		if (!positionListeners.containsKey(listener)) {
			positionListeners.put(listener, ports);
			try {
				AXCP.command(AXCP.MOTOR_POSITION_SUBSCRIPTION, positionListeners, getNumberOfMotors());
			} catch (RequestTimeoutException e) {
			}
		}
	}

	public static void unregisterCompoundPositionListener(PositionListener listener) throws NotConnectedException {
		if (positionListeners.containsKey(listener)) {
			positionListeners.remove(listener);
			try {
				AXCP.command(AXCP.MOTOR_POSITION_SUBSCRIPTION, positionListeners, getNumberOfMotors());
			} catch (RequestTimeoutException e) {
			}
		}
	}

	

	public void registerVelocityListener(VelocityListener listener) throws NotConnectedException {
		registerCompoundVelocityListener(listener, port);
	}

	public void unregisterVelocityListener(VelocityListener listener) throws NotConnectedException {
		unregisterCompoundVelocityListener(listener);
	}

	public static void registerCompoundVelocityListener(VelocityListener listener, int... ports) throws NotConnectedException {
		if (!velocityListeners.containsKey(listener)) {
			velocityListeners.put(listener, ports);
			try {
				AXCP.command(AXCP.MOTOR_VELOCITY_SUBSCRIPTION, velocityListeners, getNumberOfMotors());
			} catch (RequestTimeoutException e) {
			}
		}
	}

	public static void unregisterCompoundVelocityListener(VelocityListener listener) throws NotConnectedException {
		if (velocityListeners.containsKey(listener)) {
			velocityListeners.remove(listener);
			try {
				AXCP.command(AXCP.MOTOR_VELOCITY_SUBSCRIPTION, velocityListeners, getNumberOfMotors());
			} catch (RequestTimeoutException e) {
			}
		}
	}
	
	private static int getNumberOfMotors() throws NotConnectedException {
		if (NUMBER_OF_MOTORS == 0xFF) {
			HardwareController hwc = AXCPAccessor.getInstance().getConnectedHWType();
			if (hwc == null)
				throw new NotConnectedException();
			NUMBER_OF_MOTORS = AXCP.hwTypeToMotors.get(hwc.type);
		}
		return NUMBER_OF_MOTORS;
	}
	
	public static interface PositionListener {
		public void update(int... position);
	}

	public static interface VelocityListener {
		public void update(int... velocity);
	}
}
