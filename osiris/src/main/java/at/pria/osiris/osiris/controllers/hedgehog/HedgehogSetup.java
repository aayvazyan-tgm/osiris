package at.pria.osiris.osiris.controllers.hedgehog;

import api.Robotarm;
import at.pria.osiris.osiris.communication.DataListener;
import at.pria.osiris.osiris.communication.messageProcessor.SensorValueResponseProcessor;
import at.pria.osiris.osiris.communication.messageProcessor.StringProcessor;
import at.pria.osiris.osiris.controllers.ControllerSetup;
import at.pria.osiris.osiris.sensors.SensorRefreshable;
import at.pria.osiris.osiris.view.TableSensorValuesFragment;
import org.andrix.listeners.ExecutionListener;

/**
 * @author Ari Ayvazyan
 * @version 03.Dec.14
 */
public class HedgehogSetup implements ControllerSetup {
    private DataListener dl;

    @Override
    public void setup(Robotarm robotarm) {
        //Add a Connection Listener that connects to the controller
        MyStateListener._l_state.add(new MyStateListener());

        //We add the ExecutionListener to listen for events from the controller
        dl=new DataListener();
        //We add the required EventHandlers
        SensorRefreshable sensorRefreshable =TableSensorValuesFragment.getInstance(1, robotarm);
        dl.addMessageProcessor(new StringProcessor(sensorRefreshable));
        dl.addMessageProcessor(new SensorValueResponseProcessor(sensorRefreshable));
        //Set the listener in Hedgehog
        ExecutionListener._l_exec.add(dl);
    }

    @Override
    public DataListener getDataListener() {
        return dl;
    }
}
