package at.pria.osiris.linker.implementation.hedgehog.axes;

import at.pria.osiris.linker.controllers.components.Axes.Axis;
import at.pria.osiris.linker.implementation.hedgehog.components.HedgehogServo;
import org.andrix.low.NotConnectedException;

/**
 * @author Ari Michael Ayvazyan
 * @version 16.02.2015
 */
public class VerticalAxis extends Axis {
    private final HedgehogServo hedgehogServo2;
    private final HedgehogServo hedgehogServo;
    private int currentPosition;

    public VerticalAxis() throws NotConnectedException {
        super("VerticalAxis");
        hedgehogServo = new HedgehogServo(2,360,2);
        hedgehogServo2 = new HedgehogServo(3,360,2);
    }

    @Override
    public int getSensorValue() {
        return currentPosition;
    }

    @Override
    public void moveToAngle(int angle) {
        //TODO this is not the angle!
        currentPosition = angle;
        hedgehogServo.moveToExactPosition(angle);
        hedgehogServo2.moveToExactPosition(hedgehogServo2.getMaximumAngle()-angle);
    }

    @Override
    public void moveAtPower(int power) {
        //TODO this is no moveAtPower function.
        currentPosition = power;
        hedgehogServo.moveToExactPosition(power);
        hedgehogServo2.moveToExactPosition(hedgehogServo2.getMaximumAngle()-power);
    }


}
