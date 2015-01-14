package at.pria.osiris.osiris.util;

import Util.Serializer;
import org.andrix.AXCP;
import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;

import java.io.IOException;
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
    public static void sendData(Serializable data) throws IOException, NotConnectedException, RequestTimeoutException {
        sendData(Serializer.serialize(data));
    }
}
