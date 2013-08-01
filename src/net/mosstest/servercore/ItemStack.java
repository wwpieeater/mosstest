package org.nodetest.servercore;

public class ItemStack {
	MossItem item;
	int amount;
	final boolean mayStack;
	public ItemStack(MossItem item, int amount) {
		this.item = item;
		this.amount = amount;
		mayStack=true;
	}
	public ItemStack(MossItem item, int amount, boolean mayStack) {
		this.item = item;
		this.amount = amount;
		this.mayStack=mayStack;
	}
	
	
}
