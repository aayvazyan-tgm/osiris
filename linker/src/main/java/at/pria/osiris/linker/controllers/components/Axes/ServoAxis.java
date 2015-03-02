package at.pria.osiris.linker.controllers.components.Axes;

import at.pria.osiris.linker.controllers.components.systemDependent.Servo;

/**
 * @author Ari Michael Ayvazyan
 * @version 21.02.2015
 */
public abstract class ServoAxis extends Axis {

    private Servo servo;

    public ServoAxis(String axisName, Servo servo) {
        super(axisName);
        this.servo = servo;
    }

    /**
     * @see Axis
     */
    public void moveAtPower(int power) {
        ServoHelper.pwm(this.servo,power);
    }

    /**
     * @see Axis
     */
    @Override
    public int getSensorValue() {
        return servo.getPosition();
    }

    public Servo getServo() {
        return servo;
    }

}
