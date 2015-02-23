package messages.requests;

import messages.SerializableMessage;

/**
 * @author Ari Ayvazyan
 * @version 13.02.2015
 */
public class MoveAxisRequest extends SerializableMessage {
    private int axisPort;
    private int power;
    /**
     * Defines a request for a sensor value
     *
     * @param requestedAxis axis id to request the value for.
     */
    public MoveAxisRequest(int requestedAxis) {
        this.axisPort = requestedAxis;
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
     * Setter for property 'axisPort'.
     *
     * @param axisPort Value to set for property 'axisPort'.
     */
    public void setAxisPort(int axisPort) {
        this.axisPort = axisPort;
    }

    /**
     * Setter for property 'power'
     * @return
     */
    public int getPower() {
        return power;
    }

    /**
     * Setter for property 'power'
     * @param power
     */
    public void setPower(int power) {
        this.power = power;
    }
}
