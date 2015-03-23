package at.pria.osiris.linker.communication.messageProcessors;

import at.pria.osiris.linker.controllers.RobotArm;
import messages.requests.MoveAxisRequest;
import messages.requests.MoveAxisToAngleRequest;

/**
 * @author Ari Michael Ayvazyan
 * @version 16.02.2015
 */
public class MoveAxisToAngleRequestProcessor implements MessageProcessor {
    private RobotArm robotArm;

    public MoveAxisToAngleRequestProcessor(RobotArm robotArm) {
        this.robotArm = robotArm;
    }

    @Override
    public void processMessage(Object msg) {
        if (msg instanceof MoveAxisToAngleRequest) {
            MoveAxisToAngleRequest request = (MoveAxisToAngleRequest) msg;
            robotArm.getAxis(request.getAxisPort()).moveToAngle(request.getAngle());
        }
    }
}
