package at.pria.osiris.linker.controllers.components.Axes;

import at.pria.osiris.linker.controllers.components.systemDependent.Servo;

/**
 * @author Ari Michael Ayvazyan
 * @version 21.02.2015
 */
public class ServoAxis extends Axis {

    private Servo servo;

    public ServoAxis(String axisName, Servo servo) {
        super(axisName);
        this.servo = servo;
    }

    /**
     * @see Axis
     */
    public synchronized void moveAtPower(int power) {
        ServoHelper.pwm(this.servo, power, 100);
    }

    /**
     * @see Axis
     */
    @Override
    public int getSensorValue() {
        return servo.getPositionInDegrees();
    }

    public Servo getServo() {
        return servo;
    }

    @Override
    public void moveToAngle(int angle) {
        this.servo.moveToAngle(angle);
    }

    public int getMaximumAngle() {
        return servo.getMaximumAngle();
    }
}
