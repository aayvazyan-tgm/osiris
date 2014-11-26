package org.andrix.listeners;

import java.util.ArrayList;
import java.util.List;

import org.andrix.deployment.Program;

public interface ExecutionListener {

	public static List<ExecutionListener> _l_exec = new ArrayList<ExecutionListener>();

	public void executionDone(Program program, int version, int returnValue);
	
	public void executionDataRecieved(Program program, int version, byte[] data);
}
