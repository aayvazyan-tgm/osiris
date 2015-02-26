package messages.requests;

import messages.SerializableMessage;

/**
 * @author Ari Ayvazyan
 * @version 13.02.2015
 */
public class SensorValueRequest extends SerializableMessage {
    private static final long serialVersionUID = 1L;
    private final int sensorPort;

    /**
     * Defines a request for a sensor value
     *
     * @param sensorPort the port to request the value for.
     */
    public SensorValueRequest(int sensorPort) {
        this.sensorPort = sensorPort;
    }

    /**
     * Getter for property 'sensorPort'.
     *
     * @return Value for property 'sensorPort'.
     */
    public int getSensorPort() {
        return sensorPort;
    }

    /**
     * Setter for property 'sensorPort'.
     *
     * @param sensorPort Value to set for property 'sensorPort'.
     */
}
