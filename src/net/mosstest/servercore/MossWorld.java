package net.mosstest.servercore;

import java.io.File;
import java.io.IOException;

import net.mosstest.scripting.MossScriptEnv;
import net.mosstest.scripting.ScriptableDatabase;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

public class MossWorld {
	private MossGame game;
	private File baseDir;
	private XMLConfiguration worldCfg;
	private File cfgFile;
	private MapDatabase db;
	private NodeCache nc;
	private MossScriptEnv mossEnv;
	private ScriptEnv sEnv;
	private RenderPreparator rp;
	private ScriptableDatabase sdb;
	private EventProcessor evp;
	@SuppressWarnings("nls")
	public MossWorld(String name) throws MossWorldLoadException {
		this.baseDir = new File("data/worlds/" + name); //$NON-NLS-1$
		if (!this.baseDir.exists()) {
			this.baseDir.mkdirs();

		}
		this.cfgFile = new File(this.baseDir, "game.xml"); //$NON-NLS-1$
		if (!this.cfgFile.isFile())
			try {
				this.cfgFile.createNewFile();
				this.worldCfg = new XMLConfiguration(this.cfgFile);
			} catch (IOException | ConfigurationException e) {
				throw new MossWorldLoadException(
						"Error in creating configuration for game " + name
								+ ". The error wrapped was: " + e.getMessage());
			}
		if (!this.worldCfg.containsKey("gameid")) { //$NON-NLS-1$
			throw new MossWorldLoadException(
					"The game ID is not specified. The game ID must be specified in game.xml as <gameid>game</gameid> "
							+ "where data/games/game is a directory with a valid game.");
		}
		this.game = new MossGame(this.worldCfg.getString("gameid"));
		try {
			this.db = new MapDatabase(this.baseDir);
		} catch (MapDatabaseException e) {
			throw new MossWorldLoadException(
					"An error has occured when opening the database. It is likely inaccessible, on a full disk, or corrupt.");
		}
		this.nc = new NodeCache(this.db);
		this.rp = new RenderPreparator(this.nc);
		this.mossEnv=new MossScriptEnv(this.sdb, this.nc);
		
		//CRITICAL to run all game scripts here.
		this.evp = new EventProcessor(mossEnv);
	}
}
