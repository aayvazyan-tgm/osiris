/*
 * COPYRIGHT 2013 Christoph Krofitsch
 * Practical Robotics Institute Austria
 */

package org.andrix;

import java.io.IOException;
import java.net.InetAddress;
import java.security.InvalidParameterException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.andrix.listeners.AXCPListener;
import org.andrix.listeners.ComponentListener;
import org.andrix.listeners.DeploymentListener;
import org.andrix.listeners.StateListener;
import org.andrix.low.AXCPAccessor;
import org.andrix.low.HardwareController;
import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;
import org.andrix.motors.Motor;
import org.andrix.sensors.Analog;
import org.andrix.sensors.Digital;

public class AXCP {

	public static final DecimalFormat portFormat = new DecimalFormat("00");

	public final static byte NOP = 0;
	public final static byte ANALOG_SENSOR_REQUEST = 10;
	public final static byte ANALOG_SENSOR_REPLY = 11;
	public final static byte ANALOG_SENSOR_SUBSCRIPTION = 12;
	public final static byte ANALOG_SENSOR_UPDATE = 13;
	public final static byte ANALOG_PULLUP_ACTION = 14;
	public final static byte DIGITAL_SENSOR_REQUEST = 20;
	public final static byte DIGITAL_SENSOR_REPLY = 21;
	public final static byte DIGITAL_SENSOR_SUBSCRIPTION = 22;
	public final static byte DIGITAL_SENSOR_UPDATE = 23;
	public final static byte DIGITAL_PULLUP_ACTION = 24;
	public final static byte DIGITAL_OUTPUT_MODE_ACTION = 25;
	public final static byte DIGITAL_OUTPUT_LEVEL_ACTION = 26;
	public final static byte MOTOR_POWER_ACTION = 30;
	public final static byte MOTOR_VELOCITY_ACTION = 31;
	public final static byte MOTOR_POWER_ABSOLUTE_POSITION_ACTION = 32;
	public final static byte MOTOR_VELOCITY_ABSOLUTE_POSITION_ACTION = 33;
	public final static byte MOTOR_POWER_RELATIVE_POSITION_ACTION = 34;
	public final static byte MOTOR_VELOCITY_RELATIVE_POSITION_ACTION = 35;
	public final static byte MOTOR_FREEZE_ACTION = 36;
	public final static byte MOTOR_BRAKE_ACTION = 37;
	public final static byte MOTOR_OFF_ACTION = 38;
	public final static byte MOTOR_POSITION_REQUEST = 40;
	public final static byte MOTOR_POSITION_REPLY = 41;
	public final static byte MOTOR_POSITION_REACHED_ACTION = 42;
	public final static byte MOTOR_POSITION_SUBSCRIPTION = 43;
	public final static byte MOTOR_POSITION_UPDATE = 44;
	public final static byte MOTOR_CLEAR_POSITION_ACTION = 45;
	public final static byte MOTOR_VELOCITY_REQUEST = 46;
	public final static byte MOTOR_VELOCITY_REPLY = 47;
	public final static byte MOTOR_VELOCITY_SUBSCRIPTION = 48;
	public final static byte MOTOR_VELOCITY_UPDATE = 49;
	public final static byte SERVO_ONOFF_ACTION = 50;
	public final static byte SERVO_DRIVE_ACTION = 51;
	public final static byte CONTROLLER_BATTERY_CHARGE_REQUEST = 60;
	public final static byte CONTROLLER_BATTERY_CHARGE_REPLY = 61;
	public final static byte CONTROLLER_BATTERY_CHARGING_STATE_REQUEST = 62;
	public final static byte CONTROLLER_BATTERY_CHARGING_STATE_REPLY = 63;
	public final static byte PHONE_BATTERY_CHARGE_REQUEST = 64;
	public final static byte PHONE_BATTERY_CHARGE_REPLY = 65;
	public final static byte PHONE_BATTERY_CHARGING_STATE_REQUEST = 66;
	public final static byte PHONE_BATTERY_CHARGING_STATE_REPLY = 67;
	public final static byte CONTROLLER_BATTERY_UPDATE = 68;
	public final static byte PHONE_SENSOR_REQUEST = 70;
	public final static byte PHONE_SENSOR_REPLY = 71;
	public final static byte PHONE_SENSOR_AVAILABILITY_REQUEST = 72;
	public final static byte PHONE_SENSOR_AVAILABILITY_REPLY = 73;
	public final static byte PHONE_CAMERA_TAKE_PICTURE_ACTION = 80;
	public final static byte PHONE_CAMERA_GET_BLOB_COUNT_REQUEST = 81;
	public final static byte PHONE_CAMERA_GET_BLOB_COUNT_REPLY = 82;
	public final static byte PHONE_CAMERA_GET_BLOB_REQUEST = 83;
	public final static byte PHONE_CAMERA_GET_BLOB_REPLY = 84;
	public final static byte PHONE_CAMERA_SET_CHANNEL_ACTION = 85;
	public final static byte HW_CONTROLLER_OFF_ACTION = 90;
	public final static byte HW_CONTROLLER_RESET_ACTION = 91;
	public final static byte SW_CONTROLLER_OFF_ACTION = 92;
	public final static byte SW_CONTROLLER_RESET_ACTION = 93;
	public final static byte PHONE_OFF_ACTION = 94;
	public final static byte PHONE_RESET_ACTION = 95;
	public final static byte ERROR_ACTION = 96;
	public final static byte CUSTOM_ACTION = 97;
	public final static byte DEBUG_INFORMATION_UPDATE = 100;
	public final static byte HW_CONTROLLER_TYPE_REQUEST = 110;
	public final static byte HW_CONTROLLER_TYPE_REPLY = 111;
	public final static byte SW_CONTROLLER_TYPE_REQUEST = 112;
	public final static byte SW_CONTROLLER_TYPE_REPLY = 113;
	public final static byte PHONE_TYPE_REQUEST = 114;
	public final static byte PHONE_TYPE_REPLY = 115;
	public final static byte HW_CONTROLLER_SET_MEMORY_ACTION = 116;
	public final static byte ENVIRONMENT_SCAN_SUBSCRIPTION = 120;
	public final static byte ENVIRONMENT_SCAN_HW_CONTROLLER_UPDATE = 121;
	public final static byte ENVIRONMENT_SCAN_SW_CONTROLLER_UPDATE = 122;
	public final static byte ENVIRONMENT_SCAN_PHONE_UPDATE = 123;
	public final static byte CONTROLLER_AUTHENTICATE_REQUEST = 124;
	public final static byte CONTROLLER_AUTHENTICATE_REPLY = 125;
	public final static byte HW_CONTROLLER_GET_MEMORY_REQUEST = 126;
	public final static byte HW_CONTROLLER_GET_MEMORY_REPLY = 127;

