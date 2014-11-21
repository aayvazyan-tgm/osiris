/*
 * COPYRIGHT 2013 Christoph Krofitsch
 * Practical Robotics Institute Austria
 */

package org.andrix.sensors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.andrix.AXCP;
import org.andrix.listeners.ComponentListener;
import org.andrix.low.AXCPAccessor;
import org.andrix.low.HardwareController;
import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;
import org.andrix.misc.InvalidPortException;

/**
 * Class Digital<br>
 * Represents a physical digital sensor that is connected to the Controller.
 * 
 * @author Christoph Krofitsch
 * @version 02.10.2012
 */
public class Digital {
	private static int NUMBER_OF_DIGITALS = 0xFF;

	private static Map<Listener, int[]> listeners = new HashMap<Listener, int[]>();

	private int port;

	/**
	 * Creates a new digital sensor which is connected to the specified port.
	 * 
	 * @param port
	 *            the port the sensor is connected to.
	 * @throws InvalidPortException
	 *             if the specified port doesn't exist.
	 */
	public Digital(int port) throws InvalidPortException, NotConnectedException {
		this.port = port;
		if (port >= getNumberOfDigitals() || port < 0)
			throw new InvalidPortException("Valid digital sensor port numbers: 0 - " + (NUMBER_OF_DIGITALS - 1));
	}

	/**
	 * Returns the valid port this sensor is connected to.
	 * 
	 * @return the port this sensor is connected to.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * {@inheritDoc}<br>
	 * <br>
	 * This fetches the current value of the sensor from the controller. If the
	 * request fails or the time is running out, false will be returned by
	 * default
	 */
	public Boolean getValue() throws NotConnectedException, RequestTimeoutException {
		Boolean value = (Boolean) AXCP.command(AXCP.DIGITAL_SENSOR_REQUEST, port);
		for (ComponentListener listener : ComponentListener._l_component)
			listener.digitalReading(port, value);
		return value;
	}

	public void setOutputLevel(boolean level) throws NotConnectedException {
		try {
			AXCP.command(AXCP.DIGITAL_OUTPUT_LEVEL_ACTION, port, level);
		} catch (RequestTimeoutException e) {
		}
		for (ComponentListener listener : ComponentListener._l_component)
			listener.digitalOutput(port, level);
	}

	public void registerListener(Listener listener) throws NotConnectedException {
		registerCompoundListener(listener, port);
	}

	public void unregisterListener(Listener listener) throws NotConnectedException {
		unregisterCompoundListener(listener);
	}

	public static void registerCompoundListener(Listener listener, int... ports) throws NotConnectedException {
		if (!listeners.containsKey(listener)) {
			listeners.put(listener, ports);
			try {
				AXCP.command(AXCP.DIGITAL_SENSOR_SUBSCRIPTION, listeners, getNumberOfDigitals());
			} catch (RequestTimeoutException e) {
			}
		}
	}

	public static void unregisterCompoundListener(Listener listener) throws NotConnectedException {
		if (listeners.containsKey(listener)) {
			listeners.remove(listener);
			try {
				AXCP.command(AXCP.DIGITAL_SENSOR_SUBSCRIPTION, listeners, getNumberOfDigitals());
			} catch (RequestTimeoutException e) {
			}
		}
	}
	
	public static void setFloating(Integer... portsEnabled) throws NotConnectedException {
		List<Integer> l = new ArrayList<Integer>(Arrays.asList(portsEnabled));
		List<Integer> pullups = new ArrayList<Integer>(NUMBER_OF_DIGITALS - portsEnabled.length);
		for(int i=0; i<NUMBER_OF_DIGITALS; i++)
			if(!l.contains(i))
				pullups.add(i);
		setPullups(pullups);
	}

	/**
	 * @param portsDisabled
	 *            port numbers to DISABLE the pullup resistor. all others will
	 *            be ENABLED.
	 */
	private static void setPullups(List<Integer> portsEnabled) throws NotConnectedException {
		try {
			AXCP.command(AXCP.DIGITAL_PULLUP_ACTION, portsEnabled, getNumberOfDigitals());
		} catch (RequestTimeoutException e) {
		}
		for (ComponentListener listener : ComponentListener._l_component)
			listener.digitalPullup(portsEnabled);
	}

	/**
	 * @param portsOutput
	 *            port numbers to set to OUTPUT, all others will be set to INPUT
	 * @throws NotConnectedException
	 */
	public static void setOutputMode(Integer... portsOutput) throws NotConnectedException {
		List<Integer> l = new ArrayList<Integer>(Arrays.asList(portsOutput));
		try {
			AXCP.command(AXCP.DIGITAL_OUTPUT_MODE_ACTION, l, getNumberOfDigitals());
		} catch (RequestTimeoutException e) {
		}
		for (ComponentListener listener : ComponentListener._l_component)
			listener.digitalSetMode(l);
	}

	// TODO: set output level

	private static int getNumberOfDigitals() throws NotConnectedException {
		if (NUMBER_OF_DIGITALS == 0xFF) {
			HardwareController type = AXCPAccessor.getInstance().getConnectedHWType();
			if (type == null)
				throw new NotConnectedException();
			NUMBER_OF_DIGITALS = AXCP.hwTypeToDigitals.get(type);
		}
		return NUMBER_OF_DIGITALS;
	}

	public static interface Listener {
		public void update(boolean... value);
	}
}
