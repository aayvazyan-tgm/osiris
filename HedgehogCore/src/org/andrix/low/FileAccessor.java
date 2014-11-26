package org.andrix.low;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileAccessor {
	
	public static FileAccess access;

	public static FileInputStream inputFile(String fileName) throws IOException {
		if (access == null)
			return null;
		return access.inputFile(fileName);
	}

	public static FileOutputStream outputFile(String fileName) throws IOException {
		if (access == null)
			return null;
		return access.outputFile(fileName);
	}
	
	public static File getMainDirectory() throws IOException {
		if (access == null)
			return null;
		return access.getMainDirectory();
	}
}
