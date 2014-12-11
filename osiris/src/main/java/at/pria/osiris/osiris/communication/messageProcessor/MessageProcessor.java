package at.pria.osiris.osiris.communication.messageProcessor;

/**
 * @author Ari Ayvazyan
 * @version 03.Dec.14
 */
public interface MessageProcessor {
    abstract void processMessage(Object message);
}
