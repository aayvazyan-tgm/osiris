package at.pria.osiris.osiris.controllers.hedgehog;

import api.Robotarm;
import at.pria.osiris.osiris.controllers.ConnectionNotEstablishedException;
import at.pria.osiris.osiris.controllers.Controller;
import at.pria.osiris.osiris.controllers.ControllerSetup;

import java.io.IOException;

/**
 * @author Ari Ayvazyan
 * @version 05.Dec.14
 */
public class HedgehogController implements Controller {
    private ControllerSetup hedgehogSetup;

    public HedgehogController(/*Configuration*/) {
        this.hedgehogSetup = new HedgehogSetup();
    }

    @Override
    public ControllerSetup getSetup() {
        return hedgehogSetup;
    }

    @Override
    public Robotarm getRobotArm() throws ConnectionNotEstablishedException {
        try {
            return HedgehogRemoteRobotarm.getInstance();
        } catch (IOException e) {
            throw new ConnectionNotEstablishedException(e);
        }
    }
}
