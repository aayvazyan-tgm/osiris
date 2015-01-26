package at.pria.osiris.linker.controllers.hedgehog;

import at.pria.osiris.linker.controllers.components.systemDependent.SensorDigital;
import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;
import org.andrix.sensors.Digital;

/**
 * A class to read the value from a digital Sensor
 *
 * Created by helmuthbrunner on 26/01/15.
 */
public class HedgehogSensorDigital implements SensorDigital {

    private Digital actualSensor;

    public HedgehogSensorDigital(int port) throws NotConnectedException {
        actualSensor= new Digital(port);
    }

    @Override
    public boolean getCurentValue() throws NotConnectedException, RequestTimeoutException {
        return actualSensor.getValue();
    }

}
