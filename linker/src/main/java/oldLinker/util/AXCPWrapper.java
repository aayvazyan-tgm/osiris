package oldLinker.util;

import org.andrix.AXCP;
import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;

import java.io.Serializable;

/**
 * @author Ari Ayvazyan
 * @version 03.Dec.14
 */
public class AXCPWrapper {
    /*
     * Sends data via the Hedgehog AXCP Controller
     */
    public static void sendData(byte[] data) throws NotConnectedException, RequestTimeoutException {
        AXCP.command(AXCP.EXECUTION_DATA_ACTION, "", 0, data);
    }
    /*
     * Sends data via the Hedgehog AXCP Controller
     */
    public static void sendData(Serializable data) throws NotConnectedException, RequestTimeoutException {
        sendData(((byte[]) data));
    }
}
