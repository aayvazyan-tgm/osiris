package at.pria.osiris.osiris.controllers;

/**
 * @author Ari Ayvazyan
 * @version 05.Dec.14
 */
public interface Controller {
    abstract ControllerSetup getSetup();
    abstract RobotArm getRobotArm() throws ConnectionNotEstablishedException;
}
