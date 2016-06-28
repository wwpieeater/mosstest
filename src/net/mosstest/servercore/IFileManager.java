package net.mosstest.servercore;

import com.jme3.asset.AssetLocator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface IFileManager {
    static String getHash(File f) throws IOException,
            NoSuchAlgorithmException, FileNotFoundException {


        MessageDigest md = MessageDigest.getInstance("SHA-256");

        try (FileInputStream fis = new FileInputStream(f)) {
            try (FileChannel fc = fis.getChannel()) {
                ByteBuffer bbf = ByteBuffer.allocateDirect(LocalFileManager.HASHING_BUFFER_SIZE);

                int bytesRead;

                bytesRead = fc.read(bbf);

                while ((bytesRead != -1) && (bytesRead != 0)) {
                    bbf.flip();


                    md.update(bbf);

                    bbf.clear();
                    bytesRead = fc.read(bbf);
                }

                fis.close();

                byte[] mdBytes = md.digest();

                StringBuilder hexString = new StringBuilder();

                for (byte b : mdBytes) {
                    hexString.append(Integer.toHexString((LocalFileManager.BYTE_CAST_MASK & b)));
                }

                return hexString.toString();
            }
        }
    }

    public IMossFile getFile(String name) throws IOException;

    public void registerFile(String name, String sha256, long size) throws IOException;

    public void receiveFileChunk(String sha256, int chunkId, ByteBuffer buf) throws IOException;

    public Class<? extends AssetLocator> getAssetLocatorClass();

    public List<? extends IMossFile> getFiles();

    public AbstractMossScript getScript(final String name) throws IOException, MossWorldLoadException;
}
