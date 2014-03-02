package net.mosstest.scripting;

import net.mosstest.servercore.ItemManager;

import java.io.*;

// TODO: Auto-generated Javadoc

/**
 * The Class MossInventory.
 */
public class MossInventory {

    /**
     * The max stack size.
     */
    final int maxStackSize;

    /**
     * The stacks.
     */
    final MossItem.Stack[][] stacks;

    /**
     * Instantiates a new moss inventory.
     *
     * @param maxStackSize the max stack size
     * @param rows         the rows
     * @param cols         the cols
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
    public final byte[] toBytes() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(this.maxStackSize);
        dos.writeInt(this.stacks.length);
        dos.writeInt(this.stacks[0].length);
        for (MossItem.Stack[] sa : this.stacks) {
            for (MossItem.Stack s : sa) {
                dos.writeUTF(s.item.getInternalName());
                dos.writeDouble(s.amount);
            }
        }
        dos.flush();
        bos.flush();
        return bos.toByteArray();
    }


    public MossInventory(byte[] buf, ItemManager im) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(buf)) {
            try (DataInputStream dis = new DataInputStream(bais)) {
                this.maxStackSize = dis.readInt();
                int rows = dis.readInt();
                int cols = dis.readInt();
                this.stacks = new MossItem.Stack[rows][cols];
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        MossItem itm = im.getItem(dis.readUTF());
                        double amount = dis.readDouble();
                        this.stacks[i][j] = new MossItem.Stack(itm, amount);
                    }
                }
            }
        }
    }


    public synchronized double addItem(MossItem.Stack stack) {
        double added = 0;
        for (int row = 0; row < this.stacks.length; row++) {
            for (int col = 0; col < this.stacks[row].length; col++) {
                if (this.stacks[row][col] == null) {
                    double addedThisRound = MossItem.Stack.getMaxSize(stack.item.stackMode, stack.amount - added, this.maxStackSize);
                    added += addedThisRound;
                    this.stacks[row][col] = new MossItem.Stack(stack.item,
                            addedThisRound);
                } else if (this.stacks[row][col].item.equals(stack.item)) {
                    double addedThisRound = this.stacks[row][col].getAddable(stack.item.stackMode, stack.amount - added, this.maxStackSize);
                    added += addedThisRound;
                    this.stacks[row][col].amount += addedThisRound;
                }
                if (added >= stack.amount)
                    return added;
            }
        }
        return added;

    }

    public synchronized MossItem.Stack removeItem(MossItem.Stack stack) {
        double removed = 0;
        for (int row = 0; row < this.stacks.length; row++)
            for (int col = 0; col < this.stacks[row].length; col++) {
                if (this.stacks[row][col].item.equals(stack.item)) {
                    double removedThisRound = this.stacks[row][col].getRemovable(stack.item.stackMode, stack.amount - removed);
                    removed += removedThisRound;
                    if (this.stacks[row][col].amount != removedThisRound)
                        this.stacks[row][col].amount -= removedThisRound;
                    else this.stacks[row][col] = null;

                }
                if (removed >= stack.amount)
                    return new MossItem.Stack(stack.item, removed);
            }
        return new MossItem.Stack(stack.item, removed);
    }

    // TODO remove item

    public enum RemovalMode {
        REMOVE_NET_AMOUNT,
        REMOVE_TOOL_LVL,
    }

}
