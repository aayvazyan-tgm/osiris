package at.pria.osiris.osiris.communication;

import at.pria.osiris.osiris.communication.messageProcessor.MessageProcessor;
import at.pria.osiris.osiris.communication.messageProcessor.MessageProcessorDistributor;
import org.andrix.deployment.Program;
import org.andrix.listeners.ExecutionListener;

/**
 * @author Ari Ayvazyan
 * @version 03.Dec.14
 */
public class DataListener implements ExecutionListener {
    private MessageProcessorDistributor messageProcessorDistributor;

    public DataListener() {
        this.messageProcessorDistributor = new MessageProcessorDistributor();
    }

    @Override
    public void executionStarted(Program program, int i) {

    }

    @Override
    public void executionStopped(Program program, int i) {

    }

    @Override
    public void executionDone(Program program, int i, int i1) {

    }

    @Override
    public void executionOutput(Program program, int i, String s) {

    }

    /*
     * Is received when AXCP.EXECUTION_DATA_ACTION is called.
     */
    @Override
    public void executionDataReceived(Program program, int i, byte[] bytes) {
        this.messageProcessorDistributor.processMessage(bytes);
    }

    @Override
    public void executionBreaked(Program program, int i, int i1, String[] strings) {

    }

    public void addMessageProcessor(MessageProcessor messageProcessor){
        this.messageProcessorDistributor.addMessageProcessor(messageProcessor);
    }
}
