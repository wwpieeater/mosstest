package net.mosstest.scripting;

import java.util.HashMap;
import java.util.HashSet;

public class Player {
	private HashMap<String, MossInventory> inventories = new HashMap<>();
	public final String name;
	public volatile double xoffset;
	public volatile double yoffset;
	public volatile double zoffset;
	public volatile int xchk;
	public volatile int ychk;
	public volatile int zchk;
	public volatile double xVelocity;
	public volatile double yVelocity;
	public volatile double zVelocity;
	public volatile double oldx;
	public volatile double oldy;
	public volatile double oldz;
	public volatile int oldxchk;
	public volatile int oldychk;
	public volatile int oldzchk;
	public volatile long lastAnticheatMillis;
	private HashSet<String> privs;
	/**
	 * Object to be synchronized on for
	 */
	public final Object antiCheatDataLock = new Object();

	private Player(String name, int maxHealth) {
		this.name = name;
		this.inventories.put("default", new MossInventory(96, 8, 6)); //$NON-NLS-1$
	}

	public MossInventory createInventory(String name, int rows, int cols,
			int maxStack) {
		MossInventory inv = new MossInventory(maxStack, rows, cols);
		this.inventories.put(name, inv);
		return inv;
	}

	public void respawn() {
		// TODO Auto-generated method stub

	}

	public void grantPrivilege(String... privs) {
		for (String priv : privs) {
			this.privs.add(priv);
		}
	}
	
	public void revokePrivilege(String... privs) {
		for (String priv : privs) {
			this.privs.remove(priv);
		}
	}

}
