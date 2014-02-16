package net.mosstest.servercore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

// TODO: Auto-generated Javadoc
/**
 * The Class MossFile.
 */
public abstract class MossFile {
	
	/**
	 * Get a local copy of the file. This may be either a direct local file or a
	 * cached file.
	 *
	 * @return A valid RandomAccessFile.
	 * @throws FileNotFoundException the file not found exception
	 */
	public abstract RandomAccessFile getRandAccessCopy() throws FileNotFoundException;


	/**
	 * String denoting the containing directory for the pathname.
	 */
	public final String dirName;

	/**
	 * Read chunk.
	 *
	 * @param chk the chk
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public abstract byte[] readChunk(int chk) throws IOException;

	/**
	 * String denoting name of resource.
	 */
	public final String resourceName;
	

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public abstract long getSize();

	/**
	 * Instantiates a new moss file.
	 *
	 * @param dirName the dir name
	 * @param resourceName the resource name
	 */
	public MossFile(String dirName, String resourceName) {
		this.dirName=dirName;
		this.resourceName = resourceName;
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public abstract File getFile() throws IOException;

}
