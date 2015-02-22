package at.pria.osiris.linker.implementation.hedgehog.axes;

import at.pria.osiris.linker.controllers.components.Axes.MotorSensorAxis;
import at.pria.osiris.linker.implementation.hedgehog.components.HedgehogMotor;
import org.andrix.low.NotConnectedException;

/**
 * @author Ari Michael Ayvazyan
 * @version 16.02.2015
 */
public class HorizontalAxis extends MotorSensorAxis {
    public HorizontalAxis() throws NotConnectedException {
        super("HorizontalAxis", new HedgehogMotor(2), null);
    }
}
