package at.pria.osiris.linker.implementation.hedgehog.axes;

import at.pria.osiris.linker.controllers.components.Axes.Axis;
import at.pria.osiris.linker.controllers.components.Axes.MotorSensorAxis;
import at.pria.osiris.linker.controllers.components.systemDependent.Motor;
import at.pria.osiris.linker.implementation.hedgehog.components.HedgehogMotor;
import org.andrix.low.NotConnectedException;

/**
 * @author Ari Michael Ayvazyan
 * @version 16.02.2015
 */
public class VerticalAxis extends MotorSensorAxis {
    public VerticalAxis() throws NotConnectedException {
        super("VerticalAxis",new HedgehogMotor(3),null);
    }
}
