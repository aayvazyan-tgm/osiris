/*
 * COPYRIGHT 2014 Markus Klein
 * Practical Robotics Institute Austria
 */

package org.andrix.deployment;

import org.andrix.AXCP;
import org.andrix.json.JSONArray;
import org.andrix.json.JSONObject;
import org.andrix.listeners.ProgramListener;
import org.andrix.low.NotConnectedException;
import org.andrix.low.RequestTimeoutException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * An instance of this class represents one AndriX program with it's name, type,
 * current version as well multiple saved versions where each version has it's
 * own code and a last modified timestamp. A Program can only be instantiated by
 * a {@link org.andrix.deployment.ProgramManager}. Every program has one current
 * version which is writeable while all former versions are read-only. Note that
 * the code of each version does not have any kind of relationship with the
 * other version's source. (There a no diffs saved.) That means there is no
 * possibility to jump back and forth on the history. The only way to get back
 * to an old version is to restore it. This means to save the old version into a
 * new version which is then the current version. The Program class provides
 * methods for all these operations (save code, retrive code, save as new
 * version, reset to version and delete version).
 * 
 * @author Markus Klein
 * @version 30-08-2014
 * @see org.andrix.deployment.ProgramManager
 */
public class Program implements Comparable<Program> {

	private transient ProgramManager programManager;

	Set<Integer> versions;
	transient Map<Integer, Long> versionsLastModified;
	final String name;
	final String type;

	int currentVersion;
	transient String code = null;

	private Program(String name, String type) {
		this.name = name;
		this.type = type;
		versionsLastModified = new HashMap<Integer, Long>();
	}

	Program(String name, String type, ProgramManager programManager) {
		this.name = name;
		this.type = type;
		this.programManager = programManager;
		versions = new TreeSet<Integer>(Collections.reverseOrder());
		versionsLastModified = new HashMap<Integer, Long>();
	}

	Program(Program program, String newName) {
		this.name = newName;
		this.type = program.type;
		this.versions = new TreeSet<Integer>(program.versions);
		versionsLastModified = new HashMap<Integer, Long>();
		this.currentVersion = program.currentVersion;
		this.programManager = program.programManager;
	}

	/**
	 * Returns the program's name.
	 * 
	 * @return program's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the program's type which specifies the type of source code (aka
	 * the programming language) for this program.
	 * 
	 * @return program's type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns the current version number.
	 * 
	 * @return current version number
	 */
	public synchronized int getCurrentVersion() {
		return currentVersion;
	}

	/**
	 * Returns the current versions's code. This operation is buffered which
	 * means calling it two times does not cause to file reads.
	 * 
	 * @return current versions's code
	 * @throws IOException
	 */
	public synchronized String getCode() throws IOException {
		if (code == null)
			code = programManager.loadProgramCode(this, currentVersion);
		return code;
	}

	/**
	 * Returns the code of a specific version. Note that the code of other
	 * versions than the current version will not be cached, so this method has
	 * a much poorer performance than retrieving the current version's code.
	 * 
	 * @param version
	 *            version number of the code that should be returned
	 * @return code of a specific version
	 * @throws IOException
	 */
	public synchronized String getCode(int version) throws IOException {
		if (version == currentVersion) {
			return getCode();
		} else {
			return programManager.loadProgramCode(this, version);
		}
	}

	/**
	 * Returns a list of all versions.
	 * 
	 * @return list of all versions
	 */
	public synchronized List<Integer> getVersions() {
		return new ArrayList<Integer>(versions);
	}

	/**
	 * Returns the last modification time of the current version's code.
	 * 
	 * @return last modification time of the current version's code
	 */
	public synchronized long getLastModified() {
		return getLastModified(currentVersion);
	}

	/**
	 * Returns the last modification time of a specified version.
	 * 
	 * @param version
	 *            The version of which the last modification should be
	 *            retrieved.
	 * @return last modification time of a specified version
	 */
	public synchronized long getLastModified(int version) {
		if (!versions.contains(version))
			return 0;
		Long lastModified = versionsLastModified.get(version);
		if (lastModified == null) {
			lastModified = programManager.loadLastModified(this, version);
			versionsLastModified.put(version, lastModified);
		}
		return lastModified;
	}

	/**
	 * Saves the code under to current version.
	 * 
	 * @param code
	 *            code to save
	 * @throws IOException
	 */
	public synchronized void setCode(String code) throws IOException {
		this.code = code;
		programManager.saveProgram(this);
		// invalidate current versions's last modified
		versionsLastModified.remove(currentVersion);
	}

	/**
	 * Restores an old version by setting the old version's code as the code of
	 * a new (current) version.
	 * 
	 * @param version
	 *            the version which should be restored
	 * @throws IOException
	 */
	public synchronized void restoreVersion(int version) throws IOException {
		setCodeAsNewVersion(getCode(version));
	}

	/**
	 * Saves the assigned code as a new version.
	 * 
	 * @param code
	 *            the code which should be saved
	 * @throws IOException
	 */
	public synchronized void setCodeAsNewVersion(String code) throws IOException {
		currentVersion++;
		versions.add(currentVersion);
		this.code = code;
		programManager.saveProgram(this);
		programManager.saveMetadata();
		for (ProgramListener l : ProgramListener._l_program)
			l.versionsUpdated(this);
	}

