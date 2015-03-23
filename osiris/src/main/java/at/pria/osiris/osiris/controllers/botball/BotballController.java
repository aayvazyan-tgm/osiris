package at.pria.osiris.osiris.controllers.botball;

import at.pria.osiris.osiris.controllers.RobotArm;
import at.pria.osiris.osiris.controllers.ConnectionNotEstablishedException;
import at.pria.osiris.osiris.controllers.Controller;
import at.pria.osiris.osiris.controllers.ControllerSetup;
import at.pria.osiris.osiris.controllers.NoSetupException;

/**
 * Created by helmuthbrunner on 09/02/15.
 */
public class BotballController implements Controller {

    private BotballSetup setup;

    public BotballController() {
        setup= new BotballSetup("192.168.43.241", 8889);
    }

    @Override
    public ControllerSetup getSetup() {
        return setup;
    }

    @Override
    public RobotArm getRobotArm() throws ConnectionNotEstablishedException {
        try {
            return BotballRemoteRobotArm.getInstance();
        } catch (NoSetupException e) {
            throw new ConnectionNotEstablishedException(e);
        }
    }
}
