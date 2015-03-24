package at.pria.osiris.linker.communication;

import at.pria.osiris.linker.communication.messageProcessors.*;
import at.pria.osiris.linker.controllers.RobotArm;
import org.apache.log4j.Logger;

/**
 * @author Ari Ayvazyan
 * @version 24.03.2015
 */
public class MessageProcessorRegister {
    private static Logger logger = org.apache.log4j.Logger.getLogger(MessageProcessorRegister.class);

    public static void setupMessageDisstributor(RobotArm robotArm, MessageProcessorDistributor msgDistributor) {
        logger.info("Adding Processors");
        msgDistributor.addMessageProcessor(new SensorValueRequestProcessor(robotArm));
        msgDistributor.addMessageProcessor(new AxisValueRequestProcessor(robotArm));
        msgDistributor.addMessageProcessor(new MoveAxisRequestProcessor(robotArm));
        msgDistributor.addMessageProcessor(new StringProcessor(robotArm));
        logger.info("All Processors are added");
    }
}
