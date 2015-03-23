package at.pria.osiris.osiris.controllers;

import at.pria.osiris.osiris.communication.DataListener;

/**
 * @author Ari Ayvazyan
 * @version 03.Dec.14
 */
public interface ControllerSetup {
    public void setup(RobotArm robotArm);
    public DataListener getDataListener();
}