	public final static byte PROGRAM_COMPILE_REQUEST = (byte) 150;
	public final static byte PROGRAM_COMPILE_REPLY = (byte) 151;
	public final static byte PROGRAM_EXECUTE_ACTION = (byte) 152;
	public final static byte PROGRAM_COMPILE_EXECUTE_REQUEST = (byte) 153;
	public final static byte PROGRAM_COMPILE_EXECUTE_REPLY = (byte) 154;
	public final static byte PROGRAMS_FETCH_SUBSCRIPTION = (byte) 155;
	public final static byte PROGRAMS_FETCH_UPDATE = (byte) 156;
	public final static byte PROGRAMS_FETCH_DONE_UPDATE = (byte) 157;
	public final static byte EXECUTION_STATE_REQUEST = (byte) 160;
	public final static byte EXECUTION_STATE_REPLY = (byte) 161;
	public final static byte EXECUTION_PAUSE_ACTION = (byte) 162;
	public final static byte EXECUTION_RESUME_ACTION = (byte) 163;
	public final static byte EXECUTION_STOP_ACTION = (byte) 164;
	public final static byte EXECUTION_RESTART_ACTION = (byte) 165;
	public final static byte EXECUTION_PRINTOUTACTION = (byte) 170;

	public final static byte ERRORCODE_UNSPECIFIED_OPCODE = 1;
	public final static byte ERRORCODE_ANALOG_PORT_OUT_OF_RANGE = 2;
	public final static byte ERRORCODE_DIGITAL_PORT_OUT_OF_RANGE = 3;
	public final static byte ERRORCODE_MOTOR_PORT_OUT_OF_RANGE = 4;
	public final static byte ERRORCODE_SERVO_PORT_OUT_OF_RANGE = 5;
	public final static byte ERRORCODE_SERVO_IS_OFF = 6;
	public final static byte ERRORCODE_PHONE_SENSOR_TYPE_NOT_SUPPORTED = 7;
	public final static byte ERRORCODE_PHONE_SENSOR_TYPE_DOES_NOT_EXIST = 8;
	public final static byte ERRORCODE_CAMERA_CHANNEL_NOT_CONFIGURED = 9;
	public final static byte ERRORCODE_NO_BLOB_AT_INDEX = 10;
	public final static byte ERRORCODE_OPERATION_NOT_SUPPORTED = 11;
	public final static byte ERRORCODE_PAYLOAD_LENGTH_OUT_OF_RANGE = 12;
	public final static byte ERRORCODE_INCOMPLETE_COMMAND_TIMEOUT = 13;
	public final static byte ERRORCODE_PROGRAM_NOT_FOUND = (byte) 150;
	public final static byte ERRORCODE_A_PROGRAM_IS_AREADY_RUNNING = (byte) 151;
	public final static byte ERRORCODE_NO_HW_CONTROLLER_CONNECTED = (byte) 152;
	public final static byte ERRORCODE_UNSPECIFIED_ERROR = (byte) 255;

	public final static byte MEMORYTYPE_NAME = 0;
	public final static byte MEMORYTYPE_PASSWORD = 1;
	public final static byte MEMORYTYPE_PULLUPS_ANALOG = 2;
	public final static byte MEMORYTYPE_PULLUPS_DIGITAL = 3;
	public final static byte MEMORYTYPE_WIFI_SSID = 4;
	public final static byte MEMORYTYPE_WIFI_PASSWORD = 5;
	public final static byte MEMORYTYPE_WIFI_HOST = 6;

