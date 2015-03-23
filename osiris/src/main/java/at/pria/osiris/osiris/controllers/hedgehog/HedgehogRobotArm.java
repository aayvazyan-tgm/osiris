package at.pria.osiris.osiris.controllers.hedgehog;

import android.util.Log;
import at.pria.osiris.osiris.util.AXCPWrapper;
import at.pria.osiris.osiris.controllers.RobotArm;
import messages.requests.MoveAxisToAngleRequest;
import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;
import java.io.IOException;
import java.io.Serializable;

/**
 * This class Provides a simple Interface to Send commands to the Robot
 *
 * @author Adrian Bergler, Ari Ayvazyan
 * @version 3.11.2014
 */
public class HedgehogRobotArm extends Thread implements RobotArm {

    private static final int MAX_POWER = 100;
    private static HedgehogRobotArm INSTANCE;

    private HedgehogRobotArm() throws IOException {
        this.start();

    }

    public static HedgehogRobotArm getInstance() throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new HedgehogRobotArm();
        }
        return INSTANCE;
    }

    @Override
    public void turnAxis(int axis, int power) {
        sendMessage("turnaxis/" + axis + "/" + power);
    }

    @Override
    public void stopAxis(int axis) {
        sendMessage("stopaxis/" + axis);
    }

    @Override
    public void moveToAngle(int axis, int angle) {
        sendMessage(new MoveAxisToAngleRequest(axis,angle));
    }

    @Override
    public void getMaximumAngle(int axis) {
        //TODO Not yet implementedw
    }

    @Override
    public boolean moveTo(double x, double y, double z) {
        sendMessage("moveto/" + x + "/" + y + "/" + z);
        return true;
    }

    public double getMaxMovePower() {
        return MAX_POWER;
    }

    @Override
    public void sendMessage(Serializable message) {
        try {
            AXCPWrapper.sendData(message);
        } catch (RequestTimeoutException e) {
            Log.d("Connection", e.toString());
        } catch (NotConnectedException e) {
            Log.d("Connection", e.toString());
        } catch (IOException e){
            Log.d("Connection", e.toString());
        }
    }
}
