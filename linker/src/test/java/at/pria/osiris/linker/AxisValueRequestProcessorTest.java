package at.pria.osiris.linker;

import at.pria.osiris.linker.communication.CommunicationInterface;
import at.pria.osiris.linker.communication.messageProcessors.AxisValueRequestProcessor;
import at.pria.osiris.linker.communication.messageProcessors.MessageProcessor;
import at.pria.osiris.linker.communication.messageProcessors.MessageProcessorDistributor;
import at.pria.osiris.linker.controllers.RobotArm;
import at.pria.osiris.linker.controllers.components.Axes.Axis;
import messages.requests.AxisValueRequest;
import messages.responses.AxisValueResponse;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;


/**
 * @author Samuel Schmidt
 * @version 2/25/2015
 */
public class AxisValueRequestProcessorTest {
    private static MessageProcessorDistributor msgDistributor;
    private static RobotArm robotArm;
    private static RobotArm robotArmSpy;
    private static Logger logger = Logger.getLogger(AxisValueRequestProcessorTest.class);
    private static CommunicationInterface communicationInterface;

    /**
     * Sets up the Serial Connection
     *
     * @throws Exception
     */
    @Before
    public void before() throws Exception {
        logger.info("new Linker started");
        //Initialize the MessageProcessorDistributor to handle incoming requests
        msgDistributor = new MessageProcessorDistributor();

        //Add the message processors to handle incoming requests
        logger.info("Adding Processors");
        msgDistributor.addMessageProcessor(new AxisValueRequestProcessor(robotArm));
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
            public void moveToAngle(int angle) {

            }

            @Override
            public void moveAtPower(int power) {

            }
        };

        communicationInterface = new CommunicationInterface() {
            @Override
            public void sendMessage(Serializable message) {
                Assert.assertTrue(((AxisValueResponse) message).getAxisValue() == 42);
                Assert.assertTrue(((AxisValueResponse) message).getAxisNumber() == 0);
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

            @Override
            public int getSensorValue(int sensorPort) {
                return 0;
            }
        };

        robotArmSpy = spy(robotArm);
        doReturn(axis)
                .when(robotArmSpy).getAxis(0);

        AxisValueRequestProcessor axisValueRequestProcessor = new AxisValueRequestProcessor(robotArm);
        axisValueRequestProcessor.processMessage(new AxisValueRequest(0));
    }

}
