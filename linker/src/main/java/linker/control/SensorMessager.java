package linker.control;

import api.Axis;
import api.Stoppable;
import linker.model.RobotarmImpl;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * This simple thread implementation sends sensordata in a 1s intervall (to the client)
 * Created by Samuel on 03.11.2014.
 */
public class SensorMessager implements Runnable, Stoppable {

    private ObjectOutputStream oos;
    private RobotarmImpl robotarm;
    private boolean running;

    public SensorMessager(ObjectOutputStream oos, RobotarmImpl ra) {
        this.oos = oos;
        this.robotarm = ra;
        running = true;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(10);
                try {
                    // TODO SEND DATA via:
                    // AXCP.command(AXCP.EXECUTION_DATA_ACTION, "", 0, data)
                    oos.writeObject("sensor0/" + robotarm.getAxis(Axis.BASE).getSensor().getValue());
                    oos.writeObject("sensor1/" + robotarm.getAxis(Axis.AXISONE).getSensor().getValue());
                    oos.writeObject("sensor2/" + robotarm.getAxis(Axis.AXISTWO).getSensor().getValue());
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
