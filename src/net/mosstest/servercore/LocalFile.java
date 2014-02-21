package net.mosstest.servercore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

public class LocalFile implements IMossFile {

	public String getFilename() {
		return f.getAbsolutePath();
	}

	static Logger logger = Logger.getLogger(LocalFile.class);
	private final File f;
	private final RandomAccessFile rFile;
	private final int numChunks;
	private final long length;
	private final String sha256;

	public LocalFile(File f) throws IOException {
		if (!f.canRead())
			throw new FileNotFoundException(
					"File not existent or cannot be read");
		this.f = f;
		this.rFile = new RandomAccessFile(f, "r");
		this.length = this.rFile.length();
		this.numChunks = (int) Math.ceil(this.length
				/ ((double) IMossFile.CHUNK_SIZE));
		try {
			this.sha256 = LocalFileManager.getHash(f);
			logger.info("Hashed " + f.getAbsolutePath() + " as " + this.sha256);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Could not find algorithm SHA-256 while hashing " + f.getAbsolutePath());
			throw new IOException("Hashing failed while preparing file");
		}
	}

	@Override
	public Reader getReader() throws FileNotFoundException {
		return new FileReader(this.f);
	}

	@Override
	public InputStream getInputStream() throws FileNotFoundException {
		return new FileInputStream(this.f);
	}

	@Override
	public byte[] readChunk(int chunkId) throws IOException {
		this.rFile.seek(chunkId * IMossFile.CHUNK_SIZE);
		byte[] buf = new byte[(int) (chunkId == (this.numChunks - 1) ? (this.length % IMossFile.CHUNK_SIZE)
				: IMossFile.CHUNK_SIZE)];
		this.rFile.readFully(buf);
		return buf;
	}

	@Override
	public void writeChunk(int chunkId, byte[] buf) throws IOException {
		throw new IOException(
				"An attempt was made to write to a read-only local file");
	}

	@Override
	public String getSha256() {
		return this.sha256;
	}

}
