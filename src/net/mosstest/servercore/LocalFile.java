package net.mosstest.servercore;

import org.apache.log4j.Logger;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;

public class LocalFile implements IMossFile {

	public String getFilename() {
		return f.getAbsolutePath();
	}

	static Logger logger = Logger.getLogger(LocalFile.class);
	private final File f;
	private final RandomAccessFile rFile;

    public LocalFile(File f, int numChunks, long length, String name, String sha256) throws FileNotFoundException {
        this.f = f;
        this.rFile = new RandomAccessFile(f, "r");
        this.numChunks = numChunks;
        this.length = length;
        this.name = name;
        this.sha256 = sha256;
    }

    private final int numChunks;
    private final long length;
    private final String name;

    @Override
    public String getName() {
        return name;
    }

    private final String sha256;

    public LocalFile(String name, File f) throws IOException, FileNotFoundException {
        this.name = name;
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
			logger.info(MessageFormat.format(Messages.getString("HASH_SUCCESS"), f.getAbsolutePath(), this.sha256));
		} catch (NoSuchAlgorithmException e) {
			logger.error(MessageFormat.format(Messages.getString("ALGO_NOT_FOUND"), f.getAbsolutePath()));
            throw new IOException("Hashing failed while preparing file", e);
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
