package at.pria.osiris.osiris.controllers.hedgehog;

import android.util.Log;
import org.andrix.listeners.StateListener;
import org.andrix.low.ConnectionState;
import org.andrix.low.HardwareController;
import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ari Ayvazyan
 * @version 03.Dec.14
 */
public class MyStateListener implements StateListener{

    private Map<InetAddress, HardwareController> nearControllers = new HashMap<InetAddress, HardwareController>();
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
    public void scanUpdate(HardwareController hwc) {
        if (!nearControllers.containsKey(hwc.address)) {
            nearControllers.put(hwc.address, hwc);
            Log.d("DBG","received new controller");
            if (!hwc.connect())
                return;
            try {
                if (!hwc.authenticate(""));
            } catch (IOException ex) {
                System.out.println(ex);
//                loger.warn("Default authenticate against controller failed!", ex);
            }
        } else {
            nearControllers.get(hwc.address).lastUpdate = System.currentTimeMillis();
        }
//        if(connectionState == ConnectionState.DISCONNECTED)
//        hardwareController.connect();
    }

    @Override
    public void controllerCharge(int i) {

    }

    @Override
    public void controllerChargingState(boolean b) {

    }
}
