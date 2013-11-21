package net.mosstest.scripting;

import net.mosstest.servercore.MossFile;

public class MossItem {
	private short itemId = 0;

	public void setItemId(short itemId) {
		this.itemId = itemId;
	}

	public short getItemId() {
		return this.itemId;
	}

	public MossItem(String invTex, String wieldTex, double invWeight,
			boolean mayStack, String displayName, String technicalName) {
		this.invTex = invTex;
		this.wieldTex = wieldTex;
		this.invWeight = invWeight;
		this.mayStack = mayStack;
		this.displayName = displayName;
		this.technicalName = technicalName;
	}

	final String invTex;
	final String wieldTex;
	final double invWeight;
	boolean mayStack;
	final String displayName;
	final String technicalName;

	public static class Stack {
		public Stack(MossItem item, double amount) {
			this.item = item;
			this.amount = amount;
		}

		final MossItem item;
		/**
		 * If amount is not an integer then it is a stack with wear. Once zero
		 * it is no longer a stack.
		 */
		double amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.technicalName == null) ? 0 : this.technicalName
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MossItem other = (MossItem) obj;
		if (this.technicalName == null) {
			if (other.technicalName != null)
				return false;
		} else if (!this.technicalName.equals(other.technicalName))
			return false;
		return true;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getTechnicalName() {
		return technicalName;
	}

}
