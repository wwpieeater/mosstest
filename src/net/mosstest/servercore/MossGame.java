package net.mosstest.servercore;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

// TODO: Auto-generated Javadoc
/**
 * The Class MossGame.
 */
public class MossGame {
	
	/**
	 * Instantiates a new moss game.
	 *
	 * @param name the name
	 * @throws MossWorldLoadException the moss world load exception
	 */
	@SuppressWarnings("nls")
	public MossGame(String name) throws MossWorldLoadException {
		this.baseDir = new File("data/games/" + name); //$NON-NLS-1$
		this.cfgFile = new File(this.baseDir, "game.xml"); //$NON-NLS-1$
		if (!(this.baseDir.isDirectory() && this.cfgFile.isFile())) {
			throw new MossWorldLoadException(
					Messages.getString("MossGame.DIR_OR_CFG_NOT_FOUND")); //$NON-NLS-1$
		}
		try {
			this.gameCfg = new XMLConfiguration(this.cfgFile);
		} catch (ConfigurationException e) {
			throw new MossWorldLoadException(
					Messages.getString("MossGame.CFG_LOAD_ERR")); //$NON-NLS-1$
		}
		this.scripts = new ArrayList<>();
		String[] scNames = this.gameCfg.getStringArray("plugin"); //$NON-NLS-1$
		for (String scName : scNames) {
			try {
				this.scripts.add(new MossScript(new MossLocalFile(new File("data/scripts/"), //$NON-NLS-1$
						scName, "init.js"))); //$NON-NLS-1$
			} catch (FileNotFoundException e) {
				throw new MossWorldLoadException(Messages.getString("MossGame.FILE_NOT_FOUND") + scName); //$NON-NLS-1$
			} // TODO directory structure and proper iteration
		}
	}

	/** The base dir. */
	private File baseDir;
	
	/** The game cfg. */
	private XMLConfiguration gameCfg;
	
	/** The cfg file. */
	private File cfgFile;
	
	/** The scripts. */
	private ArrayList<MossScript> scripts;

	/**
	 * Gets the scripts.
	 *
	 * @return the scripts
	 */
	public ArrayList<MossScript> getScripts() {
		return this.scripts;
	}
}
