package net.mosstest.servercore;

import java.util.HashMap;

public class Player {
	private HashMap<String, MossInventory> inventories=new HashMap<>();
	public final String name;
	private Player(String name, int maxHealth) {
		this.name=name;
		this.inventories.put("default", new MossInventory(96, 8, 6));
	}
	public MossInventory createInventory(String name, int rows, int cols, int maxStack) {
		MossInventory inv=new MossInventory(maxStack, rows, cols);
		this.inventories.put(name,inv);
		return inv;
	}

}
