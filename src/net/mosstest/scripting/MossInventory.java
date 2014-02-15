package net.mosstest.scripting;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

// TODO: Auto-generated Javadoc
/**
 * The Class MossInventory.
 */
public class MossInventory {
	
	/** The max stack size. */
	final int maxStackSize;
	
	/** The stacks. */
	final MossItem.Stack[][] stacks;

	/**
	 * Instantiates a new moss inventory.
	 *
	 * @param maxStackSize the max stack size
	 * @param rows the rows
	 * @param cols the cols
	 */
	public MossInventory(int maxStackSize, int rows, int cols) {
		this.maxStackSize = maxStackSize;
		this.stacks = new MossItem.Stack[rows][cols];
	}

	/**
	 * To bytes.
	 *
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public byte[] toBytes() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		dos.writeInt(this.stacks.length);
		dos.writeInt(this.stacks[0].length);
		for(MossItem.Stack[] sa: this.stacks) {
			for(MossItem.Stack s: sa) {
				dos.writeShort(s.item.getItemId());
				dos.writeDouble(s.amount);
			}
		}
		dos.flush();
		bos.flush();
		return bos.toByteArray();
	}
	
	/**
	 * Adds the item.
	 *
	 * @param stack the stack
	 * @return the double
	 */
	public synchronized double addItem(MossItem.Stack stack) {
		double added = 0;
		double maxAddable = stack.item.mayStack ? this.maxStackSize : 1;
		for (int row = 0; row < this.stacks.length; row++) {
			for (int col = 0; col < this.stacks[row].length; col++) {
				if (this.stacks[row][col] == null) {
					double addedThisRound = Math.min(stack.amount, maxAddable);
					added += addedThisRound;
					this.stacks[row][col] = new MossItem.Stack(stack.item,
							addedThisRound);
				} else if (this.stacks[row][col].item.equals(stack.item)) {
					double addedThisRound = Math.min(stack.amount, maxAddable
							- this.stacks[row][col].amount);
					added += addedThisRound;
					this.stacks[row][col].amount += addedThisRound;
				}
				if (added >= stack.amount)
					return added;
			}
		}
		return added;

	}

	// TODO remove item

}
