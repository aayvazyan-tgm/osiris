package oldLinker.control;

import api.Axis;
import api.Stoppable;
import oldLinker.model.RobotarmImpl;
import oldLinker.util.AXCPWrapper;

import java.io.IOException;

/**
 * This simple thread implementation sends sensordata in a 1s intervall (to the client)
 * Created by Samuel on 03.11.2014.
 */
public class SensorMessenger implements Runnable, Stoppable {

    private RobotarmImpl robotarm;
    private boolean running;

    public SensorMessenger(RobotarmImpl ra) {
        this.robotarm = ra;
        running = true;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(10);
                try {
                    if(robotarm==null) System.out.println("Robotarm is null");
                    String toSend="sensor0/" + robotarm.getAxis(Axis.BASE).getSensor().getValue();
                    AXCPWrapper.sendData(toSend);
                    toSend="sensor1/" + robotarm.getAxis(Axis.AXISONE).getSensor().getValue();
                    AXCPWrapper.sendData(toSend);
                    toSend="sensor2/" + robotarm.getAxis(Axis.AXISTWO).getSensor().getValue();
                    AXCPWrapper.sendData(toSend);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void stop() {
        running = false;
    }
}
