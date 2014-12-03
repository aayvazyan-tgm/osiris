package at.pria.osiris.osiris.communication;

import at.pria.osiris.osiris.communication.MessageProcessor.MessageProcessorDistributor;
import org.andrix.deployment.Program;
import org.andrix.listeners.ExecutionListener;

/**
 * @author Ari Ayvazyan
 * @version 03.Dec.14
 */
public class DataListener implements ExecutionListener {
    private MessageProcessorDistributor messageProcessorDistributor;

    public DataListener(MessageProcessorDistributor messageProcessorDistributor) {
        this.messageProcessorDistributor = messageProcessorDistributor;
    }

    @Override
    public void executionDone(Program program, int i, int i1) {

    }

    /*
     * Is received when AXCP.EXECUTION_DATA_ACTION is called.
     */
    @Override
    public void executionDataReceived(Program program, int i, byte[] bytes) {
        this.messageProcessorDistributor.processMessage(bytes);
    }
}
