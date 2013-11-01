package net.mosstest.scripting;

public class MossInventory {
	final int maxStackSize;
	final MossItem.Stack[][] stacks;

	public MossInventory(int maxStackSize, int rows, int cols) {
		this.maxStackSize = maxStackSize;
		stacks = new MossItem.Stack[rows][cols];
	}

	public synchronized double addItem(MossItem.Stack stack) {
		double added = 0;
		double maxAddable = stack.item.mayStack ? maxStackSize : 1;
		for (int row = 0; row < stacks.length; row++) {
			for (int col = 0; col < stacks[row].length; col++) {
				if (stacks[row][col] == null) {
					double addedThisRound = Math.min(stack.amount, maxAddable);
					added += addedThisRound;
					stacks[row][col] = new MossItem.Stack(stack.item, addedThisRound);
				}
				else if (stacks[row][col].item.equals(stack.item)){
					double addedThisRound = Math.min(stack.amount, maxAddable-stacks[row][col].amount);
					added += addedThisRound;
					stacks[row][col].amount += addedThisRound;
				}
				if(added>=stack.amount) return added;
			}
		}
		return added;

	}
	
	//TODO remove item

}
