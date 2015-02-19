package at.pria.osiris.linker.implementation.hedgehog;

import at.pria.osiris.linker.communication.CommunicationInterface;
import at.pria.osiris.linker.communication.messageProcessors.MessageProcessor;
import at.pria.osiris.linker.controllers.RobotArm;
import at.pria.osiris.linker.controllers.components.Axis;
import at.pria.osiris.linker.implementation.hedgehog.axes.BaseAxis;
import at.pria.osiris.linker.implementation.hedgehog.axes.HorizontalAxis;
import at.pria.osiris.linker.implementation.hedgehog.axes.VerticalAxis;
import at.pria.osiris.linker.implementation.hedgehog.communication.HedgehogCommunicationInterface;
import org.andrix.low.NotConnectedException;

import java.util.ArrayList;

/**
 *
 * @author Ari Michael Ayvazyan
 * @version 15.02.2015
 */
public class HedgehogRobotArm extends RobotArm {

    private final ArrayList<Axis> axes;

    public HedgehogRobotArm(MessageProcessor messageProcessor) {
        //Add the Communication Interface
        super(new HedgehogCommunicationInterface(),messageProcessor);
        //Add the axes
        this.axes=new ArrayList<Axis>();
        try {
            this.axes.add(new BaseAxis());
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
        try {
            this.axes.add(new VerticalAxis());
        } catch (NotConnectedException e) {
            e.printStackTrace();
        }
        try {
            this.axes.add(new HorizontalAxis());
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


}
