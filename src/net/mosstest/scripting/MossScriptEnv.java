package net.mosstest.scripting;

import java.util.ArrayList;
import java.util.EnumMap;

import net.mosstest.servercore.Entity;
import net.mosstest.servercore.ItemStack;
import net.mosstest.servercore.MapNode;
import net.mosstest.servercore.MossEvent;
import net.mosstest.servercore.MossInventory;
import net.mosstest.servercore.NodePosition;
import net.mosstest.servercore.Player;
import net.mosstest.servercore.Position;
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
	private static EnumMap<MossEvent.EvtType, ArrayList<MossEventHandler>> registeredScriptEvents = new EnumMap<>(
			MossEvent.EvtType.class);

	/**
	 * Registers an event hander to fire on a player death. This will be run in
	 * the event processor thread pool. The default handler shall respawn the
	 * player at the spawnpoint and set health to default.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	static void registerOnDieplayer(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_DIEPLAYER).add(r);
	}

	/**
	 * Registers an event hander to fire on a node being dug. This will be run
	 * in the event processor thread pool. The default handler will remove the
	 * node and add its drop to the inventory of the digger if possible.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	static void registerOnDignode(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_DIGNODE).add(r);
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
	static void registerOnGenerate(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_GENERATE).add(r);
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
	static void registerOnJoinplayer(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_JOINPLAYER).add(r);
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
	static void registerOnQuitplayer(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_QUITPLAYER).add(r);
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
	static void registerOnNewplayer(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_NEWPLAYER).add(r);
	}

	/**
	 * Registers an event hander to fire on a node being placed. This will be
	 * run in the event processor thread pool. The default handler removes the
	 * item from the placer's inventory and puts down a node or entity.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	static void registerOnPlacenode(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_PLACENODE).add(r);
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
	static void registerOnFspecOpen(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_FSPEC_OPEN).add(r);
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
	static void registerOnFspecSubmit(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_FSPEC_SUBMIT).add(r);
	}

	/**
	 * Registers an event hander to fire on an inventory action. This will be
	 * run in the event processor thread pool. The default handler performs the
	 * action as-is.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	static void registerOnInvAction(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_FSPEC_INVACTION)
				.add(r);
	}

	/**
	 * Registers an event hander to fire on an entity punch. This will be run in
	 * the event processor thread pool. The default handler performs no action.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	static void registerOnEntityPunch(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_ENTITY_PUNCH).add(r);
	}

	/**
	 * Registers an event hander to fire on a player taking damage. This will be
	 * run in the event processor thread pool. The default handler causes the
	 * player to take the damage.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	static void registerOnPlayerDamage(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_PLAYERDAMAGE).add(r);
	}

	/**
	 * Registers an event hander to fire on entity death. This will be run in
	 * the event processor thread pool. The default handler performs no action
	 * aside from the removal of said entity.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	static void registerOnEntityDeath(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_ENTITY_DEATH).add(r);
	}

	/**
	 * Registers an event hander to fire on a chat message from a client. This
	 * will be run in the event processor thread pool. The default handler sends
	 * the message to all players.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	static void registerOnChatMessage(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_CHATMESSAGE).add(r);
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
	static void registerOnShutdown(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_SHUTDOWN).add(r);
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
	static void registerOnChatCommand(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_CHATCOMMAND).add(r);
	}

	/**
	 * Registers an event hander to fire on a node move. This will be run in the
	 * event processor thread pool. The default handler moves the node.
	 * 
	 * @param r
	 *            The event handler to register.
	 */
	static void registerOnNodeMove(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_NODEMOVE).add(r);
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
	public static ArrayList<MossEventHandler> getHandlers(
			MossEvent.EvtType type, ScriptSandboxBorderToken tok)
			throws SecurityException {
		if (!(tok instanceof ScriptSandboxBorderToken) || tok == null)
			throw new SecurityException(
					"Attempt to access controlled resources in the script DMZ."); //$NON-NLS-1$
		return registeredScriptEvents.get(type);
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
	public static void sendChatMessage(Player recipient, Player from,
			String message) {
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
	public static void sendChatAll(Player from, String message) {
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
	public static void setHp(Entity ent, int health) {
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
	public static void damageTool(Player actor, MapNode nd)
			throws MossScriptException {
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
	public static boolean givePlayer(Player player, ItemStack item) {
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
	 */
	public static void setNode(NodePosition pos, MapNode node) {
		// TODO stub
	}

	/**
	 * Removes a node, setting it to air. This may be called on a NodePosition
	 * with an existing solid node.
	 * 
	 * @param pos
	 *            The position at which to remove the node.
	 */
	public static void removeNode(NodePosition pos) {
		// TODO stub
	}

	public static MapNode getNode(Position pos) {
		// TODO stub
		return null;
	}
	public static MapNode registerNode(String sysname, String userFacingName, NodeParams params, String[] textures, boolean isLiquid, int light) {
		MapNode nd=new MapNode(params, textures, sysname, userFacingName,isLiquid, light);
		return nd;
	}
	
	public static MapNode registerNodeDefParams(String sysname, String userFacingName, String[] textures, boolean isLiquid, int light) {
		MapNode nd=new MapNode(textures, sysname, userFacingName, isLiquid, light);
		return nd;
	}

	public static ItemStack[] getInventory(MossInventory inv) {
		return new ItemStack[] {};
		// TODO
	}

	public static MossInventory getInvByName(String name) {
		return null;
	}

	public static MossInventory createInvByName(String name) {
		return null;
	}

	public static Player getPlayerByName(String name) {
		return null;
	}

	public static MapNode getNodeByName(String name) {
		return null;
	}
	public static void includeScript(String name) {
		//TODO
	}

}
