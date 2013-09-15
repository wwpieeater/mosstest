package net.mosstest.servercore;

import java.io.File;

import net.mosstest.scripting.MossScriptEnv;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

public class MossGame {
	@SuppressWarnings("nls")
	public MossGame(String name) throws MossWorldLoadException {
		this.baseDir = new File("data/games/" + name); //$NON-NLS-1$
		this.cfgFile = new File(this.baseDir, "game.xml"); 
		if(!(this.baseDir.isDirectory()&&this.cfgFile.isFile())) {
			throw new MossWorldLoadException("Game directory or configuration file not found.");
		}
		try {
			this.gameCfg=new XMLConfiguration(cfgFile);
		} catch (ConfigurationException e) {
			throw new MossWorldLoadException("Error in loading the configuration file.");
		}
		
	}
	private File baseDir;
	private XMLConfiguration gameCfg;
	private File cfgFile;
}
