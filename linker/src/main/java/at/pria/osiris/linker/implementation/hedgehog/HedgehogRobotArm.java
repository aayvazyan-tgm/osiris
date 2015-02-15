package at.pria.osiris.linker.implementation.hedgehog;

import at.pria.osiris.linker.communication.CommunicationInterface;
import at.pria.osiris.linker.controllers.RobotArm;
import at.pria.osiris.linker.controllers.components.Axis;
import at.pria.osiris.linker.implementation.hedgehog.communication.HedgehogCommunicationInterface;

public class HedgehogRobotArm extends RobotArm {
    private static HedgehogCommunicationInterface communicationInterfaceINSTANCE =new HedgehogCommunicationInterface();

    @Override
    public Axis[] getAvailableAxes() {
        return new Axis[0];
    }

    @Override
    public CommunicationInterface getCommunicationInterface() {
        return communicationInterfaceINSTANCE;
    }


}
