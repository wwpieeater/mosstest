package net.mosstest.servercore;

import com.google.common.collect.ImmutableList;
import com.jme3.asset.AssetLocator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public static final IOFileFilter CVS_FILTER = FileFilterUtils.makeCVSAware(null);

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

        return new LocalFile(name, f);
    }

    @Override
    public void registerFile(String name, String sha256, int size, long version)
            throws NotImplementedException {
        throw new NotImplementedException();

    }

    public void registerFile(String name, LocalFile lf) {
        this.files.put(name, lf);

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
    public List<? extends IMossFile> getFiles() {
        return ImmutableList.copyOf(files.values());
    }

    public IMossFile getScriptInitFile(String scName) throws IOException {
        String normalized = FilenameUtils.normalize(scName);
        if (normalized == null) {
            logger.warn("Failed to normalize game resource filename: " + scName);

            throw new FileNotFoundException("The filename " + scName
                    + " could not be normalized.");
        }
        final String scriptName = normalized + "/init.js";
        LocalFile scriptFile = getFile(scriptName);
        registerFile(scriptName, scriptFile);
        try {
            final String indexName = normalized + "/index";
            LocalFile fileIndex = getFile(indexName);
            registerFile(indexName, scriptFile);
            BufferedReader idxR = new BufferedReader(fileIndex.getReader());
            String line;
            while ((line = idxR.readLine()) != null) {
                String normalizedLine = FilenameUtils.normalize(line.trim());
                if (normalizedLine == null) {
                    logger.warn("Failed to normalize game resource filename from file index: "
                            + line);

                    continue;
                }
                try {

                    final String filename = normalized + normalizedLine;
                    final LocalFile file = getFile(filename);
                    this.registerFile(filename, file);
                } catch (FileNotFoundException e) {
                    logger.warn("File was in index but not on disk: "
                            + line);

                }
            }
        } catch (FileNotFoundException e) {
            logger.warn("No index file found; no files will be served to the client.");
            File base = new File(basedir, normalized);
            Path basePath = Paths.get(basedir.getAbsolutePath());
            for (File f : FileUtils.listFiles(base, CVS_FILTER, CVS_FILTER)) {
                try {
                    Path path = Paths.get(f.getAbsolutePath());
                    final String resolvedName = basePath.relativize(path).toFile().getPath();
                    LocalFile file = this.getFile(resolvedName);
                    logger.debug("Got file via recursive directory listing: " + resolvedName);
                    this.registerFile(resolvedName, file);
                } catch (FileNotFoundException fnfe2) {
                    // should not happen
                    logger.warn("Could not find file from recursive directory listing. This should never happen.");
                }
            }
        }
        return scriptFile;
    }
}
