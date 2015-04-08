package at.pria.osiris.osiris.controllers;

import java.io.Serializable;

/**
 * The Robot arm interface defines actions a Robotarm should be able to perform
 *
 * @author Ari Ayvazyan
 * @version 23.03.2015
 */
public interface RobotArm {

    /**
     * Turns an axis of the robotarm with the given power
     * (turns until the axis gets stopped)
     *
     * @param axis  the axis
     * @param power the power may contain values from -100 to 100, 0 is defined as a stop and 100 is defined as the maximum power in the positive direction.
     */
    public void turnAxis(int axis, int power);

    public void stopAxis(int axis);
    /**
     * Turns an axis of the robotarm with the given power
     * (turns until the axis gets stopped)
     *
     * @param axis  the axis
     * @param angle the angle in degrees
     */
    public void moveToAngle(int axis, int angle);

    /**
     * Turns an axis of the robotarm with the given power
     * (turns until the axis gets stopped)
     *
     * @param axis  the axis
     */
    public double getMaximumAngle(int axis);

    /**
     * Moves to the given position
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @return false if the position cant be reached, true if the robot arm is able to move there and starts to move
     */
    public boolean moveTo(double x, double y, double z);

    /**
     * sends a message to the controller
     * @param msg the message
     */
    public void sendMessage(Serializable msg);

    /**
     *
     * @param axis the axis to request the position for
     * @return the current position in degrees
     */
    public double getPosition(int axis);
}
