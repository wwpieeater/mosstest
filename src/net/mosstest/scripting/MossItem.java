package net.mosstest.scripting;

import net.mosstest.servercore.MossFile;

// TODO: Auto-generated Javadoc
/**
 * The Class MossItem.
 */
public class MossItem {
	
	/** The item id. */
	private short itemId = 0;

	/**
	 * Sets the item id.
	 *
	 * @param itemId the new item id
	 */
	public void setItemId(short itemId) {
		this.itemId = itemId;
	}

	/**
	 * Gets the item id.
	 *
	 * @return the item id
	 */
	public short getItemId() {
		return this.itemId;
	}

	/**
	 * Instantiates a new moss item.
	 *
	 * @param invTex the inv tex
	 * @param wieldTex the wield tex
	 * @param invWeight the inv weight
	 * @param mayStack the may stack
	 * @param displayName the display name
	 * @param technicalName the technical name
	 */
	public MossItem(String invTex, String wieldTex, double invWeight,
			boolean mayStack, String displayName, String technicalName) {
		this.invTex = invTex;
		this.wieldTex = wieldTex;
		this.invWeight = invWeight;
		this.mayStack = mayStack;
		this.displayName = displayName;
		this.technicalName = technicalName;
	}

	/** The inv tex. */
	final String invTex;
	
	/** The wield tex. */
	final String wieldTex;
	
	/** The inv weight. */
	final double invWeight;
	
	/** The may stack. */
	boolean mayStack;
	
	/** The display name. */
	final String displayName;
	
	/** The technical name. */
	final String technicalName;

	/**
	 * The Class Stack.
	 */
	public static class Stack {
		
		/**
		 * Instantiates a new stack.
		 *
		 * @param item the item
		 * @param amount the amount
		 */
		public Stack(MossItem item, double amount) {
			this.item = item;
			this.amount = amount;
		}

		/** The item. */
		final MossItem item;
		/**
		 * If amount is not an integer then it is a stack with wear. Once zero
		 * it is no longer a stack.
		 */
		double amount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Gets the technical name.
	 *
	 * @return the technical name
	 */
	public String getTechnicalName() {
		return technicalName;
	}

}
