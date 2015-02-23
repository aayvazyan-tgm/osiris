package at.pria.osiris.linker.communication.messageProcessors;

import at.pria.osiris.linker.controllers.RobotArm;
import messages.requests.MoveAxisRequest;

/**
 * @author Ari Michael Ayvazyan
 * @version 16.02.2015
 */
public class MoveAxisRequestProcessor implements MessageProcessor {
    private RobotArm robotArm;

    public MoveAxisRequestProcessor(RobotArm robotArm) {
        this.robotArm = robotArm;
    }

    @Override
    public void processMessage(Object msg) {
        if (msg instanceof MoveAxisRequest) {
            MoveAxisRequest request = (MoveAxisRequest) msg;
            robotArm.getAxis(request.getAxisPort()).moveAtPower(request.getPower());
        }
    }
}
