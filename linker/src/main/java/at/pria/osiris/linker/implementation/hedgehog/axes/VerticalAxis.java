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
    private int curentPosition;

    public VerticalAxis() throws NotConnectedException {
        super("VerticalAxis");
        hedgehogServo = new HedgehogServo(2);
        hedgehogServo2 = new HedgehogServo(3);
    }

    @Override
    public int getSensorValue() {
        return curentPosition;
    }

    @Override
    public void moveToPosition(int position) {
        curentPosition = position;
        hedgehogServo.moveToAngle(position);
        hedgehogServo2.moveToAngle(position);
    }

    @Override
    public void moveAtPower(int power) {
        //TODO Pfui :stinky:
        curentPosition = power;
        hedgehogServo.moveToAngle(power);
        hedgehogServo2.moveToAngle(power);
    }
}
