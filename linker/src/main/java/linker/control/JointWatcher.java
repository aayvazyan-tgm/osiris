package linker.control;

import linker.model.Joint;
import api.Stoppable;

public class JointWatcher implements Runnable, Stoppable{

	private Joint joint;
	private boolean running;
	private boolean forward;
	
	public JointWatcher(Joint joint){
		this.joint = joint;
	}
	
	private void runPositive(){
		while(running){
			if(joint.getMax() < joint.getSensor().getValue()){
				joint.off();
			}
		}
	}
	
	private void runNegative(){
		while(running){
			if(joint.getMin() > joint.getSensor().getValue()){
				joint.off();
			}
		}
	}
	
	@Override
	public void stop() {
		running = false;
	}

	@Override
	public void run() {
		if(running) return;
		
		running = true;
		
		if(forward){
			runPositive();
		}else{
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
