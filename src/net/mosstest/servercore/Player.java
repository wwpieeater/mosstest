package net.mosstest.servercore;

import java.util.HashMap;

public class Player extends Entity{
	private HashMap<String, MossInventory> inventories=new HashMap<>();
	private Player(String name, int maxHealth) {
		super(name, maxHealth);
		inventories.put("default", new MossInventory(96, 8, 6));
	}
	public MossInventory createInventory(String name, int rows, int cols, int maxStack) {
		MossInventory inv=new MossInventory(maxStack, rows, cols);
		this.inventories.put(name,inv);
		return inv;
	}

}
