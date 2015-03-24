package at.pria.osiris.osiris.controllers.hedgehogdirect;

import at.pria.osiris.linker.communication.MessageProcessorRegister;
import at.pria.osiris.linker.communication.messageProcessors.MessageProcessorDistributor;
import at.pria.osiris.linker.implementation.hedgehog.HedgehogRobotArm;
import at.pria.osiris.osiris.controllers.RobotArm;
import messages.requests.MoveAxisToAngleRequest;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;

/**
 * This class Provides a simple Interface to Send commands to the Robot
 *
 * @author Adrian Bergler, Ari Ayvazyan
 * @version 3.11.2014
 */
public class DirectCommunicationRobotArm extends Thread implements RobotArm {

    private static final int MAX_POWER = 100;
    private static Logger logger = org.apache.log4j.Logger.getLogger(DirectCommunicationRobotArm.class);
    private static DirectCommunicationRobotArm INSTANCE;
    private MessageProcessorDistributor linkerMsgDistributor;
    private at.pria.osiris.linker.controllers.RobotArm linkerRobotArm;


    private DirectCommunicationRobotArm() throws IOException {
        this.start();

        logger.info("Direct Linker started");
        //Initialize the MessageProcessorDistributor to handle incoming requests
        this.linkerMsgDistributor = new MessageProcessorDistributor();

        //Initialize a RobotArm Implementation
        this.linkerRobotArm = new HedgehogRobotArm(this.linkerMsgDistributor, false);

        //Add the message processors to handle incoming requests
        MessageProcessorRegister.setupMessageDisstributor(this.linkerRobotArm, this.linkerMsgDistributor);
    }

    public static DirectCommunicationRobotArm getInstance() throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new DirectCommunicationRobotArm();
        }
        return INSTANCE;
    }

    @Override
    public void turnAxis(int axis, int power) {
        sendMessage("turnaxis/" + axis + "/" + power);
    }

    @Override
    public void stopAxis(int axis) {
        sendMessage("stopaxis/" + axis);
    }

    @Override
    public void moveToAngle(int axis, int angle) {
        sendMessage(new MoveAxisToAngleRequest(axis, angle));
    }

    @Override
    public void getMaximumAngle(int axis) {
        //TODO Request the value and wait for the result
    }

    @Override
    public boolean moveTo(double x, double y, double z) {
        sendMessage("moveto/" + x + "/" + y + "/" + z);
        return true;
    }

    public double getMaxMovePower() {
        return MAX_POWER;
    }

    @Override
    public void sendMessage(Serializable message) {
        this.linkerMsgDistributor.processMessage(message);
    }
}
