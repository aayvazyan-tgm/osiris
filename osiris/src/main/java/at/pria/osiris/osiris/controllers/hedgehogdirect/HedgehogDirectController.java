package at.pria.osiris.osiris.controllers.hedgehogdirect;

import java.io.IOException;

import at.pria.osiris.osiris.controllers.RobotArm;
import at.pria.osiris.osiris.controllers.ConnectionNotEstablishedException;
import at.pria.osiris.osiris.controllers.Controller;
import at.pria.osiris.osiris.controllers.ControllerSetup;
import at.pria.osiris.osiris.controllers.hedgehog.HedgehogSetup;

/**
 * Smartphone --> Hedgehog
 * @author Samuel Schmidt
 * @version 3/10/2015
 */
public class HedgehogDirectController implements Controller {
    private ControllerSetup hedgehogDirectSetup;
    private RobotArm robotArm;
//    private CommunicationClassWithSendDataMethod something;

    public HedgehogDirectController(/*Configuration*/) { this.hedgehogDirectSetup = new HedgehogSetup(); }

    @Override
    public ControllerSetup getSetup() {
        return hedgehogDirectSetup;
    }

    @Override
    public RobotArm getRobotArm() throws ConnectionNotEstablishedException {
        if (robotArm == null) {
            try {
                robotArm = DirectCommunicationRobotArm.getInstance();
            } catch (IOException e) {
                throw new ConnectionNotEstablishedException(e);
            }
        }
        return robotArm;
    }
}