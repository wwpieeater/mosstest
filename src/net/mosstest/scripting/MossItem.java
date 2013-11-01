package net.mosstest.scripting;

import net.mosstest.servercore.MossFile;

public class MossItem {
	public MossItem(MossFile invTex, MossFile wieldTex, double invWeight,
			boolean mayStack, String displayName, String technicalName) {
		this.invTex = invTex;
		this.wieldTex = wieldTex;
		this.invWeight = invWeight;
		this.mayStack = mayStack;
		this.displayName = displayName;
		this.technicalName = technicalName;
	}

	final MossFile invTex;
	final MossFile wieldTex;
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
		result = prime * result
				+ ((technicalName == null) ? 0 : technicalName.hashCode());
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
		if (technicalName == null) {
			if (other.technicalName != null)
				return false;
		} else if (!technicalName.equals(other.technicalName))
			return false;
		return true;
	}

}
