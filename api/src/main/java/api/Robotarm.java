package api;

import java.io.Serializable;

/**
 * A Robotarm
 *
 * @author Adrian Bergler
 * @version 2014-10-17
 */
public interface Robotarm {

    /**
     * Turns an axis of the robotarm with the given power
     * (turns until the axis gets stopped)
     *
     * @param axis  the axis (use the enum)
     * @param power the power
     */
    public void turnAxis(Axis axis, int power);

    /**
     * Turns an axis of the robotarm with the given power for the given time
     * (can also be stopped using stop)
     *
     * @param axis       the axis (use the enum)
     * @param power      the power
     * @param timemillis the time
     */
    public void turnAxis(Axis axis, int power, long timemillis);

    /**
     * Stops an axis
     *
     * @param axis the axis (use the enum)
     */
    public void stopAxis(Axis axis);

    /**
     * Moves to the given position
     * (can be stopped using stop)
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @return if the position can be reached
     */
    public boolean moveTo(double x, double y, double z);

    /**
     * Stops all motors
     */
    public void stopAll();

    /**
     * Kills the sensor-watchdog
     */
    public void close();

    /**
     * Tests the connection
     */
    public void test();

    /**
     * Exits the Linker
     * TODO probably move this somewhere else as it isnt really related to the Robotarm
     */
    public void exit();

    /**
     *
     * @return returns the Maximum Possible Power
     */
    public double getMaxMovePower();

    public void sendMessage(Serializable msg);
}
