package messages.requests;

import messages.SerializableMessage;

/**
 * @author Ari Ayvazyan
 * @version 13.02.2015
 */
public class MoveAxisToAngleRequest extends SerializableMessage {
    private static final long serialVersionUID = 1L;
    private final int axisPort;
    private final int angle;
    /**
     * Defines a request for a sensor value
     *
     * @param requestedAxis axis id to request the value for.
     * @param angle
     */
    public MoveAxisToAngleRequest(int requestedAxis, int angle) {
        this.axisPort = requestedAxis;
        this.angle = angle;
    }

    /**
     * Getter for property 'axisPort'.
     *
     * @return Value for property 'axisPort'.
     */
    public int getAxisPort() {
        return axisPort;
    }

    /**
     * Setter for property 'angle'
     * @return
     */
    public int getAngle() {
        return angle;
    }
}
