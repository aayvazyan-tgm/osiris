package org.andrix.listeners;

import java.util.ArrayList;
import java.util.List;

import org.andrix.deployment.Program;

public interface DeploymentListener {

	public static List<DeploymentListener> _l_deployment = new ArrayList<DeploymentListener>();

	public void compilationResult(Program program, int code, String message);

	public void fetchedProgram(Program program);

	public void fetchedProgramsDone();

	public void executionDone(Program program, byte[] result);
}
