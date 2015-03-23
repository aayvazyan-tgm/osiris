package at.pria.osiris.osiris.controllers.hedgehogdirect;

import java.io.IOException;

import api.Robotarm;
import at.pria.osiris.osiris.controllers.ConnectionNotEstablishedException;
import at.pria.osiris.osiris.controllers.Controller;
import at.pria.osiris.osiris.controllers.ControllerSetup;
import at.pria.osiris.osiris.controllers.hedgehog.HedgehogSetup;
import at.pria.osiris.osiris.controllers.hedgehog.OldLinkerHedgehogRemoteRobotarm;

/**
 * Smartphone --> Hedgehog
 * @author Samuel Schmidt
 * @version 3/10/2015
 */
public class HedgehogDirectController implements Controller {
    private ControllerSetup hedgehogDirectSetup;
//    private CommunicationClassWithSendDataMethod something;

    public HedgehogDirectController(/*Configuration*/) { this.hedgehogDirectSetup = new HedgehogSetup(); }

    @Override
    public ControllerSetup getSetup() {
        return hedgehogDirectSetup;
    }

    @Override
    public Robotarm getRobotArm() throws ConnectionNotEstablishedException {
        try {
            return DirectCommunicationRobotArm.getInstance();
        } catch (IOException e) {
            throw new ConnectionNotEstablishedException(e);
        }
    }
}