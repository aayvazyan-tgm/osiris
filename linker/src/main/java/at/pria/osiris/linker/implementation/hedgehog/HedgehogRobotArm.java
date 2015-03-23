package at.pria.osiris.linker.implementation.hedgehog;

import at.pria.osiris.linker.communication.messageProcessors.MessageProcessor;
import at.pria.osiris.linker.controllers.RobotArm;
import at.pria.osiris.linker.controllers.components.Axes.Axis;
import at.pria.osiris.linker.controllers.components.Axes.ServoAxis;
import at.pria.osiris.linker.implementation.hedgehog.communication.HedgehogCommunicationInterface;
import at.pria.osiris.linker.implementation.hedgehog.components.HedgehogDoubleServo;
import at.pria.osiris.linker.implementation.hedgehog.components.HedgehogSensorAnalog;
import at.pria.osiris.linker.implementation.hedgehog.components.HedgehogServo;
import org.andrix.low.NotConnectedException;

import java.util.ArrayList;

/**
 * @author Ari Michael Ayvazyan
 * @version 15.02.2015
 */
public class HedgehogRobotArm extends RobotArm {

    private final ArrayList<Axis> axes;

    public HedgehogRobotArm(MessageProcessor messageProcessor) {
        //Add the Communication Interface
        super(new HedgehogCommunicationInterface(), messageProcessor);
        //Add the axes
        this.axes = new ArrayList<Axis>();
        try {
            this.axes.add(new ServoAxis("BaseAxis", new HedgehogServo(1, 720, 2, 6)));
            this.axes.add(new ServoAxis("VerticalAxis", new HedgehogDoubleServo(2, 3, 60, 3, 6)));
            this.axes.add(new ServoAxis("HorizontalAxis", new HedgehogServo(4, 360, 3, 6)));
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Axis[] getAvailableAxes() {
        return axes.toArray(new Axis[axes.size()]);
    }

    @Override
    public Axis getAxis(int i) {
        return this.axes.get(i);
    }

    @Override
    public int getSensorValue(int sensorPort) {
        try {
            return new HedgehogSensorAnalog(sensorPort).getCurrentValue();
        } catch (Exception ignored) {

        }
        return -1;
    }
}
