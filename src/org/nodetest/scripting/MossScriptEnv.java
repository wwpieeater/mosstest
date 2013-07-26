package org.nodetest.scripting;

import java.util.ArrayList;
import java.util.EnumMap;

import org.nodetest.servercore.Entity;
import org.nodetest.servercore.ItemStack;
import org.nodetest.servercore.MapNode;
import org.nodetest.servercore.MossEvent;
import org.nodetest.servercore.MossInventory;
import org.nodetest.servercore.Player;
import org.nodetest.servercore.Position;
import org.nodetest.servercore.ScriptSandboxBorderToken;

/**
 * This class is used by scripts and script-facing portions of Mosstest. Methods
 * restricted to be called from trusted Java-side shall pass a
 * {@link ScriptSandboxBorderToken}. Each event fired will run in the thread
 * pool. All requests via this API need not concern themselves with threading as
 * everything is handled by Mosstest itself.
 * 
 * An event handler may interrupt handling of the event so that no further event
 * handlers nor the default are ever called, by throwing an instance of
 * {@link EventProcessingCompletedSignal}.
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
	static void registerOnInvaction(MossEventHandler r) {
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

	public static ArrayList<MossEventHandler> getHandlers(
			MossEvent.EvtType type, ScriptSandboxBorderToken tok)
			throws SecurityException {
		if (!(tok instanceof ScriptSandboxBorderToken) || tok == null)
			throw new SecurityException(
					"Attempt to access controlled resources in the script DMZ.");
		return registeredScriptEvents.get(type);
	}

	public static void sendChatMessage(Player recipient, Player from,
			String message) {
		// TODO
	}

	public static void sendChatAll(Player actor, String initiatingMessage) {
		// TODO
	}

	public static void setHp(Entity actor, int i) {
		// TODO Once we have players doing stuff
	}

	public static void damageTool(Player actor, MapNode nodeBefore)
			throws MossScriptException {
		// TODO Auto-generated method stub

	}

	public static void givePlayer(Player player, MapNode nodeBefore) {
		// TODO Auto-generated method stub
	}

	public static void setNode(Position pos, String n) {
		// TODO stub
	}

	public static MapNode getNode(Position pos) {
		// TODO stub
		return null;
	}

	public static ItemStack[] getInventory(MossInventory inv) {
		return null;
		// TODO
	}

	
	
	public static MossInventory getInvByName(String name){
		return null;
	}
	public static MossInventory createInvByName(String name){
		return null;
	}
	public static Player getPlayerByName(String name){
		return null;
	}
	public static MapNode getNodeByName(String name){
		return null;
	}

}
