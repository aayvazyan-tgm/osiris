package at.pria.osiris.osiris.communication.messageProcessor;

import Util.Serializer;
import api.Robotarm;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Distributes messages to listeners.
 *
 * @author Ari Ayvazyan
 * @version 2014-10-17
 */
public class MessageProcessorDistributor implements MessageProcessor{

    private List<MessageProcessor> messageProcessors;

    /**
     * Distributes messages over its listeners,
     * the listeners need to find out by themselves if they have the correct object by calling instanceof.
     */
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
            processMessage(receivedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a MessageProcessor that receives messages.
     * @param messageProcessor the MessageProcessor to be added.
     */
    public void addMessageProcessor(MessageProcessor messageProcessor) {
        this.messageProcessors.add(messageProcessor);
    }

    @Override
    public void processMessage(Object message) {
        for (MessageProcessor messageProcessor : messageProcessors) {
            messageProcessor.processMessage(message);
        }
    }
}
