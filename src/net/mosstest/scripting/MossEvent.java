package net.mosstest.scripting;

import net.mosstest.servercore.ScriptSandboxBorderToken;

public class MossEvent {
	public MossEvent(EvtType type, Player actor, NodePosition pos, MapNode nodeBefore, MapNode nodeAfter,
			Entity recvEntity, MossFormspec fspec, MossInventoryAction action, double damage,
			String initiatingMessage, ScriptSandboxBorderToken tok) {
		if (!(tok instanceof ScriptSandboxBorderToken) || tok == null)
			throw new SecurityException("Attempt to access controlled resources in the script DMZ.");
		this.type = type;
		this.actor = actor;
		this.pos=pos;
		this.nodeBefore = nodeBefore;
		this.nodeAfter = nodeAfter;
		this.recvEntity = recvEntity;
		this.fspec = fspec;
		this.damage=damage;
		this.action = action;
		this.initiatingMessage = initiatingMessage;
	}

	public enum EvtType {
		EVT_DIEPLAYER, EVT_DIGNODE, EVT_GENERATE, EVT_JOINPLAYER, EVT_QUITPLAYER, EVT_NEWPLAYER, EVT_PLACENODE, EVT_FSPEC_OPEN, EVT_FSPEC_SUBMIT, EVT_FSPEC_INVACTION, EVT_THREADSTOP, EVT_ENTITY_PUNCH, EVT_ENTITY_DEATH, EVT_CHATMESSAGE, EVT_SHUTDOWN, EVT_CHATCOMMAND, EVT_NODEMOVE, EVT_PLAYERDAMAGE
	}

	public final MossEvent.EvtType type;
	public final Player actor; // Player no longer extends entity
	public final NodePosition pos;
	public final MapNode nodeBefore;
	public final MapNode nodeAfter;
	public final Entity recvEntity;
	public final double damage;
	public final MossFormspec fspec;
	public final MossInventoryAction action;
	public final String initiatingMessage;
}
