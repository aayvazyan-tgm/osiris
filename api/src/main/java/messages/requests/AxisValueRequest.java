package messages.requests;

import messages.SerializableMessage;

/**
 * @author Ari Ayvazyan
 * @version 13.02.2015
 */
public class AxisValueRequest extends SerializableMessage {
    private static final long serialVersionUID = 1L;
    private final int axisNumber;

    /**
     * Defines a request for a sensor value
     *
     * @param axisNumber the port to request the value for.
     */
    public AxisValueRequest(int axisNumber) {
        this.axisNumber = axisNumber;
    }


    /**
     * Getter for property 'axisNumber'.
     *
     * @return Value for property 'axisNumber'.
     */
    public int getAxisNumber() {
        return axisNumber;
    }

    /**
     * Setter for property 'axisNumber'.
     *
     * @param axisNumber Value to set for property 'axisNumber'.
     */
}
