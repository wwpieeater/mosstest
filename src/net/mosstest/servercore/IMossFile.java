package net.mosstest.servercore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;

public interface IMossFile {
	long CHUNK_SIZE = 65536;

	public Reader getReader() throws FileNotFoundException;
	
	public InputStream getInputStream() throws FileNotFoundException;
	
	public byte[] readChunk(int chunkId) throws IOException;
	
	public void writeChunk(int chunkId, byte[] buf) throws IOException;
	
	public String getSha256();
}
