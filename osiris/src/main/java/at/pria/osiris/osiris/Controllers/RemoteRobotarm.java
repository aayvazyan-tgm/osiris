package at.pria.osiris.osiris.controllers;

import api.Axis;

/**
 * @author Ari Ayvazyan
 * @version 05.Dec.14
 */
public interface RemoteRobotarm {

    abstract void turnAxis(Axis axis, int power);

    abstract void turnAxis(Axis axis, int power, long timeInMillis);

    abstract void stopAxis(Axis axis);

    abstract boolean moveTo(double x, double y, double z);

    abstract void stopAll();

    /**
     * Close sockets, kill watchdogs
     */
    abstract void close();

    abstract void test();

    abstract void exit();
}
