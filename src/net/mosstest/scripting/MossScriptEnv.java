package net.mosstest.scripting;

import java.util.ArrayList;
import java.util.EnumMap;

import org.iq80.leveldb.DB;

import net.mosstest.scripting.MossItem.Stack;
import net.mosstest.servercore.FuturesProcessor;
import net.mosstest.servercore.MapGeneratorException;
import net.mosstest.servercore.MossWorldLoadException;
import net.mosstest.servercore.NodeCache;
import net.mosstest.servercore.NodeManager;
import net.mosstest.servercore.ScriptSandboxBorderToken;

/**
 * 
 * This class is used by scripts and script-facing portions of Mosstest. Methods
 * restricted to be called from trusted Java-side shall pass a
 * {@link ScriptSandboxBorderToken}. Each event fired will run in the thread
 * pool. All requests via this API need not concern themselves with threading as
 * everything is handled by Mosstest itself.
 * 
 * The event handlers called are the ones defined via this class's registerOnFoo
 * methods, followed by any handlers defined in an instance of NodeParams via an
 * anonymous inner class, and finally with the default handler.
 * 
 * The order in which handlers registered here are called is undefined due to
 * the undefined order of scripts being loaded. Generally, this is planned to
 * occur in an order based on the SHA512 hash of the script. Comments with dummy
 * information may be used by the script author to attempt to set the position
 * of a script in the execution order via manipulating the hash. Handlers of the
 * same types within the same script are guaranteed to be called in order.
 * 
 * An event handler may interrupt handling of the event so that no further event
 * handlers nor the default are ever called, by throwing an instance of
 * {@link EventProcessingCompletedSignal}.
 * 
 * @author rarkenin
 * @since 0.0
 * @version 0.0
 */
public class MossScriptEnv {

	/*
	 * IMPORTANT, IMPORTANT, IMPORTANT. VERY IMPORTANT. THIS CLASS IS THE ONLY
	 * CLASS THAT SCRIPTS CAN ACCESS. MAKE ALL FIELDS AND METHODS PRIVATE UNLESS
	 * IT IS INTENDED TO FACE UNTRUSTED SCRIPTS.
	 */
	private EnumMap<MossEvent.EvtType, ArrayList<MossEventHandler>> registeredScriptEvents = new EnumMap<>(
			MossEvent.EvtType.class);
	private ScriptableDatabase db;
	private NodeCache nc;
	private FuturesProcessor fp;
	private NodeManager nm;

