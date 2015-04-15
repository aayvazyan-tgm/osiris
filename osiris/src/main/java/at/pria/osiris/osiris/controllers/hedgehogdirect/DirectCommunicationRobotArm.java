package at.pria.osiris.osiris.controllers.hedgehogdirect;

import at.pria.osiris.linker.communication.messageProcessors.MessageProcessorDistributor;
import at.pria.osiris.linker.controllers.components.Axes.Axis;
import at.pria.osiris.linker.controllers.components.Axes.ServoAxis;
import at.pria.osiris.linker.implementation.hedgehog.components.HedgehogDoubleServo;
import at.pria.osiris.linker.implementation.hedgehog.components.HedgehogServo;
import at.pria.osiris.osiris.controllers.RobotArm;
import org.andrix.low.AXCPAccessor;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This RobotArm communicates with the Hedgehog Controller directly
 *
 * @author Ari Ayvazyan
 * @version 15.4.2015
 */
public class DirectCommunicationRobotArm implements RobotArm {

    private static final int MAX_POWER = 100;
    private static DirectCommunicationRobotArm INSTANCE;
    private ArrayList<Axis> axes;
    private boolean connected=false;
    private MessageProcessorDistributor linkerMsgDistributor;
    private at.pria.osiris.linker.controllers.RobotArm linkerRobotArm;


    private DirectCommunicationRobotArm() throws IOException {
        isConnected();
    }

    public static DirectCommunicationRobotArm getInstance() throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new DirectCommunicationRobotArm();
        }
        return INSTANCE;
    }

    @Override
    public void turnAxis(int axis, int power) {
        if(!isConnected())return;
        axes.get(axis).moveAtPower(power);
    }

    @Override
    public void stopAxis(int axis) {
        if(!isConnected())return;
        axes.get(axis).moveAtPower(0);
    }

    @Override
    public void moveToAngle(int axis, int angle) {
        if(!isConnected())return;
        axes.get(axis).moveToAngle(angle);
    }

    @Override
    public double getMaximumAngle(int axis) {
        if(!isConnected())return 0;
        return axes.get(axis).getMaximumAngle();
    }

    @Override
    public boolean moveTo(double x, double y, double z) {
        if(!isConnected())return false;
        System.out.println("I am not yet implemented");
        return true;
    }

    public double getMaxMovePower() {
        return MAX_POWER;
    }

    @Override
    public void sendMessage(Serializable message) {
        if(!isConnected())return;
        System.out.println(message.toString());
    }

    @Override
    public double getPosition(int axis) {
        return 0;
    }

    public synchronized boolean isConnected(){
        if(!connected) {
            try {
                this.axes = new ArrayList<Axis>();
                this.axes.add(new ServoAxis("BaseAxis", new HedgehogServo(1, 720, 2, 2)));
                this.axes.add(new ServoAxis("VerticalAxis", new HedgehogDoubleServo(2, 3, 25, 0, 60, 3, 2)));
                this.axes.add(new ServoAxis("HorizontalAxis", new HedgehogServo(4, 180, 3, 2)));
                this.connected = true;
            } catch (Exception e) {
                this.connected = false;
                e.printStackTrace();
            }
        }
        return connected;
    }
    public String getConnectionState(){
        return AXCPAccessor.getInstance().getConnectionState().toString();
    }
}
