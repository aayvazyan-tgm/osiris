package at.pria.osiris.linker.controllers.components.Axes;

import at.pria.osiris.linker.controllers.components.systemDependent.Servo;

/**
 * @author Ari Michael Ayvazyan
 * @version 21.02.2015
 */
public abstract class ServoAxis extends Axis {
    private Servo servo;
    private int servoPosition = -1; //-1 is a undefined state

    public ServoAxis(String axisName, Servo servo) {
        super(axisName);
        this.servo = servo;
    }

    /**
     * @see Axis
     */
    public void moveToPosition(int position) {
        servo.moveToAngle(position);
        servoPosition = position;
    }

    /**
     * @see Axis
     */
    public void moveAtPower(int power) {
        //TODO This one will be tricky...
    }

    /**
     * @see Axis
     */
    @Override
    public int getSensorValue() {
        return servoPosition;
    }


}
