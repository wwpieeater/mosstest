package net.mosstest.servercore;

import java.util.HashMap;

public class Player {
	private HashMap<String, MossInventory> inventories=new HashMap<>();
	public final String name;
	public volatile double xpos;
	public volatile double ypos;
	public volatile double zpos;
	public volatile double oldx;
	public volatile double oldy;
	public volatile double oldz;
	
	public volatile long lastAnticheatMillis;
	
	/**
	 * Object to be synchronized on for 
	 */
	public final Object antiCheatDataLock= new Object();
	
	private Player(String name, int maxHealth) {
		this.name=name;
		this.inventories.put("default", new MossInventory(96, 8, 6));
	}
	public MossInventory createInventory(String name, int rows, int cols, int maxStack) {
		MossInventory inv=new MossInventory(maxStack, rows, cols);
		this.inventories.put(name,inv);
		return inv;
	}
	public void respawn() {
		// TODO Auto-generated method stub
		
	}

}
