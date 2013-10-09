package net.mosstest.servercore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.commons.lang.NotImplementedException;

public abstract class MossFile {
	/**
	 * Get a local copy of the file. This may be either a direct local file or a
	 * cached file.
	 * 
	 * @return A valid RandomAccessFile.
	 * @throws FileNotFoundException 
	 */
	public abstract RandomAccessFile getRandAccessCopy() throws FileNotFoundException;


	/**
	 * String denoting the containing directory for the pathname.
	 */
	public final String dirName;

	public abstract byte[] readChunk(int chk) throws IOException;

	/**
	 * String denoting name of resource.
	 */
	public final String resourceName;
	

	public abstract long getSize();

	/**
	 * @param author
	 * @param plugin
	 * @param resourceName
	 */
	public MossFile(String dirName, String resourceName) {
		this.dirName=dirName;
		this.resourceName = resourceName;
	}

}
