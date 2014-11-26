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
 * Class Analog<br>
 * Represents a physical analog sensor that is connected to the controller. It
 * returns an integer in the range 0-1023.
 * 
 * @author Christoph Krofitsch
 * @version 02.10.2012
 */
public class Analog {

	private static int NUMBER_OF_ANALOGS = 0xFF;

	private static Map<Listener, int[]> listeners = new HashMap<Listener, int[]>();

	private int port;

	/**
	 * Creates a new analog sensor which is connected to the specified port.
	 * 
	 * @param port
	 *            the port the sensor is connected to.
	 * @throws InvalidPortException
	 *             if the specified port doesn't exist.
	 */
	public Analog(int port) throws InvalidPortException, NotConnectedException {
		this.port = port;
		if (port >= getNumberOfAnalogs() || port < 0)
			throw new InvalidPortException("Valid analog sensor port numbers: 0 - " + (NUMBER_OF_ANALOGS - 1));
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
	 * The range for this sensor type is from 0 to 1023. This method fetches the
	 * current value of the sensor from the controller. If the request fails or
	 * the time is running out, -1 is returned as default.
	 */
	public Integer getValue() throws NotConnectedException, RequestTimeoutException {
		Integer value =  (Integer) AXCP.command(AXCP.ANALOG_SENSOR_REQUEST, port);
		for (ComponentListener listener : ComponentListener._l_component)
			listener.analogReading(port, value);
		return value;
	}

	public void registerListener(Listener listener) throws NotConnectedException  {
		registerCompoundListener(listener, port);
	}

	public void unregisterListener(Listener listener) throws NotConnectedException {
		unregisterCompoundListener(listener);
	}

	public static void registerCompoundListener(Listener listener, int... ports) throws NotConnectedException {
		if (!listeners.containsKey(listener)) {
			listeners.put(listener, ports);
			try {
				AXCP.command(AXCP.ANALOG_SENSOR_SUBSCRIPTION, listeners, getNumberOfAnalogs());
			} catch (RequestTimeoutException e) {
			}
		}
	}

	public static void unregisterCompoundListener(Listener listener) throws NotConnectedException {
		if (listeners.containsKey(listener)) {
			listeners.remove(listener);
			try {
				AXCP.command(AXCP.ANALOG_SENSOR_SUBSCRIPTION, listeners, getNumberOfAnalogs());
			} catch (RequestTimeoutException e) {
			}
		}
	}
	
	public static void setFloating(Integer... portsEnabled) throws NotConnectedException {
		List<Integer> l = new ArrayList<Integer>(Arrays.asList(portsEnabled));
		List<Integer> pullups = new ArrayList<Integer>(getNumberOfAnalogs() - portsEnabled.length);
		for(int i=0; i<NUMBER_OF_ANALOGS; i++)
			if(!l.contains(i))
				pullups.add(i);
		setPullups(pullups);
	}

	/**
	 * @param pullups
	 *            port numbers to DISABLE the pullup resistor. all others will
	 *            be enabled.
	 */
	private static void setPullups(List<Integer> portsEnabled) throws NotConnectedException {
		try {
			AXCP.command(AXCP.ANALOG_PULLUP_ACTION, portsEnabled, getNumberOfAnalogs());
		} catch (RequestTimeoutException e) {
		}
		for (ComponentListener listener : ComponentListener._l_component)
			listener.analogPullup(portsEnabled);
	}

	private static int getNumberOfAnalogs() throws NotConnectedException {
		if (NUMBER_OF_ANALOGS == 0xFF) {
			HardwareController hwc = AXCPAccessor.getInstance().getConnectedHWType();
			if (hwc == null)
				throw new NotConnectedException();
			NUMBER_OF_ANALOGS = AXCP.hwTypeToAnalogs.get(hwc.type);
		}
		return NUMBER_OF_ANALOGS;
	}

	public static interface Listener {
		public void update(int... values);
	}
}
