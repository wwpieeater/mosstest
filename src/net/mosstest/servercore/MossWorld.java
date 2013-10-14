package net.mosstest.servercore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
	private ScriptableDatabase sdb;
	private EventProcessor evp;
	@SuppressWarnings("unused")
	private ServerNetworkingManager snv;
	volatile boolean run = true;
	private FuturesProcessor fp;
	private NodeManager nm;
	/**
	 * Initializes a server world. This will start the server once the world is
	 * initialized, loaded, and passes basic consistency checks. This
	 * constructor will not initialize load-balancing.
	 * 
	 * @param name
	 *            A string that names the world.
	 * @param port
	 *            The port number on which to run the server.
	 * @throws MossWorldLoadException
	 *             Thrown if the world cannot be loaded, due to inconsistency,
	 *             missing files, or lack of system resources.
	 */
	@SuppressWarnings("nls")
	public MossWorld(String name, int port) throws MossWorldLoadException {
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
		this.sdb = new ScriptableDatabase(this.baseDir);
		this.fp = new FuturesProcessor();
		this.nm=new NodeManager(this.db.nodes);
		this.mossEnv = new MossScriptEnv(this.sdb, this.nc, this.fp, this.nm);
		this.sEnv = new ScriptEnv(this.mossEnv);
		ArrayList<MossScript> scripts = this.game.getScripts();
		for (MossScript sc : scripts) {
			this.sEnv.runScript(sc);
		}
		this.evp = new EventProcessor(this.mossEnv);
		try {
			this.snv = new ServerNetworkingManager(port, this);
		} catch (IOException e) {
			throw new MossWorldLoadException(
					"Failure in opening server socket for listening!");
		}
		while (this.run) {
			// hold loop for game to run.
		}
		System.exit(0);

	}

	public void enqueueEvent(MossEvent e) throws InterruptedException {
		this.evp.eventQueue.put(e);
	}

	public static void main(String[] args) throws MossWorldLoadException {
		MossWorld m = new MossWorld("test", 16511);

	}
}
