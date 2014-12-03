package at.pria.osiris.osiris.communication.MessageProcessor;

import android.util.Log;
import api.Axis;
import api.Robotarm;
import at.pria.osiris.osiris.sensors.SensorRefreshable;

/**
 * @author Ari Ayvazyan, Samuel Schmidt
 * @version 03.Dec.14
 */
public class StringProcessor implements MessageProcessor {
    private Robotarm robotarm;
    private SensorRefreshable sensorRefresher;

    public StringProcessor(Robotarm robotarm) {
        this.robotarm = robotarm;
    }

    /**
     * Changes Sensor Values in the GUI
     *
     * @param message the message
     */
    private void processMessage(String message) {
    Log.d("OSIRIS_DEBUG_MESSAGES", "Reached callMethod " + message);
    String[] splitted = message.split("/");
    for (int i = 0; i < splitted.length; i++) {
        //Log.d("OSIRIS_DEBUG_NETWORK", "Splitted" + i + ": " + splitted[i]);
    }

    /*
     * Info for the sensors
     *
     * sensor0 == sensor on the port0 and so on
     *
     */
        //Sensor 1
        if (splitted[0].equals("sensor1") && splitted.length == 2) {
            try {
                sensorRefresher.refresh(splitted[1], "sensor1");
            } catch (NumberFormatException nfe) {
                // TODO Auto-generated catch block
                nfe.printStackTrace();
            }
        }

        //Sensor 2
        if (splitted[0].equals("sensor2") && splitted.length == 2) {
            try {
                sensorRefresher.refresh(splitted[1], "sensor2");
            } catch (NumberFormatException nfe) {
                // TODO Auto-generated catch block
                nfe.printStackTrace();
            }
        }
    }

    @Override
    public void processMessage(Object message) {
        if (message instanceof String) processMessage((String) message);
    }
}
