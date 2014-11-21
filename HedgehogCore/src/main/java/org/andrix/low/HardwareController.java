/*
 * COPYRIGHT 2014 Christoph Krofitsch
 * Practical Robotics Institute Austria
 */

package org.andrix.low;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.andrix.AXCP;
import org.andrix.deployment.FunctionInsert;
import org.andrix.listeners.StateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HardwareController {

	public static final int TYPE_V2 = 1;
	public static final int TYPE_V3 = 2;

	private static final Logger log = LoggerFactory.getLogger(HardwareController.class);

	public int type;
	public long lastUpdate;
	public InetAddress address;

	private String name;
	private String password;
	private List<FunctionInsert> supported;

	private List<Integer> analogPullupsActivated;
	private List<Integer> digitalPullupsActivated;

	private String wifiSSID;
	private String wifiPassword;
	private boolean wifiHosting;

	public HardwareController(InetAddress address, int type, String name) {
		this.name = name;
		this.type = type;
		this.address = address;
		if (type == TYPE_V2 || type == TYPE_V3)
			supported = new ArrayList<FunctionInsert>(Arrays.asList(FunctionInsert.values()));
	}

	public boolean connect() {
		return AXCPAccessor.getInstance().connectController(this);
	}

	public boolean authenticate(String password) throws NotConnectedException, RequestTimeoutException {
		boolean successful = (Boolean) AXCP.command(AXCP.CONTROLLER_AUTHENTICATE_REQUEST, password);
		if (successful) {
			this.password = password;
			for (StateListener l : StateListener._l_state)
				l.connectionStateChange(ConnectionState.CONNECTED_AUTH, this);
		}
		return successful;
	}

	public String getName() {
		return name;
	}

	public void updateName() throws NotConnectedException, RequestTimeoutException {
		byte[] result = (byte[]) AXCP.command(AXCP.HW_CONTROLLER_GET_MEMORY_REQUEST, AXCP.MEMORYTYPE_NAME);
		name = new String(result);
	}

	public void setName(String name) throws NotConnectedException {
		try {
			AXCP.command(AXCP.HW_CONTROLLER_SET_MEMORY_ACTION, AXCP.MEMORYTYPE_NAME, name.getBytes());
			this.name = name;
		} catch (RequestTimeoutException e) {
		}
	}

	public String getPassword() {
		return password;
	}

	public void updatePassword() throws NotConnectedException, RequestTimeoutException {
		byte[] result = (byte[]) AXCP.command(AXCP.HW_CONTROLLER_GET_MEMORY_REQUEST, AXCP.MEMORYTYPE_PASSWORD);
		password = new String(result);
	}

	public boolean setPassword(String oldPassword, String newPassword) throws NotConnectedException {
		if (oldPassword.equals(password)) {
			try {
				AXCP.command(AXCP.HW_CONTROLLER_SET_MEMORY_ACTION, AXCP.MEMORYTYPE_PASSWORD, newPassword.getBytes());
				this.password = newPassword;
			} catch (RequestTimeoutException e) {
			}
			return true;
		} else {
			return false;
		}
	}

	public List<Integer> getAnalogPullups() {
		return analogPullupsActivated;
	}

	public boolean updateAnalogPullups() throws NotConnectedException, RequestTimeoutException {
		byte[] result = (byte[]) AXCP.command(AXCP.HW_CONTROLLER_GET_MEMORY_REQUEST, AXCP.MEMORYTYPE_PULLUPS_ANALOG);
		if (result.length == 0 || Math.ceil(AXCP.hwTypeToAnalogs.get(type) / 8d) != result.length) {
			log.warn("Wrong answer format for analog pullups memory");
			return false;
		}
		if (analogPullupsActivated == null)
			analogPullupsActivated = new ArrayList<Integer>();
		else
			analogPullupsActivated.clear();
		for (int i = 0; i < AXCP.hwTypeToAnalogs.get(type); i++)
			if ((result[result.length - 1 - (i / 8)] & (1 << (i % 8))) != 0)
				analogPullupsActivated.add(i);
		return true;
	}

	public void setAnalogPullup(int port, boolean activated) throws NotConnectedException {
		if (activated) {
			if (!analogPullupsActivated.contains(port))
				analogPullupsActivated.add(port);
			pushAnalogPullups();
		} else {
			analogPullupsActivated.remove(new Integer(port));
			pushAnalogPullups();
		}
	}

	private void pushAnalogPullups() throws NotConnectedException {
		byte[] bitmask = new byte[32];
		for (int i = 0; i < bitmask.length; i++)
			bitmask[i] = (byte) 0x00;
		for (Integer port : analogPullupsActivated)
			bitmask[port / 8] |= (1 << (port % 8));
		byte[] send = new byte[(int) Math.ceil(AXCP.hwTypeToAnalogs.get(type) / 8d)];
		for (int i = 0; i < send.length; i++)
			send[i] = bitmask[send.length - i - 1];
		try {
			AXCP.command(AXCP.HW_CONTROLLER_SET_MEMORY_ACTION, AXCP.MEMORYTYPE_PULLUPS_ANALOG, send);
		} catch (RequestTimeoutException e) {
		}
	}

	public List<Integer> getDigitalPullups() {
		return digitalPullupsActivated;
	}

	public boolean updateDigitalPullups() throws NotConnectedException, RequestTimeoutException {
		byte[] result = (byte[]) AXCP.command(AXCP.HW_CONTROLLER_GET_MEMORY_REQUEST, AXCP.MEMORYTYPE_PULLUPS_DIGITAL);
		if (result.length == 0 || Math.ceil(AXCP.hwTypeToDigitals.get(type) / 8d) != result.length) {
			log.warn("Wrong answer format for digital pullups memory");
			return false;
		}
		if (digitalPullupsActivated == null)
			digitalPullupsActivated = new ArrayList<Integer>();
		else
			digitalPullupsActivated.clear();
		for (int i = 0; i < AXCP.hwTypeToDigitals.get(type); i++)
			if ((result[result.length - 1 - (i / 8)] & (1 << (i % 8))) != 0)
				digitalPullupsActivated.add(i);
		return true;
	}

	public void setDigitalPullup(int port, boolean activated) throws NotConnectedException {
		if (activated) {
			if (!digitalPullupsActivated.contains(port)) {
				digitalPullupsActivated.add(port);
				pushDigitalPullups();
			}
		} else {
			digitalPullupsActivated.remove(new Integer(port));
			pushDigitalPullups();
		}
	}

	private void pushDigitalPullups() throws NotConnectedException {
		byte[] bitmask = new byte[32];
		for (int i = 0; i < bitmask.length; i++)
			bitmask[i] = (byte) 0x00;
		for (Integer port : digitalPullupsActivated)
			bitmask[port / 8] |= (1 << (port % 8));
		byte[] send = new byte[(int) Math.ceil(AXCP.hwTypeToDigitals.get(type) / 8d)];
		for (int i = 0; i < send.length; i++)
			send[i] = bitmask[send.length - i - 1];
		try {
			AXCP.command(AXCP.HW_CONTROLLER_SET_MEMORY_ACTION, AXCP.MEMORYTYPE_PULLUPS_DIGITAL, send);
		} catch (RequestTimeoutException e) {
		}
	}

	public void setWifiSSID(String wifiSSID) throws NotConnectedException {
		try {
			AXCP.command(AXCP.HW_CONTROLLER_SET_MEMORY_ACTION, AXCP.MEMORYTYPE_WIFI_SSID, wifiSSID.getBytes());
			this.wifiSSID = wifiSSID;
		} catch (RequestTimeoutException e) {
		}
	}

	public void updateWifiSSID() throws NotConnectedException, RequestTimeoutException {
		byte[] result = (byte[]) AXCP.command(AXCP.HW_CONTROLLER_GET_MEMORY_REQUEST, AXCP.MEMORYTYPE_WIFI_SSID);
		wifiSSID = new String(result);
	}

	public String getWifiSSID() {
		return wifiSSID;
	}

	public void setWifiPassword(String wifiPassword) throws NotConnectedException {
		try {
			AXCP.command(AXCP.HW_CONTROLLER_SET_MEMORY_ACTION, AXCP.MEMORYTYPE_WIFI_PASSWORD, wifiPassword.getBytes());
			this.wifiPassword = wifiPassword;
		} catch (RequestTimeoutException e) {
		}
	}

	public void updateWifiPassword() throws NotConnectedException, RequestTimeoutException {
		byte[] result = (byte[]) AXCP.command(AXCP.HW_CONTROLLER_GET_MEMORY_REQUEST, AXCP.MEMORYTYPE_WIFI_PASSWORD);
		wifiPassword = new String(result);
	}

	public String getWifiPassword() {
		return wifiPassword;
	}

	public void setWifiHosting(boolean wifiHosting) throws NotConnectedException {
		try {
			AXCP.command(AXCP.HW_CONTROLLER_SET_MEMORY_ACTION, AXCP.MEMORYTYPE_WIFI_HOST,
					new byte[] { wifiHosting ? (byte) 0x01 : (byte) 0x00 });
			this.wifiHosting = wifiHosting;
		} catch (RequestTimeoutException e) {
		}
	}

	public void updateWifiHosting() throws NotConnectedException, RequestTimeoutException {
		byte[] result = (byte[]) AXCP.command(AXCP.HW_CONTROLLER_GET_MEMORY_REQUEST, AXCP.MEMORYTYPE_WIFI_HOST);
		if (result.length != 1) {
			log.warn("Wrong format for WiFi Host Answer!");
			return;
		}
		wifiHosting = Boolean.valueOf(result[0] % 2 == 1);
	}

	public boolean isWifiHosting() {
		return wifiHosting;
	}

	public boolean isSupported(FunctionInsert insert) {
		return supported.contains(insert);
	}
}