	/**
	 * Registers an event hander to fire on a player death. This will be run in
	 * the event processor thread pool. The default handler shall respawn the
	 * player at the spawnpoint and set health to default.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	void registerOnDieplayer(MossEventHandler r) {
		this.registeredScriptEvents.get(MossEvent.EvtType.EVT_DIEPLAYER).add(r);
	}

	/**
	 * Registers an event hander to fire on a node being dug. This will be run
	 * in the event processor thread pool. The default handler will remove the
	 * node and add its drop to the inventory of the digger if possible.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	void registerOnDignode(MossEventHandler r) {
		this.registeredScriptEvents.get(MossEvent.EvtType.EVT_DIGNODE).add(r);
	}

	/**
	 * Registers an event hander to fire on map generation. This will be run in
	 * the event processor thread pool before the default map generator. The
	 * default map generator shall create the chunk to be a recreation of a
	 * landscape.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	void registerOnGenerate(MossEventHandler r) {
		this.registeredScriptEvents.get(MossEvent.EvtType.EVT_GENERATE).add(r);
	}

	/**
	 * Registers an event hander to fire on a player joining. This will be run
	 * in the event processor thread pool. The default handler will be called
	 * before any script-specified handlers.
	 * {@link EventProcessingCompletedSignal} will still bypass future handlers
	 * but not default ones. The default handler initializes player data in
	 * memory from the database.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	void registerOnJoinplayer(MossEventHandler r) {
		this.registeredScriptEvents.get(MossEvent.EvtType.EVT_JOINPLAYER)
				.add(r);
	}

	/**
	 * Registers an event hander to fire on a player quitting. This will be run
	 * in the event processor thread pool.
	 * {@link EventProcessingCompletedSignal} will bypass future handlers but
	 * the default handler will be run even if the signal is thrown. The default
	 * handler will clean up the player in memory.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	void registerOnQuitplayer(MossEventHandler r) {
		this.registeredScriptEvents.get(MossEvent.EvtType.EVT_QUITPLAYER)
				.add(r);
	}

	/**
	 * Registers an event hander to fire on a new player registering and
	 * entering. This will be run in the event processor thread pool. The
	 * default handler will be called before any script-specified handlers.
	 * {@link EventProcessingCompletedSignal} will still bypass future handlers
	 * but not default ones. The default handler initializes player data in
	 * memory and the database. No items are given by default.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	void registerOnNewplayer(MossEventHandler r) {
		this.registeredScriptEvents.get(MossEvent.EvtType.EVT_NEWPLAYER).add(r);
	}

	/**
	 * Registers an event hander to fire on a node being placed. This will be
	 * run in the event processor thread pool. The default handler removes the
	 * item from the placer's inventory and puts down a node or entity.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	void registerOnPlacenode(MossEventHandler r) {
		this.registeredScriptEvents.get(MossEvent.EvtType.EVT_PLACENODE).add(r);
	}

	/**
	 * Registers an event hander to fire on a formspec being opened. This will
	 * be run in the event processor thread pool. A formspec is an XML-based 2D
	 * script-specified UI. The default handler is defined in the formspec. This
	 * is a way of catch-all for formspecs and shouldn't be used for
	 * implementing one formspec.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	void registerOnFspecOpen(MossEventHandler r) {
		this.registeredScriptEvents.get(MossEvent.EvtType.EVT_FSPEC_OPEN)
				.add(r);
	}

	/**
	 * Registers an event hander to fire on a formspec being submitted. This
	 * will be run in the event processor thread pool. This is a catch-all
	 * method and the default handler created in the formspec should be used for
	 * implementing behavior.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	void registerOnFspecSubmit(MossEventHandler r) {
		this.registeredScriptEvents.get(MossEvent.EvtType.EVT_FSPEC_SUBMIT)
				.add(r);
	}

	/**
	 * Registers an event hander to fire on an inventory action. This will be
	 * run in the event processor thread pool. The default handler performs the
	 * action as-is.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	void registerOnInvAction(MossEventHandler r) {
		this.registeredScriptEvents.get(MossEvent.EvtType.EVT_FSPEC_INVACTION)
				.add(r);
	}

	/**
	 * Registers an event hander to fire on an entity punch. This will be run in
	 * the event processor thread pool. The default handler performs no action.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	void registerOnEntityPunch(MossEventHandler r) {
		this.registeredScriptEvents.get(MossEvent.EvtType.EVT_ENTITY_PUNCH)
				.add(r);
	}

	/**
	 * Registers an event hander to fire on a player taking damage. This will be
	 * run in the event processor thread pool. The default handler causes the
	 * player to take the damage.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	void registerOnPlayerDamage(MossEventHandler r) {
		this.registeredScriptEvents.get(MossEvent.EvtType.EVT_PLAYERDAMAGE)
				.add(r);
	}

	/**
	 * Registers an event hander to fire on entity death. This will be run in
	 * the event processor thread pool. The default handler performs no action
	 * aside from the removal of said entity.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	void registerOnEntityDeath(MossEventHandler r) {
		this.registeredScriptEvents.get(MossEvent.EvtType.EVT_ENTITY_DEATH)
				.add(r);
	}

	/**
	 * Registers an event hander to fire on a chat message from a client. This
	 * will be run in the event processor thread pool. The default handler sends
	 * the message to all players.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	void registerOnChatMessage(MossEventHandler r) {
		this.registeredScriptEvents.get(MossEvent.EvtType.EVT_CHATMESSAGE).add(
				r);
	}

	/**
	 * Registers an event hander to fire on server shutdown. This will be run in
	 * the event processor thread pool. The default handler calls internal
	 * shutdown functions and cannot be bypassed.
	 * 
	 * Note that in the case of an error causing shutdown of the server the
	 * script API may be unavailable or in an inconsistent state so shutdown
	 * actions cannot be guaranteed to run. They will always be run on a clean
	 * shutdown.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	void registerOnShutdown(MossEventHandler r) {
		this.registeredScriptEvents.get(MossEvent.EvtType.EVT_SHUTDOWN).add(r);
	}

	/**
	 * Registers an event hander to fire on a chat command. This will be run in
	 * the event processor thread pool. The default handler will display an
	 * invalid command message. Handlers should <b>always</b> override the
	 * default except in special circumstances.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	void registerOnChatCommand(MossEventHandler r) {
		this.registeredScriptEvents.get(MossEvent.EvtType.EVT_CHATCOMMAND).add(
				r);
	}

	/**
	 * Registers an event hander to fire on a node move. This will be run in the
	 * event processor thread pool. The default handler moves the node.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	void registerOnNodeMove(MossEventHandler r) {
		this.registeredScriptEvents.get(MossEvent.EvtType.EVT_NODEMOVE).add(r);
	}

	/**
	 * Gets handlers for an event type in the form of an {@link ArrayList} for
	 * Mosstest internal use. At this time due to limited identification of each
	 * event there is no support for scripts accessing this directly.
	 * 
	 * @param type
	 *            A {@link MossEvent.EvtType} enumerable value specifying the
	 *            event type to retrieve.
	 * @param tok
	 *            A {@link ScriptSandboxBorderToken} to be used for ensuring
	 *            that scripts cannot call this method.
	 * @return An {@link ArrayList} of {@link MossEventHandler} objects.
	 * @throws SecurityException
	 */
	public ArrayList<MossEventHandler> getHandlers(MossEvent.EvtType type,
			ScriptSandboxBorderToken tok) throws SecurityException {
		if (!(tok instanceof ScriptSandboxBorderToken) || tok == null)
			throw new SecurityException(
					"Attempt to access controlled resources in the script DMZ."); //$NON-NLS-1$
		return this.registeredScriptEvents.get(type);
	}

