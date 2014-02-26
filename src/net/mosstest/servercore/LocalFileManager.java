package net.mosstest.servercore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import com.jme3.asset.AssetLocator;

public class LocalFileManager implements IFileManager {

	public static final LocalFileManager scriptsInstance;
	private static HashMap<String, LocalFileManager> managers = new HashMap<>();
	static {
		scriptsInstance = new LocalFileManager(new File("data/scripts"));
		managers.put("scripts", scriptsInstance);
	}

	private HashMap<String, LocalFile> files;

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

		MessageDigest md = null;
		FileInputStream fis = null;
		FileChannel fc = null;
		ByteBuffer bbf = null;
		StringBuilder hexString = null;

		md = MessageDigest.getInstance("SHA-256");
		fis = new FileInputStream(f);
		fc = fis.getChannel();
		bbf = ByteBuffer.allocateDirect(8192);

		int b;

		b = fc.read(bbf);

		while ((b != -1) && (b != 0)) {
			bbf.flip();

			byte[] bytes = new byte[b];
			bbf.get(bytes);

			md.update(bbf);

			bbf.clear();
			b = fc.read(bbf);
		}

		fis.close();

		byte[] mdbytes = md.digest();

		hexString = new StringBuilder();

		for (int i = 0; i < mdbytes.length; i++) {
			hexString.append(Integer.toHexString((0xFF & mdbytes[i])));
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
			logger.warn("Failed to normalize game resource filename: " + scName);

			throw new FileNotFoundException("The filename " + scName
					+ " could not be normalized.");
		}
		LocalFile scriptFile = getFile(normalized + "init.js");
		LocalFile fileIndex = getFile(normalized + "index");
		BufferedReader idxR = new BufferedReader(fileIndex.getReader());
		String line = null;
		while ((line = idxR.readLine()) != null) {
			String normalizedLine = FilenameUtils.normalize(line.trim());
			if (normalizedLine == null) {
				logger.warn("Failed to normalize game resource filename from file index: "
						+ line);

				continue;
			}
			try {
				// side effect of registering the file in the map
				LocalFile listedFile = getFile(normalized + normalizedLine);
			} catch (IOException e) {
				logger.warn("File " + normalized + normalizedLine
						+ " does not exist but was listed in the index");
			}
		}
		return scriptFile;
	}
}
