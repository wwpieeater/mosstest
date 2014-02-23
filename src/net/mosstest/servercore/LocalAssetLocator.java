package net.mosstest.servercore;

import java.io.IOException;
import java.io.InputStream;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLoadException;
import com.jme3.asset.AssetLocator;
import com.jme3.asset.AssetManager;

public class LocalAssetLocator implements AssetLocator {
	private LocalFileManager lfm;

	@Override
	public AssetInfo locate(AssetManager manager, AssetKey key) {
		if (this.lfm == null)
			this.lfm = LocalFileManager.getFileManager("moss://scripts");
		if (this.lfm == null)
			this.lfm = LocalFileManager.scriptsInstance;
		try {
			return new LocalAssetInfo(manager, key,
					this.lfm.getFile(key.getName()));
		} catch (IOException ex) {
			throw new AssetLoadException("Failed to open file: "
					+ key.getName(), ex);
		}
		

	}

	@Override
	public void setRootPath(String arg0) {
		this.lfm = LocalFileManager.getFileManager(arg0);
	}

	private static class LocalAssetInfo extends AssetInfo {

		private LocalFile file;

		public LocalAssetInfo(AssetManager manager, AssetKey key, LocalFile file) {
			super(manager, key);
			this.file = file;
		}

		@Override
		public InputStream openStream() {
			try {
				return this.file.getInputStream();
			} catch (IOException ex) {
				throw new AssetLoadException("Failed to open file: "
						+ this.file.getFilename(), ex);
			}
		}
	}
}
