package at.pria.osiris.osiris.network;

import api.Robotarm;
import at.pria.osiris.osiris.sensors.SensorRefresher;

/**
 * Created by Samuel on 29.10.2014.
 */
public class DirtyClientMessageProcessor {

    private Robotarm robotarm;
    private SensorRefresher sensorRefresher;


    public DirtyClientMessageProcessor(Robotarm robotarm, SensorRefresher sensorRefresher) {
        this.robotarm = robotarm;
        this.sensorRefresher = sensorRefresher;
    }

    /**
     * Changes Sensor Values in the GUI
     *
     * @param message the message
     */
    public void callMethod(String message) {
        String[] splitted = message.split("/");
        for (int i = 0; i < splitted.length; i++) {
            System.out.println("Splitted" + i + ": " + splitted[i]);
        }
        if (splitted[0].equals("sensor0") && splitted.length == 2) {
            try {
                sensorRefresher.refresh(splitted[1]);
            } catch (NumberFormatException nfe) {
                // TODO Auto-generated catch block
                nfe.printStackTrace();
            }
        }
    }
}

