package at.pria.osiris.linker.controllers.components.systemDependent;

import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;

/**
 * Created by helmuthbrunner on 26/01/15.
 */
public interface SensorDigital {

    public boolean getCurentValue() throws NotConnectedException, RequestTimeoutException;

}
