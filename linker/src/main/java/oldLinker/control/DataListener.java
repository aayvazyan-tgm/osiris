package oldLinker.control;

import oldLinker.control.MessageProcessor.MessageProcessorDistributor;
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
