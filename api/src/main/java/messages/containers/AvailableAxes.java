package messages.containers;

import messages.SerializableMessage;

import java.util.Arrays;
import java.util.List;

/**
 * Created by helmuthbrunner on 24/03/15.
 */
public class AvailableAxes extends SerializableMessage {
    private static final long serialVersionUID = 1L;
    private List<String> name;

    public AvailableAxes(String ... axes) {
        this.setAxes(axes);
    }

    /**
     * Setter for property 'axes'.
     *
     * @return Value for property 'axes'.
     */
    public void setAxes(String ... axes) {
        this.name= Arrays.asList(axes);
    }

    /**
     * Getter for property 'axes'.
     *
     * @return Value for property 'axes'.
     */
    public List<String> getAxes() {
        return this.name;
    }
}
