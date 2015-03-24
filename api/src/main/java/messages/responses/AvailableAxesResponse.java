package messages.responses;

import messages.SerializableMessage;

import java.util.Arrays;
import java.util.List;

/**
 * Created by helmuthbrunner on 24/03/15.
 */
public class AvailableAxesResponse extends SerializableMessage {
    private static final long serialVersionUID = 1L;
    private List<String> axes;

    public AvailableAxesResponse(String ... axes) {
        this.setAxes(axes);
    }

    public void setAxes(String ... axes) {
        this.axes = Arrays.asList(axes);
    }

    /**
     * Returns the axes - names as a List
     * @return the axes - names as a List
     */
    public List<String> getAxes() {
        return this.axes;
    }


}
