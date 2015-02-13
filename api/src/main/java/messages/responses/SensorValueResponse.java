package messages.responses;

import messages.SerializableMessage;

/**
 * @author Ari Ayvazyan
 * @version 13.02.2015
 */

public class SensorValueResponse extends SerializableMessage {
    private final int sensorNumber;
    private final int sensorValue;

    public SensorValueResponse(int sensorNumber,int sensorValue) {
        this.sensorNumber = sensorNumber;
        this.sensorValue = sensorValue;
    }

    /**
     * Getter for property 'sensorNumber'.
     *
     * @return Value for property 'sensorNumber'.
     */
    public int getSensorNumber() {
        return sensorNumber;
    }
    /**
     * Getter for property 'sensorValue'.
     *
     * @return Value for property 'sensorValue'.
     */
    public int getSensorValue() {
        return sensorValue;
    }
}
