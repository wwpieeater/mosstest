package net.mosstest.servercore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.BitSet;

// TODO: Auto-generated Javadoc
/**
 * The Class MossRemoteFile.
 */
public class MossRemoteFile extends MossFile {
	
	/**
	 * The Class IncompleteFileException.
	 */
	public class IncompleteFileException extends IOException {


		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -4932174729349395760L;

	}

	/** The Constant CHUNK_LENGTH. */
	public static final int CHUNK_LENGTH = 65536;

	/** The num chunks. */
	public final int numChunks;
	
	/** The length. */
	public final int length;
	
	/** The chunks done. */
	private BitSet chunksDone;

	/**
	 * Instantiates a new moss remote file.
	 *
	 * @param cacheDir the cache dir
	 * @param dirName the dir name
	 * @param resourceName the resource name
	 * @param length the length
	 * @throws FileNotFoundException the file not found exception
	 */
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

	/** The file. */
	private File file;

	/* (non-Javadoc)
	 * @see net.mosstest.servercore.MossFile#getRandAccessCopy()
	 */
	@Override
	public RandomAccessFile getRandAccessCopy() throws FileNotFoundException {
		return new RandomAccessFile(this.file, "rwd"); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see net.mosstest.servercore.MossFile#readChunk(int)
	 */
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

	/**
	 * Write chunk.
	 *
	 * @param chk the chk
	 * @param data the data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
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
	public File getFile() throws IncompleteFileException {
		if(!this.isReady()) throw new IncompleteFileException();
		return this.file;
	}

	/**
	 * Checks if is ready.
	 *
	 * @return true, if is ready
	 */
	public boolean isReady() {
		return (this.chunksDone.cardinality()==this.numChunks);
	}
}
