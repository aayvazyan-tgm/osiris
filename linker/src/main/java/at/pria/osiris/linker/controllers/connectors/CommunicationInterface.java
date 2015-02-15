package at.pria.osiris.linker.controllers.connectors;

import java.io.Serializable;

/**
 * @author Ari Ayvazyan
 * @version 13.02.2015
 */
public interface CommunicationInterface {
    public void sendMessage(Serializable message);
    public void setupCommunication();
}
