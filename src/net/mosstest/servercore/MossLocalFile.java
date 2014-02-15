package net.mosstest.servercore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

// TODO: Auto-generated Javadoc
/**
 * The Class MossLocalFile.
 */
public class MossLocalFile extends MossFile {
	
	/** The Constant CHUNK_LENGTH. */
	public static final int CHUNK_LENGTH = 65536;

	/**
	 * Instantiates a new moss local file.
	 *
	 * @param baseDir the base dir
	 * @param dirName the dir name
	 * @param resourceName the resource name
	 * @throws FileNotFoundException the file not found exception
	 */
	public MossLocalFile(File baseDir, String dirName, String resourceName) throws FileNotFoundException {
		// super call to establish fields.
		super(dirName, resourceName);
		//ensure filename is valid.
		if(!(dirName.matches("[a-zA-Z0-9.]*")&&resourceName.matches("[a-zA-Z0-9.]*"))) throw new FileNotFoundException();  //$NON-NLS-1$ //$NON-NLS-2$
		this.file = new File(baseDir, dirName);
		this.file = new File(this.file, resourceName);
	}

	/** The file. */
	private File file;

	/* (non-Javadoc)
	 * @see net.mosstest.servercore.MossFile#getRandAccessCopy()
	 */
	@Override
	public RandomAccessFile getRandAccessCopy() throws FileNotFoundException {
		return new RandomAccessFile(this.file, "r"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see net.mosstest.servercore.MossFile#readChunk(int)
	 */
	public byte[] readChunk(int chk) throws IOException {
		if ((chk < 0) || (chk > 65535))
			throw new IllegalArgumentException(
					"attempted to access a chunk with an invalid length"); //$NON-NLS-1$
		byte[] buf = new byte[CHUNK_LENGTH];
		RandomAccessFile rf = new RandomAccessFile(this.file, this.dirName);
		rf.seek(CHUNK_LENGTH * chk);
		rf.readFully(buf);
		rf.close();
		return buf;
	}

	/* (non-Javadoc)
	 * @see net.mosstest.servercore.MossFile#getSize()
	 */
	@Override
	public long getSize() {

		return this.file.length();

	}

	/* (non-Javadoc)
	 * @see net.mosstest.servercore.MossFile#getFile()
	 */
	public File getFile() {
		return this.file;
	}

}
