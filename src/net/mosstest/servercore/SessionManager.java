package net.mosstest.servercore;

import java.util.HashMap;

public class SessionManager {

	public HashMap<Player, ServerSession> playerSessions=new HashMap<>();
	volatile long gameTicks; 
}
