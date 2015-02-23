package at.pria.osiris.linker;

import at.pria.osiris.linker.communication.CommunicationInterface;
import at.pria.osiris.linker.communication.messageProcessors.MessageProcessor;
import at.pria.osiris.linker.communication.messageProcessors.MessageProcessorDistributor;
import at.pria.osiris.linker.communication.messageProcessors.SensorValueRequestProcessor;
import at.pria.osiris.linker.controllers.RobotArm;
import at.pria.osiris.linker.implementation.hedgehog.HedgehogRobotArm;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.apache.log4j.Logger;
/**
 * Tests the Latency from communicating with the Pi across the Serial Connection to the Hedgehog Controller
 *
 * @author Samuel Schmidt
 * @version 2/16/2015
 */
public class SerialLatencyTest {
    private static CommunicationInterface communicationInterface;
    private static MessageProcessorDistributor msgDistributor;
    private static RobotArm robotArm;
    private static Logger log = Logger.getLogger(SerialLatencyTest.class);

//    public static void main(String[] args) throws Exception {
//        JUnitCore.main(
//                "at.pria.osiris.linker.SerialLatencyTest");
//    }


    /**
     * Sets up the Serial Connection
     * @throws Exception
     */
    @Before
    public void before() throws Exception {
        //Initialize the MessageProcessorDistributor and add MessageProcessors to handle incoming requests
        msgDistributor = new MessageProcessorDistributor();
        //Initialize a RobotArm Implementation
        robotArm = new HedgehogRobotArm(msgDistributor);

        //Add the handlers
        msgDistributor.addMessageProcessor(new SensorValueRequestProcessor(robotArm));
        //Setup the Communication of the specified RobotArm/Controller
        communicationInterface = robotArm.getCommunicationInterface();
        communicationInterface.setupCommunication(msgDistributor);


    }

    @After
    public void after() throws Exception {

    }

    @Test
    public void testSerialSendMessage() {
        long startTime, endTime;
        String text = "I like Serialports";

        MessageProcessor messageProcessor = new MessageProcessor() {
            @Override
            public void processMessage(Object msg) {
                Assert.assertTrue(msg.toString().equals("I like Serialports"));
            }
        };

        msgDistributor.addMessageProcessor(messageProcessor);

        startTime = System.nanoTime();
        communicationInterface.sendMessage(text);
        endTime = System.nanoTime();

        //divide by 1000000 to get milliseconds.
        long duration = (endTime - startTime) / 1000000;
        log.trace("testSerialSendMessage took " + duration + "ms");
    }

}
