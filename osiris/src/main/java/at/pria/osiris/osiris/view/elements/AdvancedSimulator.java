package at.pria.osiris.osiris.view.elements;

import android.content.Context;
import android.view.View;
import at.pria.osiris.OsirisSimulation;
import at.pria.osiris.linker.controllers.components.Axes.ServoHelper;
import at.pria.osiris.linker.controllers.components.systemDependent.Servo;
import at.pria.osiris.osiris.controllers.RobotArm;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.math.Vector3;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Ari Michael Ayvazyan, Samuel Schmidt
 * @version 27.04.2015
 */
public class AdvancedSimulator extends AndroidApplication implements RobotArm {

    private OsirisSimulation osirisSimulation;

    private int axis1Angle = 0;
    private int axis2Angle = 0;
    private double axis0Angle = 0;
    //the Servo helpers for fluent movement
    private static HashMap<Integer, ServoHelper> ServoHelperINSTANCES = new HashMap<>();

    public AdvancedSimulator(){
        osirisSimulation = new OsirisSimulation();
    }

    private ServoHelper getServoHelperInstance(int axis, AdvancedSimulator advancedSimulator) {
        if (!ServoHelperINSTANCES.containsKey(axis))
            ServoHelperINSTANCES.put(axis, new ServoHelper(new SimulatedServo(axis, advancedSimulator), 1));
        return ServoHelperINSTANCES.get(axis);
    }

    /**
     * get the View
     * @return a View to be used in Android
     */
    public View getGameView(){
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        // has to be non-static for initializeForView so Singleton not possible
        return initializeForView(osirisSimulation, config);
    }

    @Override
    public void turnAxis(final int axis, int power) {
        ServoHelper servoHelper = getServoHelperInstance(axis, this);
        servoHelper.moveAtPower(power);
    }

    @Override
    public void stopAxis(int axis) {
        ServoHelper servoHelper = getServoHelperInstance(axis, this);
        servoHelper.moveAtPower(0);
    }

    @Override
    public void moveToAngle(int axis, int angle) {
        switch (axis) {
            case 0:
                osirisSimulation.turnAxis(axis, (float) (angle - axis0Angle));
                axis0Angle = angle;
                break;
            case 1:
                osirisSimulation.turnAxis(axis, (float) (angle - axis1Angle));
                axis1Angle = angle;
                break;
            case 2:
                osirisSimulation.turnAxis(axis, (float) (angle - axis2Angle));
                axis2Angle = angle;
                break;
        }
    }

    @Override
    public double getMaximumAngle(int axis) {
        switch (axis) {
            case 0:
                return 360;
            case 1:
                return 90;
            case 2:
                return 180;
        }
        return 0;
    }

    @Override
    public boolean moveTo(double x, double y, double z) {
        return false;
    }

    @Override
    public void sendMessage(Serializable msg) {
    }

    @Override
    public double getPosition(int axis) {
        switch (axis) {
            case 0:
                return axis0Angle;
            case 1:
                return axis1Angle;
            case 2:
                return axis2Angle;
        }
        return -1;
    }

    @Override
    public String getConnectionState() {
        return "Connected... its a simulation";
    }

    private class SimulatedServo implements Servo {
        int axis;
        private AdvancedSimulator advancedSimulator;

        public SimulatedServo(int axis, AdvancedSimulator advancedSimulator) {
            this.axis = axis;
            this.advancedSimulator = advancedSimulator;
        }

        @Override
        public void moveToAngle(int position) {
            advancedSimulator.moveToAngle(axis, position);
        }

        @Override
        public int getPositionInDegrees() {
            return (int) advancedSimulator.getPosition(axis);
        }

        @Override
        public int getMaximumAngle() {
            return (int) advancedSimulator.getMaximumAngle(axis);
        }

        @Override
        public long getTimePerDegreeInMilli() {
            return 2;
        }
    }
}
