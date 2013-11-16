package net.mosstest.servercore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.BitSet;

public class MossRemoteFile extends MossFile {
	public class IncompleteFileException extends IOException {


		private static final long serialVersionUID = -4932174729349395760L;

	}

	public static final int CHUNK_LENGTH = 65536;

	public final int numChunks;
	public final int length;
	private BitSet chunksDone;

	public MossRemoteFile(File cacheDir, String dirName, String resourceName,
			int length) throws FileNotFoundException {
		// super call to establish fields.
		super(dirName, resourceName);
		//ensure filename is valid.
		if(!(dirName.matches("[a-zA-Z0-9]*")&&resourceName.matches("[a-zA-Z0-9]*"))) throw new FileNotFoundException();  //$NON-NLS-1$ //$NON-NLS-2$
		this.file = new File(cacheDir, dirName);
		this.file = new File(this.file, resourceName);
		this.length = length;
		this.numChunks = (length / 65536) + 1;
		this.chunksDone = new BitSet(this.numChunks);
	}

	private File file;

	@Override
	public RandomAccessFile getRandAccessCopy() throws FileNotFoundException {
		return new RandomAccessFile(this.file, "rwd"); //$NON-NLS-1$
	}

	public byte[] readChunk(int chk) throws IOException {
		if ((chk < 0) || (chk > 65535))
			throw new IllegalArgumentException(
					"attempted to access a chunk with an invalid length"); //$NON-NLS-1$
		byte[] buf = new byte[CHUNK_LENGTH];
		RandomAccessFile rf = new RandomAccessFile(this.file, "r"); //$NON-NLS-1$
		rf.seek(CHUNK_LENGTH * chk);
		rf.readFully(buf);
		rf.close();
		return buf;
	}

	public void writeChunk(int chk, byte[] data) throws IOException {
		// this will check if the chunk is the last one. If it is then it will
		// use only the correct number of bytes. Otherwise it will use
		// CHUNK_LENGTH.
		if (data.length != ((chk == this.numChunks - 1) ? this.length
				% CHUNK_LENGTH : CHUNK_LENGTH))
			throw new ArrayIndexOutOfBoundsException("Array is not 65536 bytes"); //$NON-NLS-1$
		RandomAccessFile rf = new RandomAccessFile(this.file, "rwd"); //$NON-NLS-1$
		rf.seek(chk*CHUNK_LENGTH);
		rf.write(data);
		this.chunksDone.set(chk, true);
		rf.close();
	}

	@Override
	public long getSize() {

		return this.file.length();

	}
	
	public File getFile() throws IncompleteFileException {
		if(!this.isReady()) throw new IncompleteFileException();
		return this.file;
	}

	public boolean isReady() {
		return (this.chunksDone.cardinality()==this.numChunks);
	}
}
