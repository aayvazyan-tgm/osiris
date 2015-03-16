package at.pria.osiris.linker.controllers.components.Axes;

import at.pria.osiris.linker.controllers.components.systemDependent.Servo;

/**
 * @author Ari Michael Ayvazyan
 * @version 21.02.2015
 */
public class ServoAxis extends Axis {

    private final ServoHelper servoHelper;
    private Servo servo;

    public ServoAxis(String axisName, Servo servo) {
        super(axisName);
        this.servo = servo;
        this.servoHelper=new ServoHelper(this.servo,1);
    }

    /**
     * @see Axis
     */
    public synchronized void moveAtPower(int power) {
        servoHelper.moveAtPower(power);
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
