package at.pria.osiris.linker;

import at.pria.osiris.linker.communication.messageProcessors.*;
import at.pria.osiris.linker.controllers.RobotArm;
import at.pria.osiris.linker.implementation.hedgehog.HedgehogRobotArm;
import org.apache.log4j.Logger;

/**
 * Starts the Linker and sets up its communication.
 *
 * @author Ari Michael Ayvazyan
 * @version 15.02.2015
 */
public class Main {
    private static Logger logger = org.apache.log4j.Logger.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Linker started");
        //Initialize the MessageProcessorDistributor to handle incoming requests
        MessageProcessorDistributor msgDistributor = new MessageProcessorDistributor();

        //Initialize a RobotArm Implementation
        RobotArm robotArm = new HedgehogRobotArm(msgDistributor);

        //Add the message processors to handle incoming requests
        logger.info("Adding Processors");
        msgDistributor.addMessageProcessor(new SensorValueRequestProcessor(robotArm));
        msgDistributor.addMessageProcessor(new AxisValueRequestProcessor(robotArm));
        msgDistributor.addMessageProcessor(new MoveAxisRequestProcessor(robotArm));
        msgDistributor.addMessageProcessor(new StringProcessor(robotArm));
        logger.info("All Processors are added");
        //Start to debug
        if(args.length>0 &&args[0].equals("debug")){
            logger.info("Ari debugger");
            try {
                robotArm.getAxis(1).moveToAngle(0);
                Thread.sleep(2500);
                robotArm.getAxis(1).moveToAngle(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if(args.length>0){
            logger.info("wolfgang debug Session started");
            logger.info("Moving the Axes");
            robotArm.getAxis(0).moveToAngle(0);
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("All Servos set ...");
            robotArm.getAxis(0).moveAtPower(Integer.parseInt(args[0]));
            logger.info("done");
        }
    }
}
