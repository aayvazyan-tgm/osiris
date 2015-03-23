package messages.requests;

import messages.SerializableMessage;

/**
 * @author Ari Ayvazyan
 * @version 13.02.2015
 */
public class MoveAxisToangleRequest extends SerializableMessage {
    private static final long serialVersionUID = 1L;
    private final int axisPort;
    private final int power;
    /**
     * Defines a request for a sensor value
     *
     * @param requestedAxis axis id to request the value for.
     * @param power
     */
    public MoveAxisToangleRequest(int requestedAxis, int power) {
        this.axisPort = requestedAxis;
        this.power = power;
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
     * Setter for property 'power'
     * @return
     */
    public int getPower() {
        return power;
    }
}
