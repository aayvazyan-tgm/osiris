package at.pria.osiris.linker.controllers.components;

import at.pria.osiris.linker.controllers.components.systemDependent.SensorAnalog;
import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;

/**
 * A class which represents a potentiometer
 *
 * @author Helmuth Brunner
 * @version 2015-01-26
 */
public class Potentiometer {

	private SensorAnalog sensor;

	public Potentiometer(SensorAnalog sensorAnalog) {
		sensor= sensorAnalog;
	}

	/**
	 * Get the current position of the poti, no degree.
	 *
	 * returns 0.0 if a exception accord
	 *
	 * @return the position
	 */
	public double getAngle() {
		double result=0.0;
		try {

			result= sensor.getCurrentValue();

		} catch (NotConnectedException e) {
			e.printStackTrace(); // TODO handle this exception
		} catch (RequestTimeoutException e) {
			e.printStackTrace(); // TODO handle this exception
		}
		return result;
	}

}
