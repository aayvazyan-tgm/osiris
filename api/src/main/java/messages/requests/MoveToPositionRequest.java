package messages.requests;

import messages.SerializableMessage;

/**
 * @author Ari Ayvazyan
 * @version 13.02.2015
 */
public class MoveToPositionRequest extends SerializableMessage {
    private static final long serialVersionUID = 1L;
    private final int x;
    private final int y;
    private final int z;

    public MoveToPositionRequest(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Getter for property 'x'.
     *
     * @return Value for property 'x'.
     */
    public int getX() {
        return x;
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
     * Getter for property 'z'.
     *
     * @return Value for property 'z'.
     */
    public int getZ() {
        return z;
    }
}
