package net.mosstest.scripting;

import java.util.ArrayList;

public class MossInventory {
	int maxStackSize;
	int rows;
	int cols;
	ArrayList<ArrayList<ItemStack>> stacks=new ArrayList<>();
	public MossInventory(int maxStackSize, int rows, int cols) {
		this.maxStackSize = maxStackSize;
		this.rows = rows;
		this.cols = cols;
	}
	
}