	public static final Map<Byte, Byte> requestToReply;
	static {
		requestToReply = new HashMap<Byte, Byte>();
		requestToReply.put(ANALOG_SENSOR_REQUEST, ANALOG_SENSOR_REPLY);
		requestToReply.put(DIGITAL_SENSOR_REQUEST, DIGITAL_SENSOR_REPLY);
		requestToReply.put(MOTOR_POSITION_REQUEST, MOTOR_POSITION_REPLY);
		requestToReply.put(MOTOR_VELOCITY_REQUEST, MOTOR_VELOCITY_REPLY);
		requestToReply.put(CONTROLLER_BATTERY_CHARGE_REQUEST, CONTROLLER_BATTERY_CHARGE_REPLY);
		requestToReply.put(CONTROLLER_BATTERY_CHARGING_STATE_REQUEST, CONTROLLER_BATTERY_CHARGING_STATE_REPLY);
		requestToReply.put(PHONE_BATTERY_CHARGE_REQUEST, PHONE_BATTERY_CHARGE_REPLY);
		requestToReply.put(PHONE_BATTERY_CHARGING_STATE_REQUEST, PHONE_BATTERY_CHARGING_STATE_REPLY);
		requestToReply.put(PHONE_SENSOR_AVAILABILITY_REQUEST, PHONE_SENSOR_AVAILABILITY_REPLY);
		requestToReply.put(PHONE_SENSOR_REQUEST, PHONE_SENSOR_REPLY);
		requestToReply.put(PHONE_CAMERA_GET_BLOB_COUNT_REQUEST, PHONE_CAMERA_GET_BLOB_COUNT_REPLY);
		requestToReply.put(PHONE_CAMERA_GET_BLOB_REQUEST, PHONE_CAMERA_GET_BLOB_REPLY);
		requestToReply.put(HW_CONTROLLER_TYPE_REQUEST, HW_CONTROLLER_TYPE_REPLY);
		requestToReply.put(SW_CONTROLLER_TYPE_REQUEST, SW_CONTROLLER_TYPE_REPLY);
		requestToReply.put(PHONE_TYPE_REQUEST, PHONE_TYPE_REPLY);
		requestToReply.put(CONTROLLER_AUTHENTICATE_REQUEST, CONTROLLER_AUTHENTICATE_REPLY);
		requestToReply.put(HW_CONTROLLER_GET_MEMORY_REQUEST, HW_CONTROLLER_GET_MEMORY_REPLY);
		requestToReply.put(PROGRAM_COMPILE_REQUEST, PROGRAM_COMPILE_REPLY);
		requestToReply.put(PROGRAM_COMPILE_EXECUTE_REQUEST, PROGRAM_COMPILE_EXECUTE_REPLY);
		requestToReply.put(EXECUTION_STATE_REQUEST, EXECUTION_STATE_REPLY);
	}
	public static final Map<Byte, Integer> payloadLengths;
	static {
		payloadLengths = new HashMap<Byte, Integer>();
		payloadLengths.put(NOP, 0);
		payloadLengths.put(ANALOG_SENSOR_REQUEST, 1);
		payloadLengths.put(ANALOG_SENSOR_REPLY, 3);
		payloadLengths.put(ANALOG_SENSOR_SUBSCRIPTION, -1);
		payloadLengths.put(ANALOG_SENSOR_UPDATE, -1);
		payloadLengths.put(ANALOG_PULLUP_ACTION, -1);
		payloadLengths.put(DIGITAL_SENSOR_REQUEST, 1);
		payloadLengths.put(DIGITAL_SENSOR_REPLY, 2);
		payloadLengths.put(DIGITAL_SENSOR_SUBSCRIPTION, -1);
		payloadLengths.put(DIGITAL_SENSOR_UPDATE, -1);
		payloadLengths.put(DIGITAL_PULLUP_ACTION, -1);
		payloadLengths.put(DIGITAL_OUTPUT_MODE_ACTION, -1);
		payloadLengths.put(DIGITAL_OUTPUT_LEVEL_ACTION, 2);
		payloadLengths.put(MOTOR_POWER_ACTION, 3);
		payloadLengths.put(MOTOR_VELOCITY_ACTION, 3);
		payloadLengths.put(MOTOR_POWER_ABSOLUTE_POSITION_ACTION, 6);
		payloadLengths.put(MOTOR_VELOCITY_ABSOLUTE_POSITION_ACTION, 6);
		payloadLengths.put(MOTOR_POWER_RELATIVE_POSITION_ACTION, 6);
		payloadLengths.put(MOTOR_VELOCITY_RELATIVE_POSITION_ACTION, 6);
		payloadLengths.put(MOTOR_FREEZE_ACTION, 1);
		payloadLengths.put(MOTOR_BRAKE_ACTION, 2);
		payloadLengths.put(MOTOR_OFF_ACTION, 1);
		payloadLengths.put(MOTOR_POSITION_REQUEST, 1);
		payloadLengths.put(MOTOR_POSITION_REPLY, 5);
		payloadLengths.put(MOTOR_POSITION_REACHED_ACTION, 1);
		payloadLengths.put(MOTOR_POSITION_SUBSCRIPTION, -1);
		payloadLengths.put(MOTOR_POSITION_UPDATE, -1);
		payloadLengths.put(MOTOR_CLEAR_POSITION_ACTION, 1);
		payloadLengths.put(MOTOR_VELOCITY_REQUEST, 1);
		payloadLengths.put(MOTOR_VELOCITY_REPLY, 3);
		payloadLengths.put(MOTOR_VELOCITY_SUBSCRIPTION, -1);
		payloadLengths.put(MOTOR_VELOCITY_UPDATE, -1);
		payloadLengths.put(SERVO_ONOFF_ACTION, 2);
		payloadLengths.put(SERVO_DRIVE_ACTION, 2);
		payloadLengths.put(CONTROLLER_BATTERY_CHARGE_REQUEST, 0);
		payloadLengths.put(CONTROLLER_BATTERY_CHARGE_REPLY, 1);
		payloadLengths.put(CONTROLLER_BATTERY_CHARGING_STATE_REQUEST, 0);
		payloadLengths.put(CONTROLLER_BATTERY_CHARGING_STATE_REPLY, 1);
		payloadLengths.put(PHONE_BATTERY_CHARGE_REQUEST, 0);
		payloadLengths.put(PHONE_BATTERY_CHARGE_REPLY, 1);
		payloadLengths.put(PHONE_BATTERY_CHARGING_STATE_REQUEST, 0);
		payloadLengths.put(PHONE_BATTERY_CHARGING_STATE_REPLY, 1);
		payloadLengths.put(CONTROLLER_BATTERY_UPDATE, 2);
		payloadLengths.put(PHONE_SENSOR_REQUEST, 1);
		payloadLengths.put(PHONE_SENSOR_REPLY, -1);
		payloadLengths.put(PHONE_SENSOR_AVAILABILITY_REQUEST, 0);
		payloadLengths.put(PHONE_SENSOR_AVAILABILITY_REPLY, 4);
		payloadLengths.put(PHONE_CAMERA_TAKE_PICTURE_ACTION, 0);
		payloadLengths.put(PHONE_CAMERA_GET_BLOB_COUNT_REQUEST, 1);
		payloadLengths.put(PHONE_CAMERA_GET_BLOB_COUNT_REPLY, 2);
		payloadLengths.put(PHONE_CAMERA_GET_BLOB_REQUEST, 2);
		payloadLengths.put(PHONE_CAMERA_GET_BLOB_REPLY, 10);
		payloadLengths.put(PHONE_CAMERA_SET_CHANNEL_ACTION, 7);
		payloadLengths.put(HW_CONTROLLER_OFF_ACTION, 0);
		payloadLengths.put(HW_CONTROLLER_RESET_ACTION, 0);
		payloadLengths.put(SW_CONTROLLER_OFF_ACTION, 0);
		payloadLengths.put(SW_CONTROLLER_RESET_ACTION, 0);
		payloadLengths.put(PHONE_OFF_ACTION, 0);
		payloadLengths.put(PHONE_RESET_ACTION, 0);
		payloadLengths.put(ERROR_ACTION, 2);
		payloadLengths.put(CUSTOM_ACTION, -1);
		payloadLengths.put(DEBUG_INFORMATION_UPDATE, -1);
		payloadLengths.put(HW_CONTROLLER_TYPE_REQUEST, 0);
		payloadLengths.put(HW_CONTROLLER_TYPE_REPLY, 1);
		payloadLengths.put(SW_CONTROLLER_TYPE_REQUEST, 0);
		payloadLengths.put(SW_CONTROLLER_TYPE_REPLY, 1);
		payloadLengths.put(PHONE_TYPE_REQUEST, 0);
		payloadLengths.put(PHONE_TYPE_REPLY, 1);
		payloadLengths.put(ENVIRONMENT_SCAN_SUBSCRIPTION, 0);
		payloadLengths.put(ENVIRONMENT_SCAN_HW_CONTROLLER_UPDATE, 33);
		payloadLengths.put(ENVIRONMENT_SCAN_SW_CONTROLLER_UPDATE, 1);
		payloadLengths.put(ENVIRONMENT_SCAN_PHONE_UPDATE, 1);
		payloadLengths.put(HW_CONTROLLER_SET_MEMORY_ACTION, -1);
		payloadLengths.put(CONTROLLER_AUTHENTICATE_REQUEST, -1);
		payloadLengths.put(CONTROLLER_AUTHENTICATE_REPLY, 1);
		payloadLengths.put(HW_CONTROLLER_GET_MEMORY_REQUEST, 1);
		payloadLengths.put(HW_CONTROLLER_GET_MEMORY_REPLY, -1);

		payloadLengths.put(PROGRAM_COMPILE_REQUEST, -1);
		payloadLengths.put(PROGRAM_COMPILE_REPLY, -1);
		payloadLengths.put(PROGRAM_EXECUTE_ACTION, 34);
		payloadLengths.put(PROGRAM_COMPILE_EXECUTE_REQUEST, -1);
		payloadLengths.put(PROGRAM_COMPILE_EXECUTE_REPLY, -1);
		payloadLengths.put(PROGRAMS_FETCH_SUBSCRIPTION, 0);
		payloadLengths.put(PROGRAMS_FETCH_UPDATE, -1);
		payloadLengths.put(PROGRAMS_FETCH_DONE_UPDATE, 0);
		payloadLengths.put(EXECUTION_STATE_REQUEST, 0);
		payloadLengths.put(EXECUTION_STATE_REPLY, -1);
		payloadLengths.put(EXECUTION_PAUSE_ACTION, 0);
		payloadLengths.put(EXECUTION_RESUME_ACTION, 0);
		payloadLengths.put(EXECUTION_STOP_ACTION, 0);
		payloadLengths.put(EXECUTION_RESTART_ACTION, 0);
		payloadLengths.put(EXECUTION_PRINTOUTACTION, -1);
	}

