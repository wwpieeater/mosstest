package net.mosstest.servercore;

import java.util.HashMap;

import net.mosstest.scripting.Player;

// TODO: Auto-generated Javadoc
/**
 * The Class SessionManager.
 */
public class SessionManager {

	/** The player sessions. */
	public HashMap<Player, ServerSession> playerSessions=new HashMap<>();
	
	/** The game ticks. */
	volatile long gameTicks; 
}
