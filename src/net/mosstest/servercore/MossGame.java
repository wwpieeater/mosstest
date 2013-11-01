package net.mosstest.servercore;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

public class MossGame {
	@SuppressWarnings("nls")
	public MossGame(String name) throws MossWorldLoadException {
		this.baseDir = new File("data/games/" + name); //$NON-NLS-1$
		this.cfgFile = new File(this.baseDir, "game.xml");
		if (!(this.baseDir.isDirectory() && this.cfgFile.isFile())) {
			throw new MossWorldLoadException(
					"Game directory or configuration file not found.");
		}
		try {
			this.gameCfg = new XMLConfiguration(this.cfgFile);
		} catch (ConfigurationException e) {
			throw new MossWorldLoadException(
					"Error in loading the configuration file.");
		}
		this.scripts = new ArrayList<>();
		String[] scNames = this.gameCfg.getStringArray("plugin");
		for (String scName : scNames) {
			try {
				this.scripts.add(new MossScript(new MossLocalFile(new File("data/scripts/"),
						scName, "init.js")));
			} catch (FileNotFoundException e) {
				throw new MossWorldLoadException("file not found: " + scName);
			} // TODO directory structure and proper iteration
		}
	}

	private File baseDir;
	private XMLConfiguration gameCfg;
	private File cfgFile;
	private ArrayList<MossScript> scripts;

	public ArrayList<MossScript> getScripts() {
		return this.scripts;
	}
}
