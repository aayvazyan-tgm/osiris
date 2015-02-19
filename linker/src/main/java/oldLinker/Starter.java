package oldLinker;

import at.pria.osiris.linker.Main;
import oldLinker.control.DataListener;
import oldLinker.control.MessageProcessor.MessageProcessorDistributor;
import oldLinker.control.SensorMessenger;
import oldLinker.model.RobotarmImpl;
import org.andrix.listeners.ExecutionListener;
import org.andrix.low.AXCPAccessor;
import org.andrix.low.AXCPServer;
import org.andrix.low.HardwareController;
import org.apache.log4j.Logger;

/**
 * Starts the link-controller program
 *
 * @author Adrian Bergler
 * @version 0.2
 */
public class Starter {

    private static Logger logger = org.apache.log4j.Logger.getLogger(Starter.class);

    public static void main(String[] args) {
        //Start the new Linker if any arguments are supplied
        if (args.length > 1) {
            Main.main(new String[0]);
        } else {
            logger.info("Log4J works now");
            AXCPServer.communicationInterface = new SerialPortCommunicationInterface(); // The Serial Port Communication Interface for the Pi
            AXCPAccessor.getInstance().connectController(new HardwareController(null, HardwareController.TYPE_V3, "hedgehog-osiris")); // Initialise the AXCPAccessor

            Thread thread;
            RobotarmImpl robotarm = new RobotarmImpl();

            MessageProcessorDistributor mp = new MessageProcessorDistributor(robotarm);
            boolean running = true;

            // sends sensordata in ~1s intervall
            SensorMessenger st = new SensorMessenger(robotarm);
            thread = new Thread(st);
            thread.start();

            //A listener to receive Data from the Controller
            ExecutionListener._l_exec.add(new DataListener(mp));
        }
    }
}
