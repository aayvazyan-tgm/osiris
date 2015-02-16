package at.pria.osiris.linker.implementation.hedgehog;

import at.pria.osiris.linker.communication.CommunicationInterface;
import at.pria.osiris.linker.controllers.RobotArm;
import at.pria.osiris.linker.controllers.components.Axis;
import at.pria.osiris.linker.implementation.hedgehog.communication.HedgehogCommunicationInterface;

/**
 *
 * @author Ari Michael Ayvazyan
 * @version 15.02.2015
 */
public class HedgehogRobotArm extends RobotArm {
    private static HedgehogCommunicationInterface communicationInterfaceINSTANCE =new HedgehogCommunicationInterface();

    public HedgehogRobotArm() {
//TODO this class is a stub
	}

    @Override
    public Axis[] getAvailableAxes() {
        return new Axis[0];
    }

    @Override
    public Axis getAxis(int i) {
        return null;
    }

    @Override
    public CommunicationInterface getCommunicationInterface() {
        return communicationInterfaceINSTANCE;
    }


}
