package org.nodetest.servercore;

public class MossEvent {
	public MossEvent(EvtType type, Entity actor, long posx, long posy,
			long posz, MapNode nodeBefore, MapNode nodeAfter,
			Entity recvEntity, MossFormspec fspec, MossInventoryAction action,
			String initiatingMessage, ScriptSandboxBorderToken tok) {
		if (!(tok instanceof ScriptSandboxBorderToken) || tok == null)
			throw new SecurityException("Attempt to access controlled resources in the script DMZ.");
		this.type = type;
		this.actor = actor;
		this.posx = posx;
		this.posy = posy;
		this.posz = posz;
		this.nodeBefore = nodeBefore;
		this.nodeAfter = nodeAfter;
		this.recvEntity = recvEntity;
		this.fspec = fspec;
		this.action = action;
		this.initiatingMessage = initiatingMessage;
	}

	public enum EvtType {
		EVT_DIEPLAYER, EVT_DIGNODE, EVT_GENERATE, EVT_JOINPLAYER, EVT_QUITPLAYER, EVT_NEWPLAYER, EVT_PLACENODE, EVT_FSPEC_OPEN, EVT_FSPEC_SUBMIT, EVT_FSPEC_INVACTION, EVT_THREADSTOP, EVT_ENTITY_PUNCH, EVT_ENTITY_DEATH, EVT_CHATMESSAGE, EVT_SHUTDOWN, EVT_CHATCOMMAND, EVT_NODEMOVE
	}

	MossEvent.EvtType type;
	Entity actor; // Player extends entity
	long posx;
	long posy;
	long posz;
	MapNode nodeBefore;
	MapNode nodeAfter;
	Entity recvEntity;
	MossFormspec fspec;
	MossInventoryAction action;
	String initiatingMessage;
}
