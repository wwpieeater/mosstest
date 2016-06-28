package net.mosstest.scripting;

// TODO: Auto-generated Javadoc
/**
 * The Class MapStructure.
 */
public abstract class MapStructure {
	
	/** The mse. */
	protected final MossScriptEnv mse;
	
	/** The seed. */
	protected final long seed;
	/**
	 * Gets a node at a certain position relative to the offset.
	 * 
	 * @param x
	 *            X relative to offset
	 * @param y
	 *            Y relative to offset
	 * @param z
	 *            Z relative to offset
	 * @return A MapNode object representing the mapnode to place, or null for
	 *         blank.
	 */
	public abstract MapNode getNodeAt(int x, int y, int z);

	/**
	 * Gets the x offset relative to the "natural position" at ground level and
	 * in a selected location.
	 * 
	 * @return The x offset.
	 */
	public abstract int getXOffset();

	/**
	 * Gets the y offset relative to the "natural position" at ground level and
	 * in a selected location.
	 * 
	 * @return The y offset.
	 */
	public abstract int getYOffset();

	/**
	 * Gets the z offset relative to the "natural position" at ground level and
	 * in a selected location.
	 * 
	 * @return The z offset.
	 */
	public abstract int getZOffset();

	/**
	 * Instantiates a new map structure.
	 *
	 * @param mse the mse
	 * @param seed the seed
	 */
	public MapStructure(MossScriptEnv mse, long seed) {
		this.mse = mse;
		this.seed = seed;
	}
}
