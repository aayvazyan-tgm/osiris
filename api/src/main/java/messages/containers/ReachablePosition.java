package messages.containers;

import messages.SerializableMessage;

/**
 * @author Ari Ayvazyan
 * @version 03.03.2015
 */
public class ReachablePosition extends SerializableMessage {
    private static final long serialVersionUID = 1L;
    private int x;
    private int y;
    private int z;
    private int timeToPerformActionInMilli;

    public ReachablePosition(int x, int y, int z, int timeToPerformActionInMilli) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.timeToPerformActionInMilli = timeToPerformActionInMilli;
    }

    /**
     * Getter for property 'timeToPerformActionInMilli'.
     *
     * @return Value for property 'timeToPerformActionInMilli'.
     */
    public int getTimeToPerformActionInMilli() {
        return timeToPerformActionInMilli;
    }

    /**
     * Getter for property 'z'.
     *
     * @return Value for property 'z'.
     */
    public int getZ() {
        return z;
    }

    /**
     * Getter for property 'y'.
     *
     * @return Value for property 'y'.
     */
    public int getY() {
        return y;
    }

    /**
     * Getter for property 'x'.
     *
     * @return Value for property 'x'.
     */
    public int getX() {
        return x;
    }
}