	/**
	 * Sends a chat message to a player.
	 * 
	 * @param recipient
	 *            A {@link Player} object representing the recipient. A Player
	 *            object may be constructed with
	 *            {@link MossScriptEnv#getPlayerByName(String)}.
	 * @param from
	 *            A player object representing the sender. A Player object may
	 *            be constructed with
	 *            {@link MossScriptEnv#getPlayerByName(String)}. If null a
	 *            message is sent showing to users as having been sent by the
	 *            server with the prefix <code>[*] Server:</code>
	 * @param message
	 *            A string representing the message that shall be sent to the
	 *            specified recipient.
	 */
	public void sendChatMessage(Player recipient, Player from, String message) {
		// TODO
	}

	/**
	 * Sends a chat message to all players.
	 * 
	 * @param from
	 *            A player object representing the sender. A Player object may
	 *            be constructed with
	 *            {@link MossScriptEnv#getPlayerByName(String)}. If null a
	 *            message is sent showing to users as having been sent by the
	 *            server with the prefix <code>[*] Server:</code>
	 * @param message
	 *            A string representing the message that shall be sent to the
	 *            specified recipient.
	 */
	public void sendChatAll(Player from, String message) {
		// TODO
	}

	/**
	 * Sets the health on an entity or player.
	 * 
	 * @param ent
	 *            The entity to set health on.
	 * @param health
	 *            An integer representing the amount of health to set, from 0 to
	 *            {@link Entity#maxHealth()}.
	 */
	public void setHp(Player p, int health) {
		// TODO Once we have players doing stuff
	}

	/**
	 * Damages the tool of a player corresponding to a dig. The player's
	 * currently selected tool is damaged.
	 * 
	 * @param actor
	 *            The player that is digging a node.
	 * @param nd
	 *            The node dug.
	 * @throws MossScriptException
	 *             Thrown if the current tool cannot be used to dig the node.
	 */
	public void damageTool(Player actor, MapNode nd) throws MossScriptException {
		// TODO Auto-generated method stub

	}

	/**
	 * Gives a player an item. The item stack will be added to the player's
	 * default inventory, adding to the first available partial stack. If no
	 * partial stacks are available the item is added to the first open slot in
	 * the inventory.
	 * 
	 * @param player
	 * @param item
	 * @return True if the item could be added, false if the item could not be
	 *         added due to insufficient space.
	 */
	public boolean givePlayer(Player player, MossItem.Stack item) {
		return false;
	}

