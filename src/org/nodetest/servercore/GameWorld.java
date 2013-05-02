package org.nodetest.servercore;

import java.util.HashMap;

import org.nodetest.common.Player;
import org.nodetest.common.ScriptEnv;


public class GameWorld {
	private MapDatabase db;
	boolean isServer;
	private ScriptEnv scriptenv;
	private HashMap<String, Player> players;
	private GameMap map;
	private HashMap<String, Entity> entities;
	public GameWorld(MapDatabase db, ScriptEnv scriptenv, boolean isServer) {
		super();
		this.db = db;
		this.scriptenv = new ScriptEnv(isServer, players, map, entities);
	}

	
}