	/**
	 * Deletes a single version. Notice that the current version cannot be
	 * deleted. If the assigned version == current version, the version will not
	 * be deleted.
	 * 
	 * @param version
	 *            version to delete
	 * @throws IOException
	 */
	public synchronized void deleteVersion(Integer version) throws IOException {
		if (version.equals(currentVersion)) {
			return;
		}
		versions.remove(version);
		versionsLastModified.remove(version);
		programManager.deleteProgram(this, version);
		programManager.saveMetadata();
		for (ProgramListener l : ProgramListener._l_program)
			l.versionsUpdated(this);
	}

	/**
	 * Deletes the whole program including all saved version. After this method
	 * has been called the should must not be used.
	 * 
	 * @throws IOException
	 */
	public synchronized void delete() throws IOException {
		programManager.deleteProgram(this);
		versions = new TreeSet<Integer>();
		versionsLastModified = new HashMap<Integer, Long>();
		programManager.saveMetadata();
	}

	/**
	 * Sends a compile request to the controller.
	 * 
	 * @return <code>Object[2]</code> where <code>Object[0]</code> is the
	 *         compiler return code and <code>Object[1]</code> is the compiler
	 *         message.
	 * @throws NotConnectedException
	 *             If the connection to the controller fails.
	 * @throws RequestTimeoutException
	 */
	public synchronized Compilation compile() throws NotConnectedException, RequestTimeoutException {
		Object[] result = (Object[]) AXCP.command(AXCP.PROGRAM_COMPILE_REQUEST, name, currentVersion, code);
		return new Compilation((Integer) result[0], (String) result[1]);
	}

	/**
	 * Executes the current version of the program.
	 * 
	 * @throws NotConnectedException
	 *             If the connection to the controller fails.
	 */
	public synchronized void execute() throws NotConnectedException {
		execute(currentVersion);
	}

	/**
	 * Executes the specified version of the program.
	 * 
	 * @param version
	 *            version which should be executed
	 * @throws NotConnectedException
	 *             If the connection to the controller fails.
	 */
	public synchronized void execute(int version) throws NotConnectedException {
		try {
			AXCP.command(AXCP.PROGRAM_EXECUTE_ACTION, name, version);
		} catch (RequestTimeoutException ex) {
		}
	}

	/**
	 * Compiles and executes the current version
	 * 
	 * @return <code>Object[2]</code> where <code>Object[0]</code> is the
	 *         compiler return code and <code>Object[1]</code> is the compiler
	 *         message.
	 * @throws NotConnectedException
	 *             If the connection to the controller fails.
	 * @throws RequestTimeoutException
	 */
	public synchronized Compilation compileAndExecute() throws NotConnectedException, RequestTimeoutException {
		Object[] result = (Object[]) AXCP.command(AXCP.PROGRAM_COMPILE_EXECUTE_REQUEST, name, currentVersion, code);
		return new Compilation((Integer) result[0], (String) result[1]);
	}

	/**
	 * Sends execution data to the SW controller for a specific program in its
	 * current version. Attention: This method does not check if the program
	 * currently is executing, nor if it even was deployed!
	 * 
	 * @param data
	 * @throws NotConnectedException
	 */
	public synchronized void sendExecutionData(byte[] data) throws NotConnectedException {
		try {
			AXCP.command(AXCP.EXECUTION_DATA_EXCHANGE, name, currentVersion, data);
		} catch (RequestTimeoutException e) {
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Program program = (Program) o;

		if (!name.equals(program.name))
			return false;
		if (!type.equals(program.type))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = versions.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + type.hashCode();
		return result;
	}

	public int compareTo(Program another) {
		int modifiedCompare = -new Long(this.getLastModified()).compareTo(new Long(another.getLastModified()));
		if (modifiedCompare == 0) {
			int nameCompare = this.name.compareTo(another.name);
			if (nameCompare == 0) {
				return this.type.compareTo(another.type);
			} else {
				return nameCompare;
			}
		} else {
			return modifiedCompare;
		}
	}

	JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("name", name);
		obj.put("type", type);
		obj.put("currentVersion", currentVersion);
		obj.put("versions", versions.toArray());
		return obj;
	}

	static Program fromJSON(JSONObject object, ProgramManager manager) {
		Program program = new Program(object.getString("name"), object.getString("type"));
		program.currentVersion = object.getInt("currentVersion");
		program.versions = new TreeSet<>(Collections.reverseOrder());
		JSONArray jsonVersions = object.getJSONArray("versions");
		for (int i = 0; i < jsonVersions.length(); i++) {
			program.versions.add(jsonVersions.getInt(i));
		}
		program.programManager = manager;
		return program;
	}

	public class Compilation {
		private int compilerResult;
		private String compilerMessage;

		public Compilation(int compilerResult, String compilerMessage) {
			this.compilerResult = compilerResult;
			this.compilerMessage = compilerMessage;
		}

		public String getCompilerMessage() {
			return compilerMessage;
		}

		public int getCompilerResult() {
			return compilerResult;
		}

	}
}