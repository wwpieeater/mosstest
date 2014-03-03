package net.mosstest.servercore;

import com.jme3.asset.AssetLocator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public interface IFileManager {
    public IMossFile getFile(String name) throws IOException;

    public void registerFile(String name, String sha256, long size);

    public void receiveFileChunk(String sha512, int chunkId, ByteBuffer buf) throws IOException;

    public Class<? extends AssetLocator> getAssetLocatorClass();

    public List<? extends IMossFile> getFiles();
}
