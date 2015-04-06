package at.pria.osiris.osiris.util;

import api.Axis;

/**
 * @author Ari Michael Ayvazyan
 * @version 04.11.2014
 */
public class RoboArmConfig {
    private static RoboArmConfig INSTANCE = new RoboArmConfig();
    private final Object lockSelectedAxis = new Object();
    private int selectedAxis;
    private int percentPower = 50;

    private RoboArmConfig() {
    }

    public static RoboArmConfig getInstance() {
        return INSTANCE;
    }

    public int getSelectedAxis() {
        synchronized (lockSelectedAxis) {
            return selectedAxis;
        }
    }

    public void setSelectedAxis(int selectedAxis) {
        synchronized (lockSelectedAxis) {
            this.selectedAxis = selectedAxis;
        }
    }

    public int getPercentPower() {
        return percentPower;
    }

    public void setPercentPower(int percentPower) {
        this.percentPower = percentPower;
    }
}