	public static final Map<Integer, Integer> hwTypeToAnalogs;
	public static final Map<Integer, Integer> hwTypeToDigitals;
	public static final Map<Integer, Integer> hwTypeToMotors;
	public static final Map<Integer, Integer> hwTypeToServos;
	static {
		hwTypeToAnalogs = new HashMap<Integer, Integer>();
		hwTypeToDigitals = new HashMap<Integer, Integer>();
		hwTypeToMotors = new HashMap<Integer, Integer>();
		hwTypeToServos = new HashMap<Integer, Integer>();
		hwTypeToAnalogs.put(HardwareController.TYPE_V2, 16);
		hwTypeToDigitals.put(HardwareController.TYPE_V2, 16);
		hwTypeToMotors.put(HardwareController.TYPE_V2, 6);
		hwTypeToServos.put(HardwareController.TYPE_V2, 6);
		hwTypeToAnalogs.put(HardwareController.TYPE_V3, 16);
		hwTypeToDigitals.put(HardwareController.TYPE_V3, 16);
		hwTypeToMotors.put(HardwareController.TYPE_V3, 6);
		hwTypeToServos.put(HardwareController.TYPE_V3, 6);
	}

	public static Map<Analog.Listener, int[]> analogSubscriptions;
	public static Map<Digital.Listener, int[]> digitalSubscriptions;
	public static Map<Motor.PositionListener, int[]> motorPositionSubscriptions;
	public static Map<Motor.VelocityListener, int[]> motorVelocitySubscriptions;
	static {
		analogSubscriptions = new HashMap<Analog.Listener, int[]>();
		digitalSubscriptions = new HashMap<Digital.Listener, int[]>();
		motorPositionSubscriptions = new HashMap<Motor.PositionListener, int[]>();
		motorVelocitySubscriptions = new HashMap<Motor.VelocityListener, int[]>();
	}

	public static List<AXCPHandler> externalHandlers = new ArrayList<AXCPHandler>();

	public static byte[] lowCommand(byte opcode, byte... payload) throws NotConnectedException, RequestTimeoutException {
		for (AXCPListener listener : AXCPListener._l_AXCP)
			listener.sent(opcode, payload);
		if (requestToReply.containsKey(opcode)) {
			return AXCPAccessor.getInstance().synchronousCommand(opcode, payload, requestToReply.get(opcode));
		} else {
			AXCPAccessor.getInstance().asynchronousCommand(opcode, payload);
			return null;
		}
	}

	private static void forward(byte opcode, byte[] payload) {
		// byte[] command = new byte[payload.length + 1];
		// command[0] = opcode;
		// for (int i = 1; i < command.length; i++)
		// command[i] = payload[i - 1];
		// TODO: forward
	}

	private static void paramsCheck(Object[] params, int number, Class<?>... types) {
		if (params.length < number)
			throw new IllegalArgumentException("Too few arguments!");
		for (int i = 0; i < number; i++) {
			if (!types[i].isInstance(params[i]))
				throw new IllegalArgumentException("Wrong argument type! Expected instance of: "
						+ types[i].getSimpleName() + "; Given: " + params[i].getClass().getSimpleName());
		}
	}

