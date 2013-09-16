package net.mosstest.servercore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.mosstest.scripting.MossScriptEnv;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;

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
		scripts=new ArrayList<>();
		String[] scNames=this.gameCfg.getStringArray("plugins");
		for(String scName: scNames) {
			scripts.add(new MossScript(new File("data/scripts"), scName));
		}
	}
	private File baseDir;
	private XMLConfiguration gameCfg;
	private File cfgFile;
	private ArrayList<MossScript> scripts;
	public ArrayList<MossScript> getScripts() {
		return scripts;
	}
}
