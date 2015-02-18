package at.pria.osiris.osiris.communication.messageProcessor;

import android.util.Log;
import api.Robotarm;
import at.pria.osiris.osiris.sensors.SensorRefreshable;
import messages.responses.SensorValueResponse;

/**
 * @author Ari Ayvazyan, Samuel Schmidt
 * @version 03.Dec.14
 */
public class SensorValueResponseProcessor implements MessageProcessor {
    private Robotarm robotarm;
    private SensorRefreshable sensorRefresher;

    public SensorValueResponseProcessor(Robotarm robotarm, SensorRefreshable sensorRefresher) {
        this.robotarm = robotarm;
        this.sensorRefresher=sensorRefresher;
    }

    /**
     * Changes Sensor Values in the GUI
     *
     * @param message the message
     */
    private void processMessage(SensorValueResponse message) {
        Log.d("OSIRIS_DEBUG_MESSAGES", "Received a SensorValue");
        sensorRefresher.refresh(message.getSensorValue(),"Sensor "+message.getSensorNumber());
    }

    @Override
    public void processMessage(Object message) {
        if (message instanceof SensorValueResponse) processMessage((SensorValueResponse) message);
    }
}
