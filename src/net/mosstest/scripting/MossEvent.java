package net.mosstest.scripting;

import net.mosstest.servercore.ScriptSandboxBorderToken;

// TODO: Auto-generated Javadoc
/**
 * The Class MossEvent.
 */
public class MossEvent {
	
	/**
	 * Instantiates a new moss event.
	 *
	 * @param type the type
	 * @param actor the actor
	 * @param pos the pos
	 * @param nodeBefore the node before
	 * @param nodeAfter the node after
	 * @param recvEntity the recv entity
	 * @param fspec the fspec
	 * @param action the action
	 * @param damage the damage
	 * @param initiatingMessage the initiating message
	 * @param tok the tok
	 */
	public MossEvent(EvtType type, Player actor, NodePosition pos, MapNode nodeBefore, MapNode nodeAfter,
			Entity recvEntity, MossFormspec fspec, MossInventoryAction action, double damage,
			String initiatingMessage, ScriptSandboxBorderToken tok) {
		if (!(tok instanceof ScriptSandboxBorderToken) || tok == null)
			throw new SecurityException(Messages.getString("MossEvent.MSG_CROSS_DMZ_SECURITY_WARNING")); //$NON-NLS-1$
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

	/**
	 * The Enum EvtType.
	 */
	public enum EvtType {
		
		/** The evt dieplayer. */
		EVT_DIEPLAYER, 
 /** The evt dignode. */
 EVT_DIGNODE, 
 /** The evt generate. */
 EVT_GENERATE, 
 /** The evt joinplayer. */
 EVT_JOINPLAYER, 
 /** The evt quitplayer. */
 EVT_QUITPLAYER, 
 /** The evt newplayer. */
 EVT_NEWPLAYER, 
 /** The evt placenode. */
 EVT_PLACENODE, 
 /** The evt fspec open. */
 EVT_FSPEC_OPEN, 
 /** The evt fspec submit. */
 EVT_FSPEC_SUBMIT, 
 /** The evt fspec invaction. */
 EVT_FSPEC_INVACTION, 
 /** The evt threadstop. */
 EVT_THREADSTOP, 
 /** The evt entity punch. */
 EVT_ENTITY_PUNCH, 
 /** The evt entity death. */
 EVT_ENTITY_DEATH, 
 /** The evt chatmessage. */
 EVT_CHATMESSAGE, 
 /** The evt shutdown. */
 EVT_SHUTDOWN, 
 /** The evt chatcommand. */
 EVT_CHATCOMMAND, 
 /** The evt nodemove. */
 EVT_NODEMOVE, 
 /** The evt playerdamage. */
 EVT_PLAYERDAMAGE
	}

	/** The type. */
	public final MossEvent.EvtType type;
	
	/** The actor. */
	public final Player actor; // Player no longer extends entity
	
	/** The pos. */
	public final NodePosition pos;
	
	/** The node before. */
	public final MapNode nodeBefore;
	
	/** The node after. */
	public final MapNode nodeAfter;
	
	/** The recv entity. */
	public final Entity recvEntity;
	
	/** The damage. */
	public final double damage;
	
	/** The fspec. */
	public final MossFormspec fspec;
	
	/** The action. */
	public final MossInventoryAction action;
	
	/** The initiating message. */
	public final String initiatingMessage;
}
