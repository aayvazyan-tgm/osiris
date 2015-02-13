package messages;

import java.io.Serializable;

/**
 * @author Ari Ayvazyan
 * @version 13.02.2015
 */
public abstract class SerializableMessage implements Serializable {

    private static int totalIDs = 0;
    private final long uniqueMessageID;

    /**
     * Generates a Unique message ID
     */
    public SerializableMessage() {
        this.uniqueMessageID = getUniqueMessageID();
    }

    /**
     *
     * @return returns the unique message ID of this message
     */
    public long getMessageID() {
        return uniqueMessageID;
    }

    /**
     * @return returns a unique message ID
     */
    private static synchronized long getUniqueMessageID() {
        totalIDs++;
        return totalIDs;
    }
}
