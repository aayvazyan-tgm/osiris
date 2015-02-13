package messages.requests;

import messages.SerializableMessage;

/**
 * @author Ari Ayvazyan
 * @version 13.02.2015
 */
public class SensorValueRequest extends SerializableMessage {
    private int sensorPort;

    /**
     * Defines a request for a sensor value
     *
     * @param sensorPort
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
    public void setSensorPort(int sensorPort) {
        this.sensorPort = sensorPort;
    }
}
