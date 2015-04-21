package at.pria.osiris.linker;

import at.pria.osiris.linker.communication.MessageProcessorRegister;
import at.pria.osiris.linker.communication.messageProcessors.*;
import at.pria.osiris.linker.controllers.RobotArm;
import at.pria.osiris.linker.implementation.hedgehog.HedgehogRobotArm;
import at.pria.osiris.linker.kinematics.Kinematics;
import at.pria.osiris.linker.kinematics.GeometricKinematics;
import org.apache.log4j.Logger;

import java.util.ArrayList;

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
        /*
        This if moves axis 1 to 2 degree and moves the same axis to 59 degrees afterwards
         */
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

        }
        /*
        This test moves the axis 1 to 2 degree
         */
        else if (args.length > 0 && args[0].equals("start")) {
            logger.info("Start debugger");
            try {
                logger.info("Moving to 0");
                robotArm.getAxis(1).moveToAngle(2);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /*
            This test moves the axis tho the specified position
             */
        } else if (args.length > 0 && args[0].equals("move")) {
            logger.info("Start move");
            logger.info("Moving Axis: " + args[1]);
            robotArm.getAxis(Integer.parseInt(args[1])).moveToAngle(Integer.parseInt(args[2]));
        }
        /*
        This if tests de pwm of the robot.
        It moves the angle which is defined by the user to a start position.
        After this movement it starts the pwm method of the axis
         */
        else if (args.length > 0 && args[0].equals("pwm")) {
            logger.info("wolfgang pwm debug Session started");
            logger.info("Moving Axis: " + args[1]);
            robotArm.getAxis(Integer.parseInt(args[1])).moveToAngle((int)robotArm.getAxis(Integer.parseInt(args[1])).getMaximumAngle()/2);
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("All Servos set ...");
            robotArm.getAxis(Integer.parseInt(args[1])).moveAtPower(Integer.parseInt(args[2]));
            logger.info("done");
        }
        /*
        This if tests the inverse kinematic.
        It moves before the test starts all Axes to 5 degree.
        After the initial movement it calculates the position he needs to reach.
        At least he moves to the calculated positions.
         */
        else if (args.length > 0 && args[0].equals("kin")) {
            logger.info("adrian kin debug Session started");
            robotArm.getAxis(0).moveToAngle(5);
            robotArm.getAxis(1).moveToAngle(5);
            robotArm.getAxis(2).moveToAngle(5);
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("All Servos set ...");
            double[] fragmentlengths = {26, 23};
            double[][] padding = {{0,0,0},{0,0,0},{0,0,0}};

            Kinematics kinematics = new GeometricKinematics(32, 0, 24, fragmentlengths, padding);

            ArrayList<Double> solution = kinematics.calculateValues();

            robotArm.moveSynchronized(solution);
            /*
            robotArm.getAxis(0).moveToAngle((int)Math.round(solution.get(0)));
            robotArm.getAxis(1).moveToAngle((int)Math.round(solution.get(1)));
            robotArm.getAxis(2).moveToAngle((int)Math.round(solution.get(2)));
            */
            logger.info("done");
        }
    }
}
