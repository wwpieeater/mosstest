package net.mosstest.servercore;

import com.google.common.collect.ImmutableList;
import com.jme3.asset.AssetLocator;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class LocalFileManager implements IFileManager {
    private HashSet<String> visitedScripts = new HashSet<>();
    protected HashSet<AbstractMossScript> executed = new HashSet<>();
    public static final LocalFileManager scriptsInstance;
    public static final int HASHING_BUFFER_SIZE = 8192;
    public static final int BYTE_CAST_MASK = 0xFF;
    @SuppressWarnings("StaticCollection")
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
    public LocalFile getFile(String name) throws IOException, FileNotFoundException {
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
    public void registerFile(String name, String sha256, long size) {
        try {
            this.files.put(name, new LocalFile(new File(basedir, name), (int) Math.ceil(size / (double) LocalFile.CHUNK_SIZE), size, name, sha256));
        } catch (IOException ignored) {
        }


    }

    public void registerFile(String name, LocalFile lf) {
        this.files.put(name, lf);

    }

    @Override
    public void receiveFileChunk(String sha256, int chunkId, ByteBuffer buf) throws IOException {
        throw new IOException("This file is read-only due to its being in a non-cache directory.");
    }

    @Override
    public Class<? extends AssetLocator> getAssetLocatorClass() {
        return LocalAssetLocator.class;
    }

    public LocalFileManager(File basedir) {
        this.basedir = basedir;

    }

    @Override
    public List<? extends IMossFile> getFiles() {
        return ImmutableList.copyOf(files.values());
    }

    @Override
    public AbstractMossScript getScript(final String name) throws IOException, MossWorldLoadException {
        String normalized = FilenameUtils.normalize(name);
        try {
            final String indexName = normalized + "/index";
            LocalFile fileIndex = getFile(indexName);
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
            logger.warn("No index file found; all files will be served to the client.");
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
        List<AbstractMossScript> dependencies = new ArrayList<>();
        try {
            File scriptXml = new File(this.getFile(normalized + "/script.xml").getFilename());


            XMLConfiguration scriptCfg = new XMLConfiguration(scriptXml);
            String[] scNames = scriptCfg.getStringArray("dependencies.dependency");
            for (String sc : scNames) {
                if (sc.equals(name)) continue;
                if (visitedScripts.contains(sc)) {
                    logger.fatal("A circular dependency was found. " + name + "depends on " + sc + " but the latter depends directly or indirectly on the former");
                    throw new MossWorldLoadException("A circular dependency was found. " + name + "depends on " + sc + " but the latter depends directly or indirectly on the former");
                }
                try {
                    dependencies.add(this.getScript(sc));
                } catch (StackOverflowError e) {
                    // should never happen
                    logger.fatal("FIXME The stack overflowed while resolving dependencies. " +
                            "Either there is an undetected dependency issue, or there is simply an extreme number of dependencies.");
                    throw new MossWorldLoadException("FIXME The stack overflowed while resolving dependencies. " +
                            "Either there is an undetected dependency issue, or there is simply an extreme number of dependencies.");
                }
            }
        } catch (ConfigurationException | FileNotFoundException e) {
            logger.warn("No script.xml has been given, assuming no dependencies and defaults for all other script settings.");
        }

        return new LocalScript(name, dependencies);
    }

    public IMossFile getScriptInitFile(String scName) throws IOException, FileNotFoundException {
        String normalized = FilenameUtils.normalize(scName);
        if (normalized == null) {
            logger.warn("Failed to normalize game resource filename: " + scName);

            throw new FileNotFoundException("The filename " + scName
                    + " could not be normalized.");
        }
        final String scriptName = normalized + "/init.js";
        LocalFile scriptFile = getFile(scriptName);
        registerFile(scriptName, scriptFile);

        return scriptFile;
    }

    private class LocalScript extends AbstractMossScript {

        private final List<AbstractMossScript> dependencies;

        public LocalScript(String name, List<AbstractMossScript> dependencies) {
            super(name);
            this.dependencies = dependencies;
        }

        @Override
        public void exec(ScriptEnv sEnv) throws IOException, MossWorldLoadException {
            LocalFileManager.this.executed.add(this);
            for(AbstractMossScript sc : dependencies) {
                sc.exec(sEnv);
            }
            sEnv.runScript(this.getInitFile());
        }

        @Override
        public IMossFile getInitFile() throws IOException {
            return LocalFileManager.this.getScriptInitFile(name);
        }

        @Override
        public List<AbstractMossScript> getDependencies() {
            return Collections.unmodifiableList(dependencies);
        }
    }
}
