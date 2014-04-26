package net.mosstest.client;

import com.jme3.asset.*;
import net.mosstest.servercore.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by hexafraction on 4/26/14.
 */
public class RemoteFileManager implements IFileManager {
    private static final String XML_DEPENDENCY_KEY = "dependencies.dependency";
    private static volatile RemoteFileManager instance = null;
    private HashSet<String> visitedScripts;

    protected HashSet<AbstractMossScript> executed = new HashSet<>();

    public static RemoteFileManager getInstance() {
        if (instance == null) {
            synchronized (RemoteFileManager.class) {
                if (instance == null) {
                    instance = new RemoteFileManager();
                }
            }
        }
        return instance;

    }

    private static final Logger logger = Logger.getLogger(RemoteFileManager.class);
    private HashMap<String, RemoteFile> knownByName = new HashMap<>();

    private HashMap<String, RemoteFile> knownByHash = new HashMap<>();
    private File cacheBasedir;
    // used to request files
    private MossClient client;

    @Override
    public RemoteFile getFile(String name) throws IOException {
        return knownByName.get(name);
    }

    @Override
    public void registerFile(String name, String sha256, long size) throws IOException {
        logger.info(MessageFormat.format("Registered {0} with hash {1}, and a size of {2} bytes", name, sha256, size));
        RemoteFile rmf = new RemoteFile(sha256, name, size);
        knownByName.put(name, rmf);
        knownByHash.put(sha256, rmf);
    }

    @Override
    public void receiveFileChunk(String sha256, int chunkId, ByteBuffer buf) throws IOException {
        RemoteFile rmf = this.knownByHash.get(sha256);
        if (rmf == null) {
            logger.warn(MessageFormat.format("The server tried to send a file chunk for a file with hash {0} that we don't know about.", sha256));
            return;
        }

        rmf.writeChunk(chunkId, buf.array());
    }


    @Override
    public Class<? extends AssetLocator> getAssetLocatorClass() {
        return RemoteAssetLocator.class;
    }

    @Override
    public List<? extends IMossFile> getFiles() {
        return null;
    }

    @Override
    public AbstractMossScript getScript(String name) throws IOException, MossWorldLoadException {
        List<AbstractMossScript> dependencies = new ArrayList<>();
        try {
            File scriptXml = this.getFile(name + "/script.xml").file;


            XMLConfiguration scriptCfg = new XMLConfiguration(scriptXml);
            String[] scNames = scriptCfg.getStringArray(XML_DEPENDENCY_KEY);
            for (String sc : scNames) {
                if (sc.equals(name)) continue;
                if (visitedScripts.contains(sc)) {
                    logger.fatal(MessageFormat.format(Messages.getString("CIRCULAR_DEPENDENCY_ISSUE"), name, sc));
                    throw new MossWorldLoadException(MessageFormat.format(Messages.getString("CIRCULAR_DEPENDENCY_ISSUE"), name, sc));
                }
                try {
                    dependencies.add(this.getScript(sc));
                } catch (StackOverflowError e) {
                    // should never happen
                    logger.fatal(Messages.getString("DEPFIND_STACK_OVERFLOW"));
                    throw new MossWorldLoadException("FIXME The stack overflowed while resolving dependencies. Either there is an undetected dependency issue, or there is simply an extreme number of dependencies.");
                }
            }
        } catch (ConfigurationException | FileNotFoundException e) {
            logger.warn(Messages.getString("SCRIPT_XML_MISSING"));
        }

        return new RemoteScript(name, dependencies);
    }


    public class RemoteFile implements IMossFile {
        private final long size;

        private final File file;

        private final RandomAccessFile rFile;

        private final BitSet completedChunks;

        private final String sha256;

        private final int numChunks;

        private final String name;

        RemoteFile(String sha256, String name, long size) throws IOException {
            this.sha256 = sha256;
            this.name = name;
            numChunks = (int) Math.ceil(size / IMossFile.CHUNK_SIZE);
            if (!sha256.matches("[0-9A-Za-z]{32}"))
                throw new MosstestFatalDeathException("A file SHA256 was invalid, and could not be used for caching.");
            this.file = new File(RemoteFileManager.this.cacheBasedir, sha256);
            this.file.createNewFile();
            this.rFile = new RandomAccessFile(this.file, "rw");
            this.size = size;
            try {
                completedChunks = new BitSet(numChunks);

                String actualSha = IFileManager.getHash(file);
                if (actualSha.equalsIgnoreCase(sha256)) {
                    completedChunks.set(0, numChunks);
                }

            } catch (NoSuchAlgorithmException e) {
                throw new MosstestFatalDeathException("The SHA-256 algorithm could not be found, but is required to cache files.");
            }

        }

