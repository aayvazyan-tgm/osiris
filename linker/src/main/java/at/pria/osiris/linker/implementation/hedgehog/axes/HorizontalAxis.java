package at.pria.osiris.linker.implementation.hedgehog.axes;

import at.pria.osiris.linker.controllers.components.Axes.MotorSensorAxis;
import at.pria.osiris.linker.controllers.components.Axes.ServoAxis;
import at.pria.osiris.linker.implementation.hedgehog.components.HedgehogMotor;
import at.pria.osiris.linker.implementation.hedgehog.components.HedgehogServo;
import org.andrix.low.NotConnectedException;

/**
 * @author Ari Michael Ayvazyan
 * @version 16.02.2015
 */
public class HorizontalAxis extends ServoAxis {
    public HorizontalAxis() throws NotConnectedException {
        super("HorizontalAxis", new HedgehogServo(4));
    }
}
