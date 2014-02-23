package net.mosstest.servercore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import com.jme3.asset.AssetLocator;

public interface IFileManager {
	public IMossFile getFile(String name) throws FileNotFoundException, IOException;
	
	public void registerFile(String name, String sha256, int size, long version);
	
	public void receiveFileChunk(String sha512, int chunkId, ByteBuffer buf);
	
	public Class<? extends AssetLocator> getAssetLocatorClass();
	
	public List<IMossFile> getFiles();
}
