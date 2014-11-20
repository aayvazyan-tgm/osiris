/*
 * COPYRIGHT 2014 Christoph Krofitsch
 * Practical Robotics Institute Austria
 */

package org.andrix.sensors;

import org.andrix.AXCP;
import org.andrix.listeners.ComponentListener;
import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;

public class ControllerBattery {

	public Integer getValue() throws NotConnectedException, RequestTimeoutException {
		Integer value = (Integer) AXCP.command(AXCP.CONTROLLER_BATTERY_CHARGE_REQUEST);
		for (ComponentListener listener : ComponentListener._l_component)
			listener.controllerCharge(value);
		return value;
	}

	public Boolean isCharging() throws NotConnectedException, RequestTimeoutException {
		Boolean value = (Boolean) AXCP.command(AXCP.CONTROLLER_BATTERY_CHARGING_STATE_REQUEST);
		for (ComponentListener listener : ComponentListener._l_component)
			listener.controllerChargingState(value);
		return value;
	}
}