	/**
	 * Used for using the AXCP Protocol for commands invoked from this device.
	 * 
	 * @param opcode
	 *            the operation code, see params
	 * @param params
	 *            ANALOG_SENSOR_REQUEST = 10: params[0]... port number (Integer
	 *            [0,255]) <br>
	 *            ANALOG_SENSOR_SUBSCRIPTION = 12: params[0]... A mapping of
	 *            port number and listener (HashMap<Listener, int[]>) Note:
	 *            empty list equals no entry; params[1]... number of sensors
	 *            (Integer [0,255])<br>
	 *            ANALOG_PULLUP_ACTION = 14: params[0]... list of ports to
	 *            enable pullups (List<Integer>); params[1]... number of sensors
	 *            (Integer [0,255]) <br>
	 *            DIGITAL_SENSOR_REQUEST = 20: params[0]... port number (Integer
	 *            [0,255]) <br>
	 *            DIGITAL_SENSOR_SUBSCRIPTION = 22: params[0]... A mapping of
	 *            port number and listener (HashMap<Integer, List<Listener>>)
	 *            Note: empty list equals no entry; params[1]... number of
	 *            sensors (Integer [0,255])<br>
	 *            DIGITAL_PULLUP_ACTION = 24: params[0]... list of ports to
	 *            enable pullups (List<Integer>); params[1]... number of sensors
	 *            (Integer [0,255])<br>
	 *            DIGITAL_OUTPUT_MODE_ACTION = 25: params[0]... list of ports to
	 *            set to output (List<Integer>); params[1]... number of sensors
	 *            (Integer [0,255])<br>
	 *            DIGITAL_OUTPUT_LEVEL_ACTION = 26: params[0]... port number
	 *            (Integer [0,255]); params[1]... output level (Boolean) <br>
	 *            MOTOR_POWER_ACTION = 30: params[0]... port number (Integer
	 *            [0,255]); params[1]... direction (Boolean); params[2]... power
	 *            (Integer [0,255]) <br>
	 *            MOTOR_VELOCITY_ACTION = 31: params[0]... port number (Integer
	 *            [0,255]); params[1]... direction (Boolean); params[2]...
	 *            velocity (Integer [0,255]) <br>
	 *            MOTOR_POWER_ABSOLUTE_POSITION_ACTION = 32: params[0]... port
	 *            number (Integer [0,255]); params[1]... power (Integer
	 *            [0,255]); params[2]... target position (Integer)<br>
	 *            MOTOR_VELOCITY_ABSOLUTE_POSITION_ACTION = 33: params[0]...
	 *            port number (Integer [0,255]); params[1]... velocity (Integer
	 *            [0,255]); params[2]... target position (Integer) <br>
	 *            MOTOR_POWER_RELATIVE_POSITION_ACTION = 34: params[0]... port
	 *            number (Integer [0,255]); params[1]... power (Integer
	 *            [0,255]); params[2]... delta position (Integer) <br>
	 *            MOTOR_VELOCITY_RELATIVE_POSITION_ACTION = 35: params[0]...
	 *            port number (Integer [0,255]); params[1]... velocity (Integer
	 *            [0,255]); params[2]... delta position (Integer) <br>
	 *            MOTOR_FREEZE_ACTION = 36: params[0]... port number (Integer
	 *            [0,255]) <br>
	 *            MOTOR_BRAKE_ACTION = 37: params[0]... port number (Integer
	 *            [0,255]); params[1]... breaking power (Integer [0,255]) <br>
	 *            MOTOR_OFF_ACTION = 38: params[0]... port number (Integer
	 *            [0,255]) <br>
	 *            MOTOR_POSITION_REQUEST = 40: params[0]... port number (Integer
	 *            [0,255]) <br>
	 *            MOTOR_POSITION_SUBSCRIPTION = 43: params[0]... A mapping of
	 *            port number and listener (HashMap<Integer, List<Listener>>)
	 *            Note: empty list equals no entry; params[1]... number of
	 *            sensors (Integer [0,255])<br>
	 *            MOTOR_CLEAR_POSITION_ACTION = 45: params[0]... port number
	 *            (Integer [0,255]) <br>
	 *            MOTOR_VELOCITY_REQUEST = 46: params[0]... port number (Integer
	 *            [0,255]) <br>
	 *            MOTOR_VELOCITY_SUBSCRIPTION = 48: params[0]... A mapping of
	 *            port number and listener (HashMap<Integer, List<Listener>>)
	 *            Note: empty list equals no entry; params[1]... number of
	 *            sensors (Integer [0,255]) <br>
	 *            SERVO_ONOFF_ACTION = 50: params[0]... port number (Integer
	 *            [0,255]); params[1]... on/off (Boolean) <br>
	 *            SERVO_DRIVE_ACTION = 51: params[0]... port number (Integer
	 *            [0,255]); params[1]... servo position (Integer [0,255]) <br>
	 *            CONTROLLER_BATTERY_CHARGE_REQUEST = 60: no params <br>
	 *            CONTROLLER_BATTERY_CHARGING_STATE_REQUEST = 62: no params <br>
	 *            HW_CONTROLLER_OFF_ACTION = 90: no params <br>
	 *            HW_CONTROLLER_RESET_ACTION = 91: no params <br>
	 *            SW_CONTROLLER_OFF_ACTION = 92: no params <br>
	 *            SW_CONTROLLER_RESET_ACTION = 93: no params <br>
	 *            ERROR_ACTION = 96: params[0]... error code [0,255];
	 *            params[1]... causing opcode [0,255] <br>
	 *            CUSTOM_ACTION = 97: not yet implemented <br>
	 *            HW_CONTROLLER_TYPE_REQUEST = 110: no params <br>
	 *            SW_CONTROLLER_TYPE_REQUEST = 112: no params <br>
	 *            HW_CONTROLLER_SET_MEMORY_ACTION = 116: params[0]... memory
	 *            data type (Integer); params[1]... data (byte[]) <br>
	 *            ENVIRONMENT_SCAN_SUBSCRIPTION = 120: no params <br>
	 *            CONTROLLER_AUTHENTICATE_REQUEST = 124: params[0]... password
	 *            (String, max length 32) <br>
	 *            HW_CONTROLLER_GET_MEMORY_REQUEST = 126: params[0]... memory
	 *            data type (Integer) <br>
	 *            PROGRAM_COMPILE_REQUEST = 150: params[0]... name(String);
	 *            params[1]... version(Integer); params[2]... code(String)<br>
	 *            PROGRAM_EXECUTE_ACTION = 152: params[0]... name(String);
	 *            params[1]... version(Integer)<br>
	 *            PROGRAM_COMPILE_EXECUTE_REQUEST = 153: params[0]...
	 *            name(String); params[1]... version(Integer); params[2]...
	 *            code(String)<br>
	 *            PROGRAMS_FETCH_SUBSCRIPTION = 155: no params<br>
	 *            EXECUTION_STATE_REQUEST = 160: no params<br>
	 * @return ANALOG_SENSOR_REQUEST = 10: sensor value (Integer [0,1023]) <br>
	 *         DIGITAL_SENSOR_REQUEST = 20: sensor value (Boolean) <br>
	 *         MOTOR_POSITION_REQUEST = 40: motor position (Integer) <br>
	 *         MOTOR_VELOCITY_REQUEST = 46: motor velocity (Integer [-255,255]) <br>
	 *         CONTROLLER_BATTERY_CHARGE_REQUEST = 60: battery charge (Integer
	 *         [0,255]) <br>
	 *         CONTROLLER_BATTERY_CHARGING_STATE_REQUEST = 62: charging state
	 *         (Boolean)<br>
	 *         HW_CONTROLLER_TYPE_REQUEST = 110: hw controller type (Integer
	 *         [0,255]) <br>
	 *         SW_CONTROLLER_TYPE_REQUEST = 112: sw controller type (Integer
	 *         [0,255]) <br>
	 *         CONTROLLER_AUTHENTICATE_REQUEST = 124: authentication successful
	 *         (boolean)<br>
	 *         HW_CONTROLLER_GET_MEMORY_REQUEST = 126: data (byte[]) <br>
	 *         PROGRAM_COMPILE_REQUEST = 150: Object[2] mit [0]... compiler
	 *         result (Integer); [1]... compiler message (String)<br>
	 *         PROGRAM_COMPILE_EXECUTE_REQUEST = 153: Object[2] mit [0]...
	 *         compiler result (Integer); [1]... compiler message (String)<br>
	 *         EXECUTION_STATE_REQUEST = 160: Object[3] mit [0]... state
	 *         (Integer); [1]... program name (String); [2]... program version
	 *         (Integer)<br>
	 * 
	 * @throws NotConnectedException
	 *             if there's no connection to a controller
	 * @throws RequestTimeoutException
	 *             if the command is a request and the reply didn't come within
	 *             the timeout interval
	 */
	@SuppressWarnings("unchecked")
	public static Object command(byte opcode, Object... params) throws NotConnectedException, RequestTimeoutException {
		byte[] reply;
		switch (opcode) {
		case NOP:
			lowCommand(opcode);
			break;
		case ANALOG_SENSOR_REQUEST: {
			paramsCheck(params, 1, Number.class);
			reply = lowCommand(opcode, ((Number) params[0]).byteValue());
			// TODO check if port equals! also for digital, motor, servo, and
			// memory request types!
			return Integer.valueOf(((reply[1] << 8) & 0xFF00) | (reply[2] & 0xFF));
		}
		case ANALOG_SENSOR_SUBSCRIPTION: {
			paramsCheck(params, 2, Map.class, Number.class);
			analogSubscriptions = (Map<Analog.Listener, int[]>) params[0];
			byte count = ((Number) params[1]).byteValue();
			byte[] bitmask = new byte[(int) Math.ceil(count / 8d)];
			for (int i = 0; i < bitmask.length; i++)
				bitmask[i] = 0;
			for (int[] value : analogSubscriptions.values()) {
				for (int v : value)
					bitmask[bitmask.length - 1 - v / 8] |= (1 << (v % 8));
			}
			lowCommand(opcode, bitmask);
			break;
		}
		case ANALOG_PULLUP_ACTION: {
			paramsCheck(params, 2, List.class, Number.class);
			if (params.length < 2)
				throw new InvalidParameterException("Wrong number of parameters");
			byte count = ((Number) params[1]).byteValue();
			byte[] bitmask = new byte[32];
			for (int i = 0; i < bitmask.length; i++)
				bitmask[i] = (byte) 0x00;
			for (Integer port : (List<Integer>) params[0])
				bitmask[port / 8] |= (1 << (port % 8));
			byte[] send = new byte[(int) Math.ceil(count / 8d)];
			for (int i = 0; i < send.length; i++)
				send[i] = bitmask[send.length - i - 1];
			lowCommand(opcode, send);
			break;
		}
		case DIGITAL_SENSOR_REQUEST: {
			paramsCheck(params, 1, Number.class);
			if (params.length < 1)
				throw new InvalidParameterException("Wrong number of parameters");
			reply = lowCommand(opcode, ((Number) params[0]).byteValue());
			return Boolean.valueOf(reply[1] % 2 == 1);
		}
		case DIGITAL_SENSOR_SUBSCRIPTION: {
			paramsCheck(params, 2, Map.class, Number.class);
			digitalSubscriptions = (Map<Digital.Listener, int[]>) params[0];
			int count = ((Number) params[1]).byteValue();
			byte[] bitmask = new byte[(int) Math.ceil(count / 8d)];
			for (int i = 0; i < bitmask.length; i++)
				bitmask[i] = 0;
			for (int[] value : digitalSubscriptions.values()) {
				for (int v : value)
					bitmask[bitmask.length - 1 - v / 8] |= (1 << (v % 8));
			}
			lowCommand(opcode, bitmask);
			break;
		}
		case DIGITAL_PULLUP_ACTION: {
			paramsCheck(params, 2, List.class, Number.class);
			int count = ((Number) params[1]).byteValue();
			byte[] bitmask = new byte[32];
			for (int i = 0; i < bitmask.length; i++)
				bitmask[i] = (byte) 0x00;
			for (Integer port : (List<Integer>) params[0])
				bitmask[port / 8] |= (1 << (port % 8));
			byte[] send = new byte[(int) Math.ceil(count / 8d)];
			for (int i = 0; i < send.length; i++)
				send[i] = bitmask[send.length - i - 1];
			lowCommand(opcode, send);
			break;
		}
		case DIGITAL_OUTPUT_MODE_ACTION: {
			paramsCheck(params, 2, List.class, Number.class);
			int count = ((Number) params[1]).byteValue();
			byte[] bitmask = new byte[32];
			for (int i = 0; i < bitmask.length; i++)
				bitmask[i] = 0;
			for (Integer port : (List<Integer>) params[0])
				bitmask[port / 8] |= (1 << (port % 8));
			byte[] send = new byte[(int) Math.ceil(count / 8d)];
			for (int i = 0; i < send.length; i++)
				send[i] = bitmask[send.length - i - 1];
			lowCommand(opcode, send);
			break;
		}
		case DIGITAL_OUTPUT_LEVEL_ACTION: {
			paramsCheck(params, 2, Number.class, Boolean.class);
			lowCommand(opcode, ((Number) params[0]).byteValue(), (Boolean) params[1] ? (byte) 0x01 : (byte) 0x00);
			break;
		}
		case MOTOR_POWER_ACTION: {
			paramsCheck(params, 3, Number.class, Boolean.class, Number.class);
			lowCommand(opcode, ((Number) params[0]).byteValue(), (Boolean) params[1] ? (byte) 0x01 : (byte) 0x00,
					((Number) params[2]).byteValue());
			break;
		}
		case MOTOR_VELOCITY_ACTION: {
			paramsCheck(params, 3, Number.class, Boolean.class, Number.class);
			lowCommand(opcode, ((Number) params[0]).byteValue(), (Boolean) params[1] ? (byte) 0x01 : (byte) 0x00,
					((Number) params[2]).byteValue());
			break;
		}
		case MOTOR_POWER_ABSOLUTE_POSITION_ACTION: {
			paramsCheck(params, 3, Number.class, Number.class, Integer.class);
			int position = (Integer) params[2];
			lowCommand(opcode, ((Number) params[0]).byteValue(), ((Number) params[1]).byteValue(),
					(byte) (position >> 24), (byte) (position >> 16), (byte) (position >> 8), (byte) position);
			break;
		}
		case MOTOR_VELOCITY_ABSOLUTE_POSITION_ACTION: {
			paramsCheck(params, 3, Number.class, Number.class, Integer.class);
			int position = (Integer) params[2];
			lowCommand(opcode, ((Number) params[0]).byteValue(), ((Number) params[1]).byteValue(),
					(byte) (position >> 24), (byte) (position >> 16), (byte) (position >> 8), (byte) position);
			break;
		}
		case MOTOR_POWER_RELATIVE_POSITION_ACTION: {
			paramsCheck(params, 3, Number.class, Number.class, Integer.class);
			int position = (Integer) params[2];
			lowCommand(opcode, ((Number) params[0]).byteValue(), ((Number) params[1]).byteValue(),
					(byte) (position >> 24), (byte) (position >> 16), (byte) (position >> 8), (byte) position);
			break;
		}
		case MOTOR_VELOCITY_RELATIVE_POSITION_ACTION: {
			paramsCheck(params, 3, Number.class, Number.class, Integer.class);
			int position = (Integer) params[2];
			lowCommand(opcode, ((Number) params[0]).byteValue(), ((Number) params[1]).byteValue(),
					(byte) (position >> 24), (byte) (position >> 16), (byte) (position >> 8), (byte) position);
			break;
		}
		case MOTOR_FREEZE_ACTION: {
			paramsCheck(params, 1, Number.class);
			lowCommand(opcode, ((Number) params[0]).byteValue());
			break;
		}
		case MOTOR_BRAKE_ACTION: {
			paramsCheck(params, 2, Number.class, Number.class);
			lowCommand(opcode, ((Number) params[0]).byteValue(), ((Number) params[1]).byteValue());
			break;
		}
		case MOTOR_OFF_ACTION: {
			paramsCheck(params, 1, Number.class);
			lowCommand(opcode, ((Number) params[0]).byteValue());
			break;
		}
		case MOTOR_POSITION_REQUEST: {
			paramsCheck(params, 1, Number.class);
			reply = lowCommand(opcode, ((Number) params[0]).byteValue());
			return ((reply[1] << 24) & 0xFF000000) | ((reply[2] << 16) % 0xFF0000) | ((reply[3] << 8) & 0xFF00)
					| (reply[4] & 0xFF);
		}
		case MOTOR_POSITION_SUBSCRIPTION: {
			paramsCheck(params, 2, Map.class, Number.class);
			motorPositionSubscriptions = (Map<Motor.PositionListener, int[]>) params[0];
			int count = ((Integer) params[1]).byteValue();
			byte[] bitmask = new byte[(int) Math.ceil(count / 8d)];
			for (int i = 0; i < bitmask.length; i++)
				bitmask[i] = 0;
			for (int[] value : motorPositionSubscriptions.values()) {
				for (int v : value)
					bitmask[bitmask.length - 1 - v / 8] |= (1 << (v % 8));
			}
			lowCommand(opcode, bitmask);
			break;
		}
		case MOTOR_CLEAR_POSITION_ACTION: {
			paramsCheck(params, 1, Number.class);
			lowCommand(opcode, ((Number) params[0]).byteValue());
			break;
		}
		case MOTOR_VELOCITY_REQUEST: {
			paramsCheck(params, 1, Number.class);
			reply = lowCommand(opcode, ((Number) params[0]).byteValue());
			return reply[1] % 2 == 1 ? 0x000000FF & (-reply[2]) : 0x000000FF & reply[2];
		}
		case MOTOR_VELOCITY_SUBSCRIPTION: {
			paramsCheck(params, 2, Map.class, Number.class);
			motorVelocitySubscriptions = (Map<Motor.VelocityListener, int[]>) params[0];
			int count = ((Number) params[1]).byteValue();
			byte[] bitmask = new byte[(int) Math.ceil(count / 8d)];
			for (int i = 0; i < bitmask.length; i++)
				bitmask[i] = 0;
			for (int[] value : motorVelocitySubscriptions.values()) {
				for (int v : value)
					bitmask[bitmask.length - 1 - v / 8] |= (1 << (v % 8));
			}
			lowCommand(opcode, bitmask);
			break;
		}
		case SERVO_ONOFF_ACTION: {
			paramsCheck(params, 2, Number.class, Boolean.class);
			lowCommand(opcode, ((Number) params[0]).byteValue(), (Boolean) params[1] ? (byte) 0x01 : (byte) 0x00);
			break;
		}
		case SERVO_DRIVE_ACTION: {
			paramsCheck(params, 2, Number.class, Number.class);
			lowCommand(opcode, ((Number) params[0]).byteValue(), ((Number) params[1]).byteValue());
			break;
		}
		case CONTROLLER_BATTERY_CHARGE_REQUEST: {
			reply = lowCommand(opcode);
			return (int) reply[0];
		}
		case CONTROLLER_BATTERY_CHARGING_STATE_REQUEST: {
			reply = lowCommand(opcode);
			return Boolean.valueOf(reply[0] % 2 == 1);
		}
		case HW_CONTROLLER_OFF_ACTION: {
			lowCommand(opcode);
			break;
		}
		case HW_CONTROLLER_RESET_ACTION: {
			lowCommand(opcode);
			break;
		}
		case SW_CONTROLLER_OFF_ACTION: {
			lowCommand(opcode);
			break;
		}
		case SW_CONTROLLER_RESET_ACTION: {
			lowCommand(opcode);
			break;
		}
		case ERROR_ACTION: {
			paramsCheck(params, 2, Number.class, Number.class);
			lowCommand(opcode, ((Number) params[0]).byteValue(), ((Number) params[1]).byteValue());
			break;
		}
		case CUSTOM_ACTION: {
			break;
		}
		case HW_CONTROLLER_TYPE_REQUEST: {
			reply = lowCommand(opcode);
			return (int) reply[0];
		}
		case SW_CONTROLLER_TYPE_REQUEST: {
			reply = lowCommand(opcode);
			return (int) reply[0];
		}
		case HW_CONTROLLER_SET_MEMORY_ACTION: {
			paramsCheck(params, 2, Number.class, byte[].class);
			byte[] data = (byte[]) params[1];
			byte[] payload = new byte[1 + data.length];
			payload[0] = ((Number) params[0]).byteValue();
			System.arraycopy(data, 0, payload, 1, data.length);
			lowCommand(opcode, payload);
			break;
		}
		case CONTROLLER_AUTHENTICATE_REQUEST: {
			paramsCheck(params, 1, String.class);
			String name = (String) params[0];
			reply = lowCommand(opcode, name.getBytes());
			return Boolean.valueOf(reply[0] % 2 == 1);
		}
		case HW_CONTROLLER_GET_MEMORY_REQUEST: {
			paramsCheck(params, 1, Number.class);
			reply = lowCommand(opcode, ((Number) params[0]).byteValue());
			byte[] data = new byte[reply.length - 1];
			System.arraycopy(reply, 1, data, 0, data.length);
			return data;
		}
		case PROGRAM_COMPILE_REQUEST: {
			paramsCheck(params, 3, String.class, Integer.class, String.class);
			String code = (String) params[2];
			byte[] send = new byte[34 + code.length()];
			System.arraycopy(((String) params[0]).getBytes(), 0, send, 0, ((String) params[0]).length());
			for (int i = ((String) params[0]).length(); i < 32; i++)
				send[i] = ' ';
			send[32] = (byte) ((Integer) params[1] >> 8);
			send[33] = (byte) ((Integer) params[1]).byteValue();
			System.arraycopy(code.getBytes(), 0, send, 34, code.length());
			reply = lowCommand(opcode, send);
			String compMsg = "";
			if(reply.length - 1 > 0) {
				byte[] message = new byte[reply.length - 1];
				System.arraycopy(reply, 1, message, 0, message.length);
				compMsg = new String(message);
			}
			return new Object[] { reply[0] & 0xFF, compMsg };
		}
		case PROGRAM_EXECUTE_ACTION: {
			paramsCheck(params, 2, String.class, Integer.class);
			byte[] send = new byte[34];
			System.arraycopy(((String) params[0]).getBytes(), 0, send, 0, ((String) params[0]).length());
			for (int i = ((String) params[0]).length(); i < 32; i++)
				send[i] = ' ';
			send[32] = (byte) ((Integer) params[1] >> 8);
			send[33] = (byte) ((Integer) params[1]).byteValue();
			lowCommand(opcode, send);
			break;
		}
		case PROGRAM_COMPILE_EXECUTE_REQUEST: {
			paramsCheck(params, 3, String.class, Integer.class, String.class);
			String code = (String) params[2];
			byte[] send = new byte[34 + code.length()];
			System.arraycopy(((String) params[0]).getBytes(), 0, send, 0, ((String) params[0]).length());
			for (int i = ((String) params[0]).length(); i < 32; i++)
				send[i] = ' ';
			send[32] = (byte) ((Integer) params[1] >> 8);
			send[33] = (byte) ((Integer) params[1]).byteValue();
			System.arraycopy(code.getBytes(), 0, send, 34, code.length());
			reply = lowCommand(opcode, send);
			byte[] message = new byte[reply.length - 1];
			System.arraycopy(reply, 1, message, 0, message.length);
			return new Object[] { reply[0] & 0xFF, new String(send) };
		}
		case PROGRAMS_FETCH_SUBSCRIPTION: {
			lowCommand(opcode);
			break;
		}
		case EXECUTION_STATE_REQUEST: {
			reply = lowCommand(opcode);
			byte[] name = new byte[32];
			System.arraycopy(reply, 1, name, 0, 32);
			return new Object[] { (int) reply[0], new String(name),
					Integer.valueOf(((reply[33] << 8) & 0xFF00) | (reply[35] & 0xFF)) };
		}
		default: {
			break;
		}
		}
		return Boolean.TRUE; // TODO why?
	}

