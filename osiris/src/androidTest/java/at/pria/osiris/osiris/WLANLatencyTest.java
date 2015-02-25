package at.pria.osiris.osiris;

import api.Robotarm;
import at.pria.osiris.osiris.communication.DataListener;
import at.pria.osiris.osiris.communication.messageProcessor.MessageProcessor;
import at.pria.osiris.osiris.communication.messageProcessor.SensorValueResponseProcessor;
import at.pria.osiris.osiris.communication.messageProcessor.StringProcessor;
import at.pria.osiris.osiris.controllers.ConnectionNotEstablishedException;
import at.pria.osiris.osiris.controllers.Controller;
import at.pria.osiris.osiris.controllers.hedgehog.MyStateListener;
import at.pria.osiris.osiris.sensors.SensorRefreshable;
import at.pria.osiris.osiris.view.TableSensorValuesFragment;
import messages.SerializableMessage;
import org.andrix.listeners.ExecutionListener;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.apache.log4j.Logger;

import at.pria.osiris.osiris.controllers.ControllerFactory;
import at.pria.osiris.osiris.controllers.ControllerType;
import org.junit.Test;

/**
 * @author Samuel Schmidt
 * @version 2/17/2015
 */
public class WLANLatencyTest {
    private static Controller robotController;
    private static DataListener dataListener;
    private static Logger logger = Logger.getLogger(WLANLatencyTest.class);

    @Before
    public void before() throws Exception {
        robotController = ControllerFactory.getController(ControllerType.Hedgehog);
        robotController.getSetup().setup(robotController.getRobotArm());
        dataListener = robotController.getSetup().getDataListener();
    }

    @After
    public void after() throws Exception {

    }

    @Test
    public void testWLANSendMessage() {
        MessageProcessor messageProcessor = new MessageProcessor() {
            @Override
            public void processMessage(Object msg) {
                long time = System.currentTimeMillis() - ((SerializableMessage) msg).getCreationTime();
                logger.trace("testWLANSendMessage took: " + time + "ms");

                Assert.assertTrue(msg.toString().equals("test"));
            }
        };
        dataListener.addMessageProcessor(messageProcessor);
        try {
            robotController.getRobotArm().test();
        } catch (ConnectionNotEstablishedException e) {
            e.printStackTrace();
        }
    }
}