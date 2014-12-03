package api;

import org.andrix.AXCP;
import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;

/**
 * @author Ari Ayvazyan
 * @version 03.Dec.14
 */
public class AXCPWrapper {
    public static void sendData(byte[] data) throws NotConnectedException, RequestTimeoutException {
        AXCP.command(AXCP.EXECUTION_DATA_ACTION,"",0,data);
    }
}
