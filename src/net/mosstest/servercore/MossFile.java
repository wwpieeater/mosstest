package net.mosstest.servercore;

import java.io.File;

public interface MossFile {
	/**
	 * Get a local copy of the file. This may be either a direct local file or a
	 * cached file.
	 * 
	 * @return A valid input stream.
	 */
	public File getLocalCopy();

	public int getSize();
}
