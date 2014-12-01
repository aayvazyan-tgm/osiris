package linker.control;

import api.Stoppable;
import linker.model.Joint;
import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;

public class JointWatcher implements Runnable, Stoppable {

    private Joint joint;
    private boolean running;
    private boolean forward;

    public JointWatcher(Joint joint) {
        this.joint = joint;
    }

    private void runPositive() {
        while (running) {
            try {
                if (joint.getMax() < joint.getSensor().getValue()) {
                    joint.off();
                }
            } catch (NotConnectedException e) {
                e.printStackTrace();
            } catch (RequestTimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    private void runNegative() {
        while (running) {
            try {
                if (joint.getMin() > joint.getSensor().getValue()) {
                    joint.off();
                }
            } catch (NotConnectedException e) {
                e.printStackTrace();
            } catch (RequestTimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        if (running) return;

        running = true;

        if (forward) {
            runPositive();
        } else {
            runNegative();
        }

    }

    public boolean isRunning() {
        return running;
    }

    public boolean isForward() {
        return forward;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setForward(boolean forward) {
        this.forward = forward;
    }

}
