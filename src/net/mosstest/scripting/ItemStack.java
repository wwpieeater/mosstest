package net.mosstest.scripting;


public class ItemStack {
	MossItem item;
	int amount;
	final boolean mayStack;
	public ItemStack(MossItem item, int amount) {
		this.item = item;
		this.amount = amount;
		this.mayStack=true;
	}
	public ItemStack(MossItem item, int amount, boolean mayStack) {
		this.item = item;
		this.amount = amount;
		this.mayStack=mayStack;
	}
	
	
}
