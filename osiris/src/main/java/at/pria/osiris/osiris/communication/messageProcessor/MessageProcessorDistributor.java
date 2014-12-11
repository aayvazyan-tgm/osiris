package at.pria.osiris.osiris.communication.messageProcessor;

import Util.Serializer;
import api.Robotarm;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Sends a message via socket
 *
 * @author Adrian Bergler
 * @version 2014-10-17
 */
public class MessageProcessorDistributor {

    private List<MessageProcessor> messageProcessors;

    public MessageProcessorDistributor() {
        this.messageProcessors = new LinkedList<MessageProcessor>();
    }

    /**
     * Combined with a message-"receiver" this calls a method of the robotarm
     *
     * @param message the message
     */

    public void processMessage(byte[] message) {
        try {
            Object receivedMessage = Serializer.deserialize(message);
            for (MessageProcessor messageProcessor : messageProcessors) {
                messageProcessor.processMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addMessageProcessor(MessageProcessor messageProcessor) {
        this.messageProcessors.add(messageProcessor);
    }
}