	public static void received(byte opcode, byte[] payload, InetAddress source) {
		try {
			switch (opcode) {
			case ANALOG_SENSOR_UPDATE: {
				if (payload.length % 3 != 0) {
					command(ERROR_ACTION, ERRORCODE_PAYLOAD_LENGTH_OUT_OF_RANGE, ANALOG_SENSOR_UPDATE);
					return;
				}
				Map<Integer, Integer> values = new HashMap<Integer, Integer>();
				for (int i = 0; i < payload.length; i += 3) {
					int port = payload[i];
					int value = ((payload[i + 1] << 8) & 0xFF00) | (payload[i + 2] & 0xFF);
					values.put(port, value);
					for (ComponentListener listener : ComponentListener._l_component)
						listener.analogReading(port, value);
				}
				iterateListeners: for (Entry<Analog.Listener, int[]> entry : analogSubscriptions.entrySet()) {
					int[] vals = new int[entry.getValue().length];
					for (int i = 0; i < vals.length; i++) {
						if (values.get(entry.getValue()[i]) == null)
							continue iterateListeners;
						vals[i] = values.get(entry.getValue()[i]);
					}
					entry.getKey().update(vals);
				}
				break;
			}
			case DIGITAL_SENSOR_UPDATE: {
				if (payload.length % 2 != 0) {
					command(ERROR_ACTION, ERRORCODE_PAYLOAD_LENGTH_OUT_OF_RANGE, DIGITAL_SENSOR_UPDATE);
					return;
				}
				Map<Integer, Integer> values = new HashMap<Integer, Integer>();
				for (int i = 0; i < payload.length; i += 2) {
					int port = payload[i];
					int value = payload[i + 1];
					values.put(port, value);
					for (ComponentListener listener : ComponentListener._l_component)
						listener.digitalReading(port, value == 1);
				}
				iterateListeners: for (Entry<Digital.Listener, int[]> entry : digitalSubscriptions.entrySet()) {
					boolean[] vals = new boolean[entry.getValue().length];
					for (int i = 0; i < vals.length; i++) {
						if (values.get(entry.getValue()[i]) == null)
							continue iterateListeners;
						vals[i] = values.get(entry.getValue()[i]) == 1;
					}
					entry.getKey().update(vals);
				}
				break;
			}
			case MOTOR_POSITION_UPDATE: {
				if (payload.length % 5 != 0) {
					command(ERROR_ACTION, ERRORCODE_PAYLOAD_LENGTH_OUT_OF_RANGE, MOTOR_POSITION_UPDATE);
					return;
				}
				Map<Integer, Integer> values = new HashMap<Integer, Integer>();
				for (int i = 0; i < payload.length; i += 5) {
					int port = payload[i];
					int value = ((payload[i + 1] << 24) & 0xFF000000) | ((payload[i + 2] << 16) & 0xFF0000)
							| ((payload[i + 3] << 8) & 0xFF00) | (payload[i + 4] & 0xFF);
					values.put(port, value);
					for (ComponentListener listener : ComponentListener._l_component)
						listener.motorGetPosition(port, value);
				}
				iterateListeners: for (Entry<Motor.PositionListener, int[]> entry : motorPositionSubscriptions
						.entrySet()) {
					int[] vals = new int[entry.getValue().length];
					for (int i = 0; i < vals.length; i++) {
						if (values.get(entry.getValue()[i]) == null)
							continue iterateListeners;
						vals[i] = values.get(entry.getValue()[i]);
					}
					entry.getKey().update(vals);
				}
				break;
			}
			case MOTOR_VELOCITY_UPDATE: {
				if (payload.length % 3 != 0) {
					command(ERROR_ACTION, ERRORCODE_PAYLOAD_LENGTH_OUT_OF_RANGE, MOTOR_VELOCITY_UPDATE);
					return;
				}
				Map<Integer, Integer> values = new HashMap<Integer, Integer>();
				for (int i = 0; i < payload.length; i += 3) {
					int port = payload[i];
					boolean direction = payload[i + 1] % 2 == 1;
					int value = 0x000000FF & payload[i + 2];
					values.put(port, direction ? -value : value);
					for (ComponentListener listener : ComponentListener._l_component)
						listener.motorGetVelocity(port, direction ? -value : value);
				}
				iterateListeners: for (Entry<Motor.VelocityListener, int[]> entry : motorVelocitySubscriptions
						.entrySet()) {
					int[] vals = new int[entry.getValue().length];
					for (int i = 0; i < vals.length; i++) {
						if (values.get(entry.getValue()[i]) == null)
							continue iterateListeners;
						vals[i] = values.get(entry.getValue()[i]);
					}
					entry.getKey().update(vals);
				}
				break;
			}
			case MOTOR_POSITION_REACHED_ACTION: {
				int port = (int) payload[0];
				Motor.getInstance(port).positionReached();
				break;
			}
			case CONTROLLER_BATTERY_UPDATE: {
				for (ComponentListener l : ComponentListener._l_component) {
					l.controllerChargingState(payload[0] != 0);
					l.controllerCharge(payload[1] & 0xFF);
				}
				break;
			}
			case PHONE_SENSOR_REQUEST: {
				for (int i = 0; i < externalHandlers.size(); externalHandlers.get(i).handleCommand(opcode, payload[0]))
					;
				break;
			}
			case PHONE_SENSOR_AVAILABILITY_REQUEST: {
				for (int i = 0; i < externalHandlers.size(); externalHandlers.get(i).handleCommand(opcode))
					;
				break;
			}
			case PHONE_CAMERA_TAKE_PICTURE_ACTION: {
				break;
			}
			case PHONE_CAMERA_GET_BLOB_COUNT_REQUEST: {
				break;
			}
			case PHONE_CAMERA_GET_BLOB_REQUEST: {
				break;
			}
			case PHONE_CAMERA_SET_CHANNEL_ACTION: {
				break;
			}
			case PHONE_OFF_ACTION: {
				break;
			}
			case PHONE_RESET_ACTION: {
				break;
			}
			case ERROR_ACTION: {
				break;
			}
			case CUSTOM_ACTION: {
				break;
			}
			case PHONE_TYPE_REQUEST: {
				for (int i = 0; i < externalHandlers.size(); externalHandlers.get(i).handleCommand(opcode))
					;
				break;
			}
			case ENVIRONMENT_SCAN_SUBSCRIPTION: {
				break;
			}
			case ENVIRONMENT_SCAN_HW_CONTROLLER_UPDATE: {
				int type = payload[0];
				byte[] nameBytes = new byte[32];
				System.arraycopy(payload, 1, nameBytes, 0, nameBytes.length);
				for (StateListener listener : StateListener._l_state)
					listener.scanUpdate(new HardwareController(source, type, new String(nameBytes).trim()));
			}
			case DEBUG_INFORMATION_UPDATE: {
				// TODO don't forget the eventlisteners
				break;
			}
			case PROGRAMS_FETCH_UPDATE: {
				if (payload.length < 34) {
					command(ERROR_ACTION, ERRORCODE_PAYLOAD_LENGTH_OUT_OF_RANGE, PROGRAMS_FETCH_UPDATE);
					return;
				}
				byte[] name = new byte[32];
				byte[] code = new byte[payload.length - 34];
				System.arraycopy(payload, 0, name, 0, 32);
				System.arraycopy(payload, 34, code, 0, code.length);
				// TODO
//				for (DeploymentListener l : DeploymentListener._l_deployment)
//					l.fetchedProgram(new Program_old(new String(name).trim(), Integer.valueOf(((payload[32] << 8) & 0xFF00)
//							| (payload[33] & 0xFF)), new String(code)));
				break;
			}
			case PROGRAMS_FETCH_DONE_UPDATE: {
				for (DeploymentListener l : DeploymentListener._l_deployment)
					l.fetchedProgramsDone();
				break;
			}
			default: {
				// for opcodes this phone cannot handle (but are valid AXCP
				// opcodes)
				forward(opcode, payload);
				break;
			}
			}
		} catch (IOException ex) {
			for (StateListener listener : StateListener._l_state)
				listener.exceptionThrown(ex);
		}
	}
}
