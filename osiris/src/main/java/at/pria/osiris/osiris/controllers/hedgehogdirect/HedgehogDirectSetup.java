package at.pria.osiris.osiris.controllers.hedgehogdirect;

import org.andrix.listeners.ExecutionListener;

import java.io.IOException;

import api.Robotarm;
import at.pria.osiris.osiris.communication.DataListener;
import at.pria.osiris.osiris.communication.messageProcessor.SensorValueResponseProcessor;
import at.pria.osiris.osiris.communication.messageProcessor.StringProcessor;
import at.pria.osiris.osiris.controllers.ConnectionNotEstablishedException;
import at.pria.osiris.osiris.controllers.Controller;
import at.pria.osiris.osiris.controllers.ControllerSetup;
import at.pria.osiris.osiris.controllers.hedgehog.HedgehogSetup;
import at.pria.osiris.osiris.controllers.hedgehog.MyStateListener;
import at.pria.osiris.osiris.controllers.hedgehog.OldLinkerHedgehogRemoteRobotarm;
import at.pria.osiris.osiris.sensors.SensorRefreshable;
import at.pria.osiris.osiris.view.TableSensorValuesFragment;

/**
 * Smartphone --> Hedgehog
 * Sets up the necessary configuration for the communication
 * @author Samuel Schmidt
 * @version 3/10/2015
 */
public class HedgehogDirectSetup implements ControllerSetup {
    private DataListener dl;

    @Override
    public void setup(Robotarm robotarm) {
        //Add a Connection Listener that connects to the controller
        MyStateListener._l_state.add(new MyStateListener());

        //We add the ExecutionListener to listen for events from the controller
        dl = new DataListener();
        //We add the required EventHandlers
        SensorRefreshable sensorRefreshable = TableSensorValuesFragment.getInstance(1, robotarm);

        // TODO add a way to directly send Sensor/moveAxis/.. Requests
//        dl.addMessageProcessor(new StringProcessor(sensorRefreshable));
//        dl.addMessageProcessor(new SensorValueResponseProcessor(sensorRefreshable));


        //Set the listener in Hedgehog
        ExecutionListener._l_exec.add(dl);
    }

    @Override
    public DataListener getDataListener() {
        return dl;
    }
}
