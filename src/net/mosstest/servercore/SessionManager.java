package net.mosstest.servercore;

import java.util.HashMap;

import net.mosstest.scripting.Player;

public class SessionManager {

	public HashMap<Player, ServerSession> playerSessions=new HashMap<>();
	volatile long gameTicks; 
}
