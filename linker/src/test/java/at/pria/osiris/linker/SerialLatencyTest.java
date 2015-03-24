package at.pria.osiris.linker;

import at.pria.osiris.linker.communication.CommunicationInterface;
import at.pria.osiris.linker.communication.messageProcessors.*;
import at.pria.osiris.linker.controllers.RobotArm;
import at.pria.osiris.linker.implementation.hedgehog.HedgehogRobotArm;
import org.andrix.low.NotConnectedException;
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
    private static MessageProcessorDistributor msgDistributor;
    private static RobotArm robotArm;
    private static Logger logger = Logger.getLogger(SerialLatencyTest.class);
    private static boolean configuredPi = true;

    /**
     * Sets up the Serial Connection
     * @throws Exception
     */
    @Before
    public void before() throws Exception {
        logger.info("new Linker started");
        //Initialize the MessageProcessorDistributor to handle incoming requests
        msgDistributor = new MessageProcessorDistributor();

        //Initialize a RobotArm Implementation
        try{
            robotArm = new HedgehogRobotArm(msgDistributor,true);
        } catch (Exception e){
            e.printStackTrace();
            configuredPi = false;
        }

        //Add the message processors to handle incoming requests
        logger.info("Adding Processors");
        msgDistributor.addMessageProcessor(new SensorValueRequestProcessor(robotArm));
        msgDistributor.addMessageProcessor(new MoveAxisRequestProcessor(robotArm));
        msgDistributor.addMessageProcessor(new StringProcessor(robotArm));
        logger.info("All Processors are added");
    }

    @After
    public void after() throws Exception {

    }

    /**
     * Tests the Serialports communcication speed using a sample String
     */

    @Test
    public void testSerialSendMessage() {
        if(!configuredPi)
            return;
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
        robotArm.getCommunicationInterface().sendMessage(text);

        endTime = System.nanoTime();

        //divide by 1000000 to get milliseconds.
        long duration = (endTime - startTime) / 1000000;
        logger.trace("testSerialSendMessage took " + duration + "ms");
    }
}
