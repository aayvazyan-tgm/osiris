package at.pria.osiris.osiris.sensors;

import java.io.IOException;
import java.io.ObjectInputStream;

import android.util.Log;
import api.Stoppable;
import at.pria.osiris.osiris.network.DirtyClientMessageProcessor;
import at.pria.osiris.osiris.network.RemoteRobotarm;

/**
 * Created by helmuthbrunner on 10/11/14.
 */
public class SensorRefresher implements Runnable, Stoppable {

    private RemoteRobotarm remoteRobotarm;
    private SensorRefreshable sensorRefreshable;
    private boolean running;

    public SensorRefresher(RemoteRobotarm remoteRobotarm, SensorRefreshable sensorRefreshable) {
        this.remoteRobotarm = remoteRobotarm;
        this.sensorRefreshable = sensorRefreshable;
        running = true;
    }

    @Override
    public void run() {
        DirtyClientMessageProcessor cmp = new DirtyClientMessageProcessor(remoteRobotarm, this);
        while (running) {
            //convert ObjectInputStream object to String
            String message = "failmessage";
            ObjectInputStream ois = remoteRobotarm.getOis();
            try {
                Log.d("OSIRIS_DEBUG_MESSAGES", "Reached waitForMessage");
                message = (String) ois.readObject();
                Log.d("OSIRIS_DEBUG_MESSAGES","Reached gotMessage");
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Closing connection...");
                running = false;
            }
            System.out.println("Message Received: " + message);

            cmp.callMethod(message);
        }
    }

    @Override
    public void stop() {
        running = false;
    }

    public void refresh(String s) {
        this.sensorRefreshable.refresh(Double.parseDouble(s));
        Log.d("OSIRIS_DEBUG_MESSAGES","SentRefresh");
    }
}
