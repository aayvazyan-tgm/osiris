package at.pria.osiris.linker.implementation.hedgehog.components;

import at.pria.osiris.linker.controllers.components.systemDependent.Sensor;
import org.andrix.low.NotConnectedException;
import org.andrix.sensors.Analog;

/**
 * A class to read the value from analog sensor.
 *
 * @author Ari Ayvazyan
 * @version 2015-01-26
 */
public class HedgehogSensorAnalog extends Analog implements Sensor {
    /**
     *
     * @param port the port to get the data from
     * @throws NotConnectedException
     */
    public HedgehogSensorAnalog(int port) throws NotConnectedException {
        super(port);
    }

    /**
     *
     * @return returns the current value of the sensor
     */
    @Override
    public int getCurrentValue() {
        try {
            return super.getValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
