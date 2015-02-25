package at.pria.osiris.linker;

import at.pria.osiris.linker.communication.CommunicationInterface;
import at.pria.osiris.linker.communication.messageProcessors.*;
import at.pria.osiris.linker.controllers.RobotArm;
import at.pria.osiris.linker.controllers.components.Axes.Axis;
import at.pria.osiris.linker.implementation.hedgehog.HedgehogRobotArm;
import messages.requests.SensorValueRequest;
import messages.responses.SensorValueResponse;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;

import static org.mockito.Mockito.*;


/**
 * @author Samuel Schmidt
 * @version 2/25/2015
 */
public class SensorValueRequestProcessorTest {
    private static MessageProcessorDistributor msgDistributor;
    private static RobotArm robotArm;
    private static RobotArm robotArmSpy;
    private static Logger logger = Logger.getLogger(SensorValueRequestProcessorTest.class);
    private static CommunicationInterface communicationInterface;

    /**
     * Sets up the Serial Connection
     * @throws Exception
     */
    @Before
    public void before() throws Exception {

        logger.info("new Linker started");
        //Initialize the MessageProcessorDistributor to handle incoming requests
        msgDistributor = new MessageProcessorDistributor();

//        robotArm = mock(HedgehogRobotArm.class);
//        when(robotArm.getAxis(
//                0).getSensorValue()).thenReturn(42);

        //Initialize a RobotArm Implementation





        //Add the message processors to handle incoming requests
        logger.info("Adding Processors");
        msgDistributor.addMessageProcessor(new SensorValueRequestProcessor(robotArm));
        logger.info("All Processors are added");
    }

    @Test
    public void processMessageTest() throws Exception {

        final Axis axis = new Axis("name") {
            @Override
            public int getSensorValue() {
                return 42;
            }

            @Override
            public void moveToPosition(int position) {

            }

            @Override
            public void moveAtPower(int power) {

            }
        };

        communicationInterface = new CommunicationInterface() {
            @Override
            public void sendMessage(Serializable message) {
                Assert.assertTrue(((SensorValueResponse) message).getSensorValue() == 42);
                Assert.assertTrue(((SensorValueResponse) message).getSensorNumber() == 0);
                System.out.println("test");
            }

            @Override
            public void setupCommunication(MessageProcessor messageProcessor) {
            }
        };

        robotArm = new RobotArm(communicationInterface, msgDistributor) {
            @Override
            public Axis[] getAvailableAxes() {
                return new Axis[0];
            }

            @Override
            public Axis getAxis(int i) {
                return axis;
            }
        };

        robotArmSpy = spy(robotArm);
        doReturn(axis)
        .when(robotArmSpy).getAxis(0);

        SensorValueRequestProcessor sensorValueRequestProcessor = new SensorValueRequestProcessor(robotArm);
        sensorValueRequestProcessor.processMessage(new SensorValueRequest(0));

    }

}
