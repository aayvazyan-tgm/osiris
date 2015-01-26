package at.pria.osiris.linker.controllers.hedgehog;

import at.pria.osiris.linker.controllers.components.systemDependent.Sensor;

import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;
import org.andrix.sensors.Analog;

/**
 * A class to access the sensor over hedgehog
 *
 * @author Helmuth Brunner
 * @version 2015-01-26
 */
public class HedgehogSensor implements at.pria.osiris.linker.controllers.components.systemDependent.Sensor {

    private Analog actualSensor;

    public HedgehogSensor(int port) throws NotConnectedException {
        actualSensor= new Analog(port);
    }

    @Override
    public double getCurentValue() throws NotConnectedException, RequestTimeoutException {
        return actualSensor.getValue();
    }

}
