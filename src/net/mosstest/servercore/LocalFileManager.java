package net.mosstest.servercore;

import com.jme3.asset.AssetLocator;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

public class LocalFileManager implements IFileManager {

    public static final LocalFileManager scriptsInstance;
    private static HashMap<String, LocalFileManager> managers = new HashMap<>();

    static {
        scriptsInstance = new LocalFileManager(new File("data/scripts"));
        managers.put("scripts", scriptsInstance);
    }

    private HashMap<String, LocalFile> files = new HashMap<>();

    public static LocalFileManager getFileManager(String key) {
        return managers.get(key);
    }

    private final File basedir;

    static Logger logger = Logger.getLogger(LocalFileManager.class);

    @Override
    public LocalFile getFile(String name) throws IOException {
        String normalized = FilenameUtils.normalize(name);
        if (normalized == null) {
            logger.warn("Failed to normalize game resource filename: " + name);

            throw new FileNotFoundException("The filename " + name
                    + " could not be normalized.");
        }
        File f = new File(this.basedir, normalized);
        logger.info("Got local file " + name + " as " + f.getAbsolutePath());
        LocalFile lf = new LocalFile(f);
        this.files.put(name, lf);
        return lf;
    }

    @Override
    public void registerFile(String name, String sha256, int size, long version)
            throws NotImplementedException {
        throw new NotImplementedException();

    }

    @Override
    public void receiveFileChunk(String sha512, int chunkId, ByteBuffer buf) {
        throw new NotImplementedException();
    }

    @Override
    public Class<? extends AssetLocator> getAssetLocatorClass() {
        return LocalAssetLocator.class;
    }

    public LocalFileManager(File basedir) {
        this.basedir = basedir;

    }

    public static String getHash(File f) throws IOException,
            NoSuchAlgorithmException {


        MessageDigest md = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();
        ByteBuffer bbf = ByteBuffer.allocateDirect(8192);

        int bytesRead;

        bytesRead = fc.read(bbf);

        while ((bytesRead != -1) && (bytesRead != 0)) {
            bbf.flip();

            byte[] bytes = new byte[bytesRead];
            bbf.get(bytes);

            md.update(bbf);

            bbf.clear();
            bytesRead = fc.read(bbf);
        }

        fis.close();

        byte[] mdBytes = md.digest();

        StringBuilder hexString = new StringBuilder();

        for (byte b : mdBytes) {
            hexString.append(Integer.toHexString((0xFF & b)));
        }

        return hexString.toString();

    }

    @Override
    public List<IMossFile> getFiles() {
        // TODO Auto-generated method stub
        return null;
    }

    public IMossFile getScriptInitFile(String scName) throws IOException {
        String normalized = FilenameUtils.normalize(scName);
        if (normalized == null) {
            System.out.println("FOO");
            logger.warn("Failed to normalize game resource filename: " + scName);

            throw new FileNotFoundException("The filename " + scName
                    + " could not be normalized.");
        }
        LocalFile scriptFile = getFile(normalized + "/init.js");
        try {
            LocalFile fileIndex = getFile(normalized + "/ignore");
            BufferedReader idxR = new BufferedReader(fileIndex.getReader());
            String line;
            while ((line = idxR.readLine()) != null) {
                String normalizedLine = FilenameUtils.normalize(line.trim());
                if (normalizedLine == null) {
                    logger.warn("Failed to normalize game resource filename from file index: "
                            + line);

                    continue;
                }

                // side effect of registering the file in the map
                getFile(normalized + normalizedLine);

            }
        } catch (FileNotFoundException e) {
            logger.warn("No index file found; no files will be served to the client.");
            // TODO use directory listing in this case
        }
        return scriptFile;
    }
}