	/**
	 * Sets a node of the world to a given type. This cannot be called on a
	 * NodePosition with an existing solid node; use
	 * {@link #removeNode(NodePosition)} first.
	 * 
	 * @param pos
	 *            The position at which to set a node.
	 * @param node
	 *            The node to place at that position.
	 * @throws MapGeneratorException
	 */
	public void setNode(NodePosition pos, MapNode node) {
		MapChunk chk = this.nc.getChunkNoGenerate(pos.chunk);
		if (chk == null)
			return;
		chk.setNode(pos.xl, pos.yl, pos.zl, node.getNodeId());
	}

	/**
	 * Removes a node, setting it to air. This may be called on a NodePosition
	 * with an existing solid node.
	 * 
	 * @param pos
	 *            The NodePosition at which to remove the node.
	 * @throws MapGeneratorException
	 */
	public void removeNode(NodePosition pos) {
		MapChunk chk = this.nc.getChunkNoGenerate(pos.chunk);
		if (chk == null)
			return;
		chk.setNode(pos.xl, pos.yl, pos.zl, this.nm.getNode("mg:air")
				.getNodeId());
		this.nc.setChunk(pos.chunk, chk);
	}

	/**
	 * Get the MapNode at a certain location
	 * 
	 * @param pos
	 *            The location at which to get the node
	 * @return
	 * @throws MapGeneratorException
	 */
	public MapNode getNode(NodePosition pos) throws MapGeneratorException {
		return this.nm.getNode((short) this.nc.getChunk(pos.chunk).getNodeId(
				pos.xl, pos.yl, pos.zl));

	}

	/**
	 * Register a new MapNode in the node manager.
	 * 
	 * @param sysname
	 *            The name such as default:dirt to set. The prefix mg: is used
	 *            for mapgen-specific nodes, and should be done by creating a
	 *            node with a different prefix and aliasing mg:foo to it.
	 * @param userFacingName
	 *            The name to display in the UI, such as Dirt or Iron Ore
	 * @param params
	 *            An implementation of the {@link LiquidNodeParams} interface
	 *            detailing the action of the node.
	 *            {@link DefaultLiquidNodeParams} is a default that is
	 *            applicable to most liquids with near-water viscosity.
	 * @param textures
	 *            A string stating the filename of the textures image.
	 * @param light
	 *            The amount of light from 0 to 255 to be emitted.
	 * @return The MapNode object that has been created and added to the
	 *         manager.
	 * @throws MossWorldLoadException
	 *             If an exception occurs during the execution of the
	 *             registering.
	 */
	public MapNode registerNode(String sysname, String userFacingName,
			INodeParams params, String textures, boolean isLiquid, int light)
			throws MossWorldLoadException {
		MapNode nd = new MapNode(params, textures, sysname, userFacingName,
				light);
		this.nm.putNode(nd);
		return nd;
	}

	/**
	 * Register a new MapNode in the node manager.
	 * 
	 * @param sysname
	 *            The name such as default:lava to set. The prefix mg: is used
	 *            for mapgen-specific nodes, and should be done by creating a
	 *            node with a different prefix and aliasing mg:foo to it.
	 * @param userFacingName
	 *            The name to display in the UI, such as Lava or Iron Ore
	 * @param params
	 *            An implementation of the {@link INodeParams} interface
	 *            detailing the action of the node. {@link AirNodeParams} and
	 *            {@link DefaultNodeParams} are valid for air-like(display only)
	 *            and standard solid blocks, respectively.
	 * @param textures
	 *            A string stating the filename of the textures image.
	 * @param light
	 *            The amount of light from 0 to 255 to be emitted.
	 * @return The MapNode object that has been created and added to the
	 *         manager.
	 * @throws MossWorldLoadException
	 *             If an exception occurs during the execution of the
	 *             registering.
	 */
	public LiquidNode registerLiquid(String sysname, String userFacingName,
			LiquidNodeParams params, LiquidNodeParams sourceParams, String textures, int light)
			throws MossWorldLoadException {
		LiquidNode nd = new LiquidNode(sourceParams, textures, sysname, userFacingName,
				light);
		this.nm.putNode(nd);
		nd.level=0;
		for (int i = 1; i < 8; i++) {
			LiquidNode innerNd=new LiquidNode(params, textures, sysname+"$LEVEL$"+i, userFacingName, light);
			innerNd.setByBounds(-.5f, .5f, -.5f, .5f, -.5f, (i/8f)-0.5f);
			nd.liquidLevels[i]=innerNd;
			innerNd.liquidLevels=nd.liquidLevels;
			innerNd.level=i;
			this.nm.putNode(innerNd);
		}
		nd.liquidLevels[0]=nd;
		return nd;
	}

