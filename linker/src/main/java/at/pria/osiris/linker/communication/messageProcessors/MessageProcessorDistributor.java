package at.pria.osiris.linker.communication.messageProcessors;

import Util.Serializer;
import messages.SerializableMessage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Distributes messages to listeners.
 *
 * @author Ari Ayvazyan
 * @version 13.02.2015
 */
public class MessageProcessorDistributor implements MessageProcessor {
    private static Logger logger = org.apache.log4j.Logger.getLogger(MessageProcessorDistributor.class);
    private List<MessageProcessor> messageProcessors;

    /**
     * Distributes messages over its listeners,
     * the listeners need to find out by themselves if they have the correct object by calling instanceof.
     */
    public MessageProcessorDistributor() {
        this.messageProcessors = new LinkedList<MessageProcessor>();
        logger.info("MessageProcessorDistributor started");
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
     *
     * @param messageProcessor the MessageProcessor/s to be added.
     */
    public void addMessageProcessor(MessageProcessor... messageProcessor) {
        Collections.addAll(this.messageProcessors, messageProcessor);
    }

    /**
     * Distributes the message
     *
     * @param message the message to distribute
     */
    @Override
    public void processMessage(Object message) {
        logger.info("Distributing message: "+message.getClass());
        if(message instanceof SerializableMessage) {
            SerializableMessage msg=((SerializableMessage) message);
            logger.info("Distributing Serializable msg ID: " +msg.getMessageID());
        }
        for (MessageProcessor messageProcessor : messageProcessors) {
            messageProcessor.processMessage(message);
        }
    }
}
