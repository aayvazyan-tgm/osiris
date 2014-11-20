package org.andrix.low;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public interface FileAccess {
	public FileInputStream inputFile(String fileName) throws IOException;

	public FileOutputStream outputFile(String fileName) throws IOException;
	
	public File getMainDirectory() throws IOException;
}
