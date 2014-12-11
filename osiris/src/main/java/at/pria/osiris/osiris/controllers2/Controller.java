package at.pria.osiris.osiris.controllers2;

import api.Robotarm;

/**
 * @author Ari Ayvazyan
 * @version 05.Dec.14
 */
public interface Controller {
    abstract ControllerSetup getSetup();
    abstract Robotarm getRobotArm() throws ConnectionNotEstablishedException;
}
