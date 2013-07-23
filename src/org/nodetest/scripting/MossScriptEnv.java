package org.nodetest.scripting;

import java.util.ArrayList;
import java.util.HashMap;

import org.nodetest.servercore.Entity;
import org.nodetest.servercore.MapNode;
import org.nodetest.servercore.MossEvent;
import org.nodetest.servercore.Player;
import org.nodetest.servercore.ScriptSandboxBorderToken;
/**
 * IMPORTANT, IMPORTANT, IMPORTANT. VERY IMPORTANT. THIS CLASS IS THE ONLY CLASS THAT
 * SCRIPTS CAN ACCESS. MAKE ALL FIELDS AND METHODS PRIVATE UNLESS IT IS
 * INTENDED TO FACE UNTRUSTED SCRIPTS, OR REQUIRE THAT SCRIPTS PASS AN INSTANCE OF
 * ScriptSandboxBorderToken.
 */
public class MossScriptEnv {
	/*
	 * IMPORTANT, IMPORTANT, IMPORTANT. VERY IMPORTANT. THIS CLASS IS THE ONLY CLASS THAT
	 * SCRIPTS CAN ACCESS. MAKE ALL FIELDS AND METHODS PRIVATE UNLESS IT IS
	 * INTENDED TO FACE UNTRUSTED SCRIPTS.
	 */
	private static HashMap<MossEvent.EvtType, ArrayList<MossEventHandler>> registeredScriptEvents = new HashMap<>();

	static void registerOnDieplayer(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_DIEPLAYER).add(r);
	}

	static void registerOnDignode(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_DIGNODE).add(r);
	}

	static void registerOnGenerate(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_GENERATE).add(r);
	}

	static void registerOnJoinplayer(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_JOINPLAYER).add(r);
	}

	static void registerOnQuitplayer(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_QUITPLAYER).add(r);
	}

	static void registerOnNewplayer(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_NEWPLAYER).add(r);
	}

	static void registerOnPlacenode(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_PLACENODE).add(r);
	}

	static void registerOnFspecOpen(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_FSPEC_OPEN).add(r);
	}

	static void registerOnFspecSubmit(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_FSPEC_SUBMIT).add(r);
	}

	static void registerOnInvaction(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_FSPEC_INVACTION)
				.add(r);
	}

	static void registerOnEntityPunch(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_ENTITY_PUNCH).add(r);
	}

	static void registerOnEntityDeath(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_ENTITY_DEATH).add(r);
	}

	static void registerOnChatmessage(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_CHATMESSAGE).add(r);
	}

	static void registerOnShutdown(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_SHUTDOWN).add(r);
	}

	static void registerOnChatCommand(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_CHATCOMMAND).add(r);
	}

	static void registerOnNodeMove(MossEventHandler r) {
		registeredScriptEvents.get(MossEvent.EvtType.EVT_NODEMOVE).add(r);
	}

	public static ArrayList<MossEventHandler> getHandlers(MossEvent.EvtType type,
			ScriptSandboxBorderToken tok) throws SecurityException {
		if (!(tok instanceof ScriptSandboxBorderToken) || tok == null)
			throw new SecurityException(
					"Attempt to access controlled resources in the script DMZ.");
		return registeredScriptEvents.get(type);
	}

	
	public static void sendChatMessage(Player recipient, Player from, String message) {
				//TODO 
	}

	public static void sendChatAll(Player actor, String initiatingMessage) {
				//TODO
	}

	public static void setHp(Entity actor, int i) {
		//TODO Once we have players doing stuff
	}

	public static void damageTool(Player actor, MapNode nodeBefore) throws MossScriptException{
		// TODO Auto-generated method stub
		
	}

	public static void givePlayer(Player player, MapNode nodeBefore) {
		// TODO Auto-generated method stub
	}


	
	//TODO JS functions for accessing databases, nodes, etc, etc, etc, etc, etc

}
