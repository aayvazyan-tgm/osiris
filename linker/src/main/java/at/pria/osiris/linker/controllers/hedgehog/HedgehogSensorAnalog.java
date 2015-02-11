package at.pria.osiris.linker.controllers.hedgehog;

import at.pria.osiris.linker.controllers.components.systemDependent.SensorAnalog;

import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;
import org.andrix.sensors.Analog;

/**
 * A class to read the value from analog sensor.
 *
 * @author Helmuth Brunner
 * @version 2015-01-26
 */
public class HedgehogSensorAnalog implements SensorAnalog {

    private Analog actualSensor;

    public HedgehogSensorAnalog(int port) throws NotConnectedException {
        actualSensor= new Analog(port);
    }

    @Override
    public double getCurrentValue() throws NotConnectedException, RequestTimeoutException {
        return actualSensor.getValue();
    }

}
