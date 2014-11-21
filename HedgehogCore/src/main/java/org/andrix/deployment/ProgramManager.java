/*
 * COPYRIGHT 2014 Markus Klein
 * Practical Robotics Institute Austria
 */

package org.andrix.deployment;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.andrix.json.JSONArray;
import org.andrix.listeners.ProgramListener;
import org.andrix.low.FileAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * One ProgramManager instance is responsible for managing the programs of
 * workspace. Every file system related operation (like saving, loading or
 * restoring) on a {@link org.andrix.deployment.Program} causes a method call on
 * it's underlying ProgramManager. Since every
 * {@link org.andrix.deployment.Program} needs a ProgramManager, it's
 * <code>newProgram</code> method is the only way to create new programs.
 * 
 * @author Markus Klein
 * @version 30-08-2014
 * @see org.andrix.deployment.Program
 */
public class ProgramManager {

	private static final Logger log = LoggerFactory.getLogger(ProgramManager.class);

	private static final String NEW_PROGRAM_CODE = "int main(int argc, char** argv) {\n\t//Write your code here\n\t//Library, stdlib, stdio is included\n}";
	public static final String PROGRAMS_FILE_NAME = ".hedgehog_programs";

	private File workspace = null;
	private File programsFile = null;
	private Map<String, Program> programs = null;

	private static Map<String, ProgramManager> programManagers = new HashMap<String, ProgramManager>();

	public static ProgramManager fetch(String workspaceDir) {
		return programManagers.get(workspaceDir);
	}

	public static ProgramManager create(String workspaceDir) throws IOException {
		if (programManagers.containsKey(workspaceDir))
			return programManagers.get(workspaceDir);
		ProgramManager manager = new ProgramManager(workspaceDir);
		programManagers.put(workspaceDir, manager);
		return manager;
	}

	/**
	 * Creates a new ProgramManager with the assigned workspace. The workspace
	 * does not have to exists, but if it exists, it must be a directory.
	 * Otherwise a {@link org.andrix.deployment.ProgramException} will be
	 * thrown.
	 * 
	 * @param workspace
	 *            workspace used for the ProgramManager
	 */
	private ProgramManager(String workspaceDir) throws IOException {
		workspace = new File(FileAccessor.getMainDirectory(), workspaceDir);
		if (!workspace.exists()) {
			workspace.mkdirs();
		} else if (!workspace.isDirectory()) {
			throw new IOException("The workspace `" + workspace.getAbsolutePath() + "` is not a directory.");
		}
		programsFile = new File(workspace, PROGRAMS_FILE_NAME);
		programs = new TreeMap<String, Program>();
		programsFile.createNewFile();
		log.info("Metafile successfully created!!");
		if (programsFile.length() > 0) {
			String programsString;
			try {
				byte[] buf = new byte[(int) programsFile.length()];
				DataInputStream dis = new DataInputStream(new FileInputStream(programsFile));
				dis.readFully(buf);
				dis.close();
				programsString = new String(buf);
			} catch (IOException e) {
				throw new IOException("The .andrix_programs file is your workspace is corrupted.");
			}
			JSONArray jsonPrograms = new JSONArray(programsString);
			for (int i = 0; i < jsonPrograms.length(); i++) {
				Program program = Program.fromJSON(jsonPrograms.getJSONObject(i), this);
				programs.put(program.getName() + "|" + program.getType(), program);
			}
		}
	}

	/**
	 * Returns a list of all Programs.
	 * 
	 * @return list of all programs
	 */
	public synchronized List<Program> getPrograms() {
		return new LinkedList<Program>(programs.values());
	}

	/* Only internal methods beyond this point */

	synchronized void saveProgram(Program program) throws IOException {
		saveProgram(program, program.getCurrentVersion(), program.getCode());
	}

	void saveProgram(Program program, int version, String code) throws FileNotFoundException {
		// save the actual code
		PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(workspace,
				program.getName() + version + "." + program.getType()))));
		out.print(code);
		out.close();
		for (ProgramListener listener : ProgramListener._l_program) {
			listener.programUpdated(program);
		}
	}

	synchronized void deleteProgram(Program program, int version) {
		new File(workspace, program.getName() + version + "." + program.getType()).delete();
		for (ProgramListener listener : ProgramListener._l_program) {
			listener.programUpdated(program);
		}
	}

	synchronized void deleteProgram(Program program) {
		for (Integer version : program.getVersions()) {
			new File(workspace, program.getName() + version + "." + program.getType()).delete();
		}
		programs.remove(program.getName() + "|" + program.getType());
		for (ProgramListener listener : ProgramListener._l_program) {
			listener.programDeleted(program);
		}
	}

	public synchronized Program newProgram(String name, String type) throws IOException {
		if (programs.get(name + "|" + type) != null)
			return null;

		Program program = new Program(name, type, this);
		programs.put(name + "|" + type, program);
		program.setCode(NEW_PROGRAM_CODE);
		saveMetadata();
		for (ProgramListener listener : ProgramListener._l_program) {
			listener.programAdded(program);
		}
		return program;
	}

	public synchronized boolean exists(String name, String type) {
		return programs.containsKey(name + "|" + type);
	}

	public synchronized Program copyProgram(String oldName, String newName, String type) throws IOException {
		if (programs.get(oldName + "|" + type) == null)
			return null;
		Program oldProgram = programs.get(oldName + "|" + type);
		Program newProgram = new Program(oldProgram, newName);
		programs.put(newName + "|" + type, newProgram);
		for (int version : oldProgram.getVersions())
			saveProgram(newProgram, version, oldProgram.getCode(version));
		saveMetadata();
		for (ProgramListener listener : ProgramListener._l_program)
			listener.programAdded(newProgram);
		return newProgram;
	}

	public synchronized Program getProgram(String name, String type) {
		return programs.get(name + "|" + type);
	}

	String loadProgramCode(Program program, int version) throws IOException {
		File file = new File(workspace, program.getName() + version + "." + program.getType());
		byte[] buf = new byte[(int) file.length()];
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		dis.readFully(buf);
		dis.close();
		return new String(buf);
	}

	long loadLastModified(Program program, int version) {
		return new File(workspace, program.getName() + version + "." + program.getType()).lastModified();
	}

	synchronized void saveMetadata() throws IOException {
		// update .andrix_programs
		JSONArray jsonPrograms = new JSONArray();
		for (Program program : programs.values()) {
			jsonPrograms.put(program.toJSON());
		}
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(programsFile));
		jsonPrograms.write(writer);
		writer.close();
	}
}