package net.mosstest.servercore;

import java.util.HashMap;

public class SessionManager {

	HashMap<Player, ServerSession> playerSessions=new HashMap<>();
	volatile long gameTicks; 
}
