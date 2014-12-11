package at.pria.osiris.osiris.controllers2.bedgehog;

import org.andrix.listeners.StateListener;
import org.andrix.low.ConnectionState;
import org.andrix.low.HardwareController;
import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;

/**
 * @author Ari Ayvazyan
 * @version 03.Dec.14
 */
public class MyStateListener implements StateListener{

    private ConnectionState connectionState = ConnectionState.DISCONNECTED;
    @Override
    public void connectionStateChange(ConnectionState connectionState, HardwareController hardwareController) {
        if(connectionState == ConnectionState.CONNECTED_NOAUTH){
            try {
                hardwareController.authenticate("");
            } catch (NotConnectedException e) {
                e.printStackTrace();
            } catch (RequestTimeoutException e) {
                e.printStackTrace();
            }
        } else if(connectionState == ConnectionState.CONNECTED_AUTH){
            // loslegen commands zu senden
        }
        this.connectionState = connectionState;
    }

    @Override
    public void scanUpdate(HardwareController hardwareController) {
        if(connectionState == ConnectionState.DISCONNECTED)
        hardwareController.connect();
    }

    @Override
    public void exceptionThrown(Exception e) {
        e.printStackTrace();
    }
}
