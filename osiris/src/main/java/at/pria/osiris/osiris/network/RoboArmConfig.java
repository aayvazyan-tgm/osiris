package at.pria.osiris.osiris.network;

import at.pria.osiris.osiris.api.Axis;

/**
 * @author Ari Michael Ayvazyan
 * @version 04.11.2014
 */
public class RoboArmConfig {
    Object lockSelectedAxis = new Object();
    private Axis selectedAxis;
    private static RoboArmConfig INSTANCE = new RoboArmConfig();
    private int percentPower=0;

    private RoboArmConfig() {
    }

    public static RoboArmConfig getInstance() {
        return INSTANCE;
    }

    public Axis getSelectedAxis() {
        synchronized (lockSelectedAxis) {
            return selectedAxis;
        }
    }

    public void setSelectedAxis(Axis selectedAxis) {
        synchronized (lockSelectedAxis) {
            this.selectedAxis = selectedAxis;
        }
    }

    public void setSelectedAxis(String selectedAxis) {
        synchronized (lockSelectedAxis) {
            this.selectedAxis = Axis.valueOf(selectedAxis);
        }
    }

    public int getPercentPower() {
        return percentPower;
    }

    public void setPercentPower(int percentPower) {
        this.percentPower = percentPower;
    }
}
