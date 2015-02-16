package at.pria.osiris.linker;

import at.pria.osiris.linker.communication.messageProcessors.MessageProcessorDistributor;
import at.pria.osiris.linker.communication.messageProcessors.SensorValueRequestProcessor;
import at.pria.osiris.linker.controllers.RobotArm;
import at.pria.osiris.linker.implementation.hedgehog.HedgehogRobotArm;

/**
 * Starts the Linker and sets up its communication.
 *
 * @author Ari Michael Ayvazyan
 * @version 15.02.2015
 */
public class Main {
    public static void main(String[] args){
        //Initialize a RobotArm Implementation
        RobotArm robotArm=new HedgehogRobotArm();

        //Initialize the MessageProcessorDistributor and add MessageProcessors to handle incoming requests
        MessageProcessorDistributor msgDistributor= new MessageProcessorDistributor();

        //Add the handlers
        msgDistributor.addMessageProcessor(new SensorValueRequestProcessor(robotArm));


        //Setup the Communication of the specified RobotArm/Controller
        robotArm.getCommunicationInterface().setupCommunication(msgDistributor);
    }
}
