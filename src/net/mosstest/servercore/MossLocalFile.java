package net.mosstest.servercore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MossLocalFile extends MossFile {
	public static final int CHUNK_LENGTH = 65536;

	public MossLocalFile(File baseDir, String dirName, String resourceName) throws FileNotFoundException {
		// super call to establish fields.
		super(dirName, resourceName);
		//ensure filename is valid.
		if(!(dirName.matches("[a-zA-Z0-9.]*")&&resourceName.matches("[a-zA-Z0-9.]*"))) throw new FileNotFoundException();  //$NON-NLS-1$ //$NON-NLS-2$
		this.file = new File(baseDir, dirName);
		this.file = new File(this.file, resourceName);
	}

	private File file;

	@Override
	public RandomAccessFile getRandAccessCopy() throws FileNotFoundException {
		return new RandomAccessFile(this.file, "r"); //$NON-NLS-1$
	}

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

	@Override
	public long getSize() {

		return this.file.length();

	}

	public File getFile() {
		return this.file;
	}

}