	/**
	 * Registers a node alias. Since the map generator and scripts work via
	 * string names, registering an alias of mg:dirt to myscript:specialdirt
	 * will cause a mapgen that recognizes mg:dirt as a generated element to use
	 * specialdirt for that.
	 * 
	 * @param alias
	 *            The alias to create, i.e. mg:dirt
	 * @param dst
	 *            The existing node to set as the alias target, i.e
	 *            myscript:specialdirt. This element must already exist.
	 */
	public void registerNodeAlias(String alias, String dst) {
		this.nm.putNodeAlias(alias, dst);
	}

	/**
	 * Register a new MapNode in the node manager.
	 * 
	 * @param sysname
	 *            The name such as default:dirt to set. The prefix mg: is used
	 *            for mapgen-specific nodes, and should be done by creating a
	 *            node with a different prefix and aliasing mg:foo to it.
	 * @param userFacingName
	 *            The name to display in the UI, such as Dirt or Iron Ore
	 * @param textures
	 *            A string stating the filename of the textures image.
	 * @param light
	 *            The amount of light from 0 to 255 to be emitted.
	 * @return The MapNode object that has been created and added to the
	 *         manager.
	 * @throws MossWorldLoadException
	 *             If an exception occurs during node registration.
	 */
	public MapNode registerNodeDefParams(String sysname, String userFacingName,
			String textures, int light) throws MossWorldLoadException {
		MapNode nd = new MapNode(new DefaultNodeParams(), textures, sysname,
				userFacingName, light);
		this.nm.putNode(nd);
		return nd;
	}

	/**
	 * Register a new liquid in the node manager.
	 * 
	 * @param sysname
	 *            The name such as default:lava to set. The prefix mg: is used
	 *            for mapgen-specific nodes, and should be done by creating a
	 *            node with a different prefix and aliasing mg:foo to it.
	 * @param userFacingName
	 *            The name to display in the UI, such as Dirt or Iron Ore
	 * @param textures
	 *            A string stating the filename of the textures image.
	 * @param light
	 *            The amount of light from 0 to 255 to be emitted.
	 * @return The LiquidNode object that has been created and added to the
	 *         manager.
	 * @throws MossWorldLoadException
	 *             If an exception occurs during node registration.
	 */
	public LiquidNode registerLiquidDefParams(String sysname,
			String userFacingName, String textures, int light)
			throws MossWorldLoadException {
		LiquidNode nd = new LiquidNode(new LiquidSourceNodeParams(), textures, sysname, userFacingName,
				light);
		this.nm.putNode(nd);
		nd.level=0;
		for (int i = 1; i < 8; i++) {
			LiquidNode innerNd=new LiquidNode(new LiquidFlowingNodeParams(), textures, sysname+"$LEVEL$"+i, userFacingName, light);
			innerNd.setByBounds(-.5f, .5f, -.5f, .5f, -.5f, (i/8f)-0.5f);
			nd.liquidLevels[i]=innerNd;
			innerNd.liquidLevels=nd.liquidLevels;
			innerNd.level=i;
			this.nm.putNode(innerNd);
		}
		nd.liquidLevels[0]=nd;
		return nd;
	}



	public MossInventory getInvByName(Player player, String name) {
		return null;
	}

	public MossInventory createInvByName(Player p, String name) {
		return null;
	}

	public Player getPlayerByName(String name) {
		return null;
	}

	public MapNode getNodeByName(String name) {
		return null;
	}

	public ScriptableDatabase getDb() {
		return this.db;
	}

	public MossScriptEnv(ScriptableDatabase db, NodeCache nc,
			FuturesProcessor fp, NodeManager nm) {
		this.db = db;
		this.nc = nc;
		this.fp = fp;
		this.nm = nm;
	}

	public FuturesProcessor getFuturesProcessor() {
		return this.fp;
	}

}
