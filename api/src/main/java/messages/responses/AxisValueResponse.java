package messages.responses;

import messages.SerializableMessage;

/**
 * @author Ari Ayvazyan
 * @version 13.02.2015
 */

public class AxisValueResponse extends SerializableMessage {
    private static final long serialVersionUID = 1L;
    private final int axisNumber;
    private final int axisValue;

    public AxisValueResponse(int axisNumber, int axisValue) {
        this.axisNumber = axisNumber;
        this.axisValue = axisValue;
    }

    public int getAxisValue() {
        return axisValue;
    }

    public int getAxisNumber() {
        return axisNumber;
    }
}
