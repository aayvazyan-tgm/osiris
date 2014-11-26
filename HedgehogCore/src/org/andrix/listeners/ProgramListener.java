package org.andrix.listeners;

import java.util.ArrayList;
import java.util.List;

import org.andrix.deployment.Program;

public interface ProgramListener {
	
	public static List<ProgramListener> _l_program = new ArrayList<ProgramListener>();

	public void programAdded(Program program);

	public void programUpdated(Program program);

	public void programDeleted(Program program);
	
	public void versionsUpdated(Program prorgam);
}
