package at.pria.osiris.linker;

import at.pria.osiris.linker.communication.MessageProcessorRegister;
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
        RobotArm robotArm = new HedgehogRobotArm(msgDistributor,true);

        //Add the message processors to handle incoming requests
        MessageProcessorRegister.setupMessageDisstributor(robotArm,msgDistributor);

        //Start to debug
        if (args.length > 0 && args[0].equals("debug")) {
            logger.info("Ari debugger");
            try {
                logger.info("Moving to 0");
                robotArm.getAxis(1).moveToAngle(2);
                Thread.sleep(2000);
                logger.info("Moving to: " + 59);
                robotArm.getAxis(1).moveToAngle(59);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else if (args.length > 0 && args[0].equals("start")) {
            logger.info("Start debugger");
            try {
                logger.info("Moving to 0");
                robotArm.getAxis(1).moveToAngle(2);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (args.length > 0 && args[0].equals("move")) {
            logger.info("Start move");
            logger.info("Moving Axis: " + args[1]);
            robotArm.getAxis(Integer.parseInt(args[1])).moveToAngle(Integer.parseInt(args[2]));
        }else if (args.length > 0 && args[0].equals("pwm")) {
            logger.info("wolfgang pwm debug Session started");
            logger.info("Moving Axis: " + args[1]);
            robotArm.getAxis(Integer.parseInt(args[1])).moveToAngle(Integer.parseInt(args[2]));
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("All Servos set ...");
            robotArm.getAxis(Integer.parseInt(args[1])).moveAtPower(Integer.parseInt(args[2]));
            logger.info("done");
        }
    }
}
