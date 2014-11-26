/*
 * COPYRIGHT 2014 Christoph Krofitsch
 * Practical Robotics Institute Austria
 */

package org.andrix.deployment;

public enum FunctionInsert {

	SLEEP("sleep", ParameterType.UNSIGNED), ANALOG("analog", ParameterType.PORT), DIGITAL("digital", ParameterType.PORT), MOVE_AT_VELOCITY(
			"moveAtVelocity", ParameterType.PORT, ParameterType.SIGNED_8), MOVE_AT_POWER("moveAtPower",
			ParameterType.PORT, ParameterType.SIGNED_8), GET_VELOCITY("getVelocity", ParameterType.PORT), GET_POSITION(
			"getPosition", ParameterType.PORT), CLEAR_POSITION("clearPosition", ParameterType.PORT), MOVE_AT_POWER_TO_ABSOLUTE(
			"moveAtPowerToAbsolute", ParameterType.PORT, ParameterType.UNSIGNED_8, ParameterType.POSITION_ABS), MOVE_AT_VELOCITY_TO_ABSOLUTE(
			"moveAtVelocityToAbsolute", ParameterType.PORT, ParameterType.UNSIGNED_8, ParameterType.POSITION_ABS), MOVE_AT_POWER_TO_RELATIVE(
			"moveAtPowerToRelative", ParameterType.PORT, ParameterType.UNSIGNED_8, ParameterType.POSITION_REL), MOVE_AT_VELOCITY_TO_RELATIVE(
			"moveAtVelocityToRelative", ParameterType.PORT, ParameterType.UNSIGNED_8, ParameterType.POSITION_REL), OFF(
			"off", ParameterType.PORT), FREEZE("freeze", ParameterType.PORT), BRAKE("brake", ParameterType.PORT,
			ParameterType.UNSIGNED_8), SET_POSITION("setPosition", ParameterType.PORT, ParameterType.POSITION_ABS), ENABLE_SERVO(
			"enableServo", ParameterType.PORT), DISABLE_SERVO("disableServo", ParameterType.PORT), SET_ANALOG_PULLUPS(
			"setAnalogPullups", ParameterType.BOOLEAN, ParameterType.BOOLEAN, ParameterType.BOOLEAN,
			ParameterType.BOOLEAN, ParameterType.BOOLEAN, ParameterType.BOOLEAN, ParameterType.BOOLEAN,
			ParameterType.BOOLEAN, ParameterType.BOOLEAN, ParameterType.BOOLEAN, ParameterType.BOOLEAN,
			ParameterType.BOOLEAN, ParameterType.BOOLEAN, ParameterType.BOOLEAN, ParameterType.BOOLEAN,
			ParameterType.BOOLEAN), SET_DIGITAL_PULLUPS("setDigitalPullups", ParameterType.BOOLEAN,
			ParameterType.BOOLEAN, ParameterType.BOOLEAN, ParameterType.BOOLEAN, ParameterType.BOOLEAN,
			ParameterType.BOOLEAN, ParameterType.BOOLEAN, ParameterType.BOOLEAN, ParameterType.BOOLEAN,
			ParameterType.BOOLEAN, ParameterType.BOOLEAN, ParameterType.BOOLEAN, ParameterType.BOOLEAN,
			ParameterType.BOOLEAN, ParameterType.BOOLEAN, ParameterType.BOOLEAN), ALL_OFF("allOff"), ENABLE_ALL_SERVOS(
			"enableAllServos"), DISABLE_ALL_SERVOS("disableAllServos");

	public String functionName;
	private ParameterType[] params;

	private FunctionInsert(String functionName, ParameterType... params) {
		this.functionName = functionName;
		this.params = params;
	}

	public ParameterType getParamType(int nr) {
		if (nr >= params.length || nr < 0)
			throw new AssertionError("param number out of range!");
		return params[nr];
	}

	public int getParamNumber() {
		return params.length;
	}
}
