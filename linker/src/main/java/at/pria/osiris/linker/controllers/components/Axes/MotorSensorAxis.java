package at.pria.osiris.linker.controllers.components.Axes;

import at.pria.osiris.linker.controllers.components.systemDependent.Motor;
import at.pria.osiris.linker.controllers.components.systemDependent.Sensor;

/**
 * @author Ari Michael Ayvazyan
 * @version 21.02.2015
 */
public abstract class MotorSensorAxis extends Axis {
    private Motor motor;
    private Sensor sensor;
    private int servoPosition = -1; //-1 is a undefined state

    public MotorSensorAxis(String axisName, Motor motor, Sensor sensor) {
        super(axisName);
        this.motor = motor;
        this.sensor = sensor;
    }

    /**
     * @see at.pria.osiris.linker.controllers.components.Axes.Axis
     */
    public void moveToPosition(int position) {
        //TODO work with sensor values
    }

    /**
     * @see at.pria.osiris.linker.controllers.components.Axes.Axis
     */
    public void moveAtPower(int power) {
        motor.moveAtPower(power);
    }

    /**
     * @see at.pria.osiris.linker.controllers.components.Axes.Axis
     */
    @Override
    public int getSensorValue() {
        return sensor.getCurrentValue();
    }
}
