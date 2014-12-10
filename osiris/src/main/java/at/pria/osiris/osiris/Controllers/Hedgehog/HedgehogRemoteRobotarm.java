package at.pria.osiris.osiris.controllers.Hedgehog;

import android.util.Log;
import Util.AXCPWrapper;
import api.Axis;
import api.Robotarm;
import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;
import java.io.IOException;

/**
 * This class Provides a simple Interface to Send commands to the Robot
 *
 * @author Adrian Bergler, Ari Ayvazyan
 * @version 3.11.2014
 */
public class HedgehogRemoteRobotarm extends Thread implements Robotarm {

    private static final int MAX_POWER = 100;
    private static HedgehogRemoteRobotarm INSTANCE;

    private HedgehogRemoteRobotarm() throws IOException {
        this.start();

    }

    public static HedgehogRemoteRobotarm getInstance() throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new HedgehogRemoteRobotarm();
        }
        return INSTANCE;
    }

    @Override
    public void turnAxis(Axis axis, int power) {
        sendMessage("turnaxis/" + axis.ordinal() + "/" + power);
    }

    @Override
    public void turnAxis(Axis axis, int power, long timemillis) {
        sendMessage("turnaxis/" + axis.ordinal() + "/" + power + "/" + timemillis);

    }

    @Override
    public void stopAxis(Axis axis) {
        sendMessage("stopaxis/" + axis.ordinal());
    }

    @Override
    public boolean moveTo(double x, double y, double z) {
        sendMessage("moveto/" + x + "/" + y + "/" + z);
        return true;
    }

    @Override
    public void stopAll() {
        sendMessage("stopall");
    }

    /**
     * Close sockets, kill watchdogs
     */
    @Override
    public void close() {
        sendMessage("close");
    }

    @Override
    public void test() {
        sendMessage("test");
    }

    public void exit() {
        sendMessage("exit");
    }

    @Override
    public double getMaxMovePower() {
        return MAX_POWER;
    }

    private void sendMessage(String message) {
        try {
            AXCPWrapper.sendData(message);
        } catch (RequestTimeoutException e) {
            Log.d("Connection", e.toString());
        } catch (NotConnectedException e) {
            Log.d("Connection", e.toString());
        }
    }
}
