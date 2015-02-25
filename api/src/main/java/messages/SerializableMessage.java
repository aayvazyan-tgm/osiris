package messages;

import java.io.Serializable;

/**
 * @author Ari Ayvazyan
 * @version 13.02.2015
 */
public abstract class SerializableMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int totalIDs = 0;
    private final long uniqueMessageID;
    private final long creationTime;

    /**
     * Generates a Unique message ID
     */
    public SerializableMessage() {
        this.uniqueMessageID = getUniqueMessageID();
        this.creationTime = System.currentTimeMillis();
    }

    /**
     * @return returns a unique message ID
     */
    private static synchronized long getUniqueMessageID() {
        totalIDs++;
        return totalIDs;
    }

    /**
     * Getter for property 'creationTime'.
     *
     * @return Value for property 'creationTime'.
     */
    public long getCreationTime() {
        return creationTime;
    }

    /**
     * @return returns the unique message ID of this message
     */
    public long getMessageID() {
        return uniqueMessageID;
    }
}
