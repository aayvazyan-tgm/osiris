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

    public HedgehogRobotArm(MessageProcessor messageProcessor,boolean useSerialConnection) {
        //Add the Communication Interface
        super(new HedgehogCommunicationInterface(useSerialConnection), messageProcessor);
        //Add the axes
        this.axes = new ArrayList<Axis>();
        try {
            this.axes.add(new ServoAxis("BaseAxis", new HedgehogServo(1, 15, 0, 720, 2, 2)));
            this.axes.add(new ServoAxis("VerticalAxis", new HedgehogDoubleServo(2, 3, 25, 0, 60, 3, 2)));
            this.axes.add(new ServoAxis("HorizontalAxis", new HedgehogServo(4, 75, 0, 180, 3, 2)));
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

    @Override
    public void moveSynchronized(ArrayList<Double> angle) {
        for(int z = 0; z < angle.size() ;z++){
            this.getAxis(z).moveAtPower(100,Math.round(angle.get(z)));
        }
    }
}
