package at.pria.osiris.linker.implementation.hedgehog.communication;

import at.pria.osiris.linker.communication.messageProcessors.MessageProcessor;
import org.andrix.deployment.Program;
import org.andrix.listeners.ExecutionListener;

/**
 * @author Ari Ayvazyan
 * @version 03.Dec.14
 */
public class HedgehogDataListener implements ExecutionListener {
    private MessageProcessor messageProcessorDistributor;

    public HedgehogDataListener(MessageProcessor messageProcessor) {
        this.messageProcessorDistributor = messageProcessor;
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
}
