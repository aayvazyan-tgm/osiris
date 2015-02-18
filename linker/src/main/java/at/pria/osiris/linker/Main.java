package at.pria.osiris.linker;

import at.pria.osiris.linker.communication.messageProcessors.MessageProcessorDistributor;
import at.pria.osiris.linker.communication.messageProcessors.MoveAxisRequestProcessor;
import at.pria.osiris.linker.communication.messageProcessors.SensorValueRequestProcessor;
import at.pria.osiris.linker.controllers.RobotArm;
import at.pria.osiris.linker.implementation.hedgehog.HedgehogRobotArm;
import at.pria.osiris.linker.implementation.hedgehog.communication.HedgehogCommunicationInterface;

/**
 * Starts the Linker and sets up its communication.
 *
 * @author Ari Michael Ayvazyan
 * @version 15.02.2015
 */
public class Main {
    public static void main(String[] args){

        //Initialize the MessageProcessorDistributor to handle incoming requests
        MessageProcessorDistributor msgDistributor= new MessageProcessorDistributor();

        //Initialize a RobotArm Implementation
        RobotArm robotArm=new HedgehogRobotArm(msgDistributor);

        //Add the message processors to handle incoming requests
        msgDistributor.addMessageProcessor(new SensorValueRequestProcessor(robotArm));
        msgDistributor.addMessageProcessor(new MoveAxisRequestProcessor(robotArm));
    }
}
