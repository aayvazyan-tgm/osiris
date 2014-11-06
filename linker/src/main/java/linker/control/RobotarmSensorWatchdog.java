package linker.control;

import api.Axis;
import api.Stoppable;
import linker.model.RobotarmImpl;
import linkjvm.Botball;

/**
 * Checks if the motors are moving within the defined sensormin/max values (if
 * not it stops the motor)
 *
 * @author Adrian Bergler, Christian Janeczek
 * @version 2014-10-25
 */
public class RobotarmSensorWatchdog implements Runnable, Stoppable {

    private boolean running;
    private RobotarmImpl robotarm;
    private Axis axis;
    private int msinterval;

    public RobotarmSensorWatchdog(RobotarmImpl robotarm, Axis axis,
                                  int msinterval) {
        this.robotarm = robotarm;
        this.axis = axis;
        this.msinterval = msinterval;
    }

    @Override
    public void run() {
        running = true;

        while (running) {

            // Sensor check
            if (robotarm.getAxis(axis).getSensor().getValue() <= axis.getMinimumAngle()
                    || robotarm.getAxis(axis).getSensor().getValue() >= axis
                    .getMaximumAngle()) {
                robotarm.stopAxis(axis);
            }

            Botball.msleep(msinterval);

        }
    }

    /**
     * Stops the Watchdog
     */
    @Override
    public void stop() {
        running = false;
    }
}
