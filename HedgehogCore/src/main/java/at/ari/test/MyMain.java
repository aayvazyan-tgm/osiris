package at.ari.test;

import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;
import org.andrix.motors.Motor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ari
 * @version 19.Nov.14
 */
public class MyMain {
    private static final Logger log = LoggerFactory.getLogger(MyMain.class);
    public static void main(String[] args){
        try {
            Motor mZero = new Motor(0);
            try {
                mZero.forward();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mZero.backward();
            } catch (RequestTimeoutException e) {
                log.warn("RequestTimeout:  " + e.getMessage());
            }
        } catch (NotConnectedException e) {
            log.warn("Not connected to Controller 0: " + e.getMessage());
        }
    }
}