        @Override
        public Reader getReader() throws FileNotFoundException {
            return new FileReader(this.file);
        }

        @Override
        public InputStream getInputStream() throws FileNotFoundException {
            return new FileInputStream(this.file);
        }

        @Override
        public byte[] readChunk(int chunkId) throws IOException {
            this.rFile.seek(chunkId * IMossFile.CHUNK_SIZE);
            byte[] buf = new byte[(int) (chunkId == (this.numChunks - 1) ? (this.size % IMossFile.CHUNK_SIZE)
                    : IMossFile.CHUNK_SIZE)];
            this.rFile.readFully(buf);
            return buf;
        }

        @Override
        public void writeChunk(int chunkId, byte[] buf) throws IOException {
            this.rFile.seek(chunkId * IMossFile.CHUNK_SIZE);
            if (((chunkId == (this.numChunks - 1) ? (this.size % IMossFile.CHUNK_SIZE) : IMossFile.CHUNK_SIZE)) != buf.length)
                throw new MosstestFatalDeathException(MessageFormat.format("An inbound chunk for file {0} is not of the correct size. The server may be sending erroneous data.", this.name));
            this.completedChunks.set(chunkId);
            if (isComplete()) {
                logger.info(MessageFormat.format("Completed file {0}.", this.name));
            }
            try {
                String realHash = IFileManager.getHash(this.file);
                if (!this.sha256.equalsIgnoreCase(realHash)) {
                    logger.warn(MessageFormat.format("File {0} expected with hash {1} but got reassembled as hash {2}", this.name, this.sha256, realHash));
                }
            } catch (NoSuchAlgorithmException e) {
                throw new MosstestFatalDeathException("The SHA-256 algorithm could not be found, but is required to cache files.");
            }
        }

        @Override
        public String getSha256() {
            return this.sha256;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public boolean isComplete() {
            return (completedChunks.nextClearBit(0) == -1 || completedChunks.nextClearBit(0) == this.numChunks);
        }

        @Override
        public long getSize() {
            return this.size;
        }
    }

    private static class RemoteAssetLocator implements AssetLocator {
        private static final Logger logger = Logger.getLogger(RemoteAssetLocator.class);

        @Override
        public void setRootPath(String s) {
            // ignore
            logger.warn(MessageFormat.format("Tried to set root path {0} for RemoteAssetLocator, which ignores the root path.", s));
        }

        @Override
        public AssetInfo locate(AssetManager assetManager, AssetKey assetKey) {
            try {
                return new RemoteAssetInfo(assetManager, assetKey,
                        RemoteFileManager.getInstance().getFile(assetKey.getName()));
            } catch (IOException ex) {
                throw new AssetLoadException("Failed to open file: "
                        + assetKey.getName(), ex);
            }
        }

        private static class RemoteAssetInfo extends AssetInfo {

            private RemoteFile file;

            public RemoteAssetInfo(AssetManager manager, AssetKey key, RemoteFile file) {
                super(manager, key);
                this.file = file;
            }

            @Override
            public InputStream openStream() {
                try {
                    return this.file.getInputStream();
                } catch (IOException ex) {
                    throw new AssetLoadException("Failed to open file: "
                            + this.file.getName(), ex);
                }
            }
        }
    }

    private class RemoteScript extends AbstractMossScript {
        private final List<AbstractMossScript> dependencies;

        public RemoteScript(String name, List<AbstractMossScript> dependencies) {
            super(name);
            this.dependencies = dependencies;
        }

        @Override
        public void exec(ScriptEnv sEnv) throws IOException, MossWorldLoadException {
            RemoteFileManager.this.executed.add(this);
            for (AbstractMossScript sc : dependencies) {
                sc.exec(sEnv);
            }
            sEnv.runScript(this.getInitFile());
        }

        @Override
        public IMossFile getInitFile() throws IOException {
            return RemoteFileManager.this.getFile(this.name + "/init.js");
        }

        @Override
        public List<AbstractMossScript> getDependencies() {
            return Collections.unmodifiableList(dependencies);
        }
    }
}
