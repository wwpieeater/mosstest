package net.mosstest.servercore;

import com.google.common.collect.ImmutableList;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class MossGame.
 */
public class MossGame {

	/**
	 * Instantiates a new moss game.
	 * 
	 * @param name
	 *            the name
	 * @throws MossWorldLoadException
	 *             the moss world load exception
	 * @throws IOException
	 */
	@SuppressWarnings("nls")
	public MossGame(String name) throws MossWorldLoadException, IOException {
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
		String[] scNames = this.gameCfg.getStringArray("plugins.plugin"); //$NON-NLS-1$
        for (String scName : scNames)
            try {
                this.scripts.add(LocalFileManager.scriptsInstance
                        .getScript(scName)); //$NON-NLS-1$
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new MossWorldLoadException(MessageFormat.format(Messages.getString("MossGame.FILE_NOT_FOUND"), scName)); //$NON-NLS-1$
            }
    }

    /**
     * The base dir.
     */
    private File baseDir;

    /**
     * The game cfg.
     */
    private XMLConfiguration gameCfg;

	/** The cfg file. */
	private File cfgFile;

	/** The scripts. */
	private ArrayList<AbstractMossScript> scripts;

	/**
	 * Gets the scripts.
	 * 
	 * @return the scripts
	 */
	public List<AbstractMossScript> getScripts() {
		return ImmutableList.copyOf(this.scripts);
	}
}
