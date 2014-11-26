package org.andrix.listeners;

import java.util.ArrayList;
import java.util.List;


public interface ComponentListener {
	
	public static List<ComponentListener> _l_component = new ArrayList<ComponentListener>();
	
	public void analogReading(int port, int value);

	public void analogPullup(List<Integer> portsEnabled);

	public void digitalReading(int port, boolean value);

	public void digitalPullup(List<Integer> portsEnabled);

	public void digitalSetMode(List<Integer> portsOutput);

	public void digitalOutput(int port, boolean level);

	public void motorPower(int port, int power);

	public void motorVelocity(int port, int velocity);

	public void motorPowerPosition(int port, int power, boolean absolute, int position);

	public void motorVelocityPosition(int port, int velocity, boolean absolute, int position);

	public void motorFreeze(int port);

	public void motorBrake(int port, int power);

	public void motorOff(int port);

	public void motorGetPosition(int port, int position);

	public void motorGetVelocity(int port, int velocity);

	public void motorPositionReached(int port);

	public void motorClearPosition(int port);

	public void servoOnOff(int port, boolean on);

	public void servoSetPosition(int port, int position);
	
	public void controllerCharge(int charge);

	public void controllerChargingState(boolean charging);
}
