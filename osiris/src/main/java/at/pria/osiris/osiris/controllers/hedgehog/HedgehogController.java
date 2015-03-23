package at.pria.osiris.osiris.controllers.hedgehog;

import at.pria.osiris.osiris.controllers.ConnectionNotEstablishedException;
import at.pria.osiris.osiris.controllers.Controller;
import at.pria.osiris.osiris.controllers.ControllerSetup;
import at.pria.osiris.osiris.controllers.RobotArm;

import java.io.IOException;

/**
 * @author Ari Ayvazyan
 * @version 05.Dec.14
 */
public class HedgehogController implements Controller {
    private ControllerSetup hedgehogSetup;
    private RobotArm robotArm;
//    private CommunicationClassWithSendDataMethod something;

    public HedgehogController(/*Configuration*/) {
        this.hedgehogSetup = new HedgehogSetup();
    }

    @Override
    public ControllerSetup getSetup() {
        return hedgehogSetup;
    }

    @Override
    public RobotArm getRobotArm() throws ConnectionNotEstablishedException {
        if (robotArm == null) {
            try {
                robotArm = HedgehogRobotArm.getInstance();
            } catch (IOException e) {
                throw new ConnectionNotEstablishedException(e);
            }
        }
        return robotArm;
    }
}
