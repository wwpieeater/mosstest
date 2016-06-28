package net.mosstest.scripting;

import net.mosstest.servercore.INodeManager;
import net.mosstest.servercore.MapGeneratorException;

// TODO: Auto-generated Javadoc
/**
 * The Interface IMapGenerator.
 */
public interface IMapGenerator {

	/**
	 * Inits the.
	 *
	 * @param seed the seed
	 * @param nm the nm
	 * @param params the params
	 * @throws MapGeneratorException the map generator exception
	 */
	void init(long seed, INodeManager nm, Object... params) throws MapGeneratorException;

	/**
	 * Generate chunk.
	 *
	 * @param pos the pos
	 * @return the map chunk
	 * @throws MapGeneratorException the map generator exception
	 */
	public MapChunk generateChunk(Position pos) throws MapGeneratorException;

	/**
	 * Fills in a chunk as an array of light nodes, where 0 values require
	 * filling.
	 * 
	 * @param lightNodes
	 *            A reference to the array that must be updated.
	 * @param pos
	 *            The position corresponding to the chunk to be filled in.
	 * @throws MapGeneratorException
	 *             Thrown in case the map generator encounters an inconsistency
	 *             it cannot recover from.
	 */
	public void fillInChunk(int[][][] lightNodes, Position pos)
			throws MapGeneratorException;

	/**
	 * Register an ore for future generation of chunks. The values given here
	 * are hints to the map generator and need not be exact.
	 *
	 * @param oreNode            The ore to generate.
	 * @param minDepth            The minimum depth(as a positive integer number of nodes below
	 *            sea level.
	 * @param rarity            How rare the ore should be. Rarity 1 corresponds to virtually
	 *            every ore-containing node, and rarity 100 would correspond to
	 *            approximately one out of 100 nodes being this ore.
	 * @param clumping            Defines how clumped the ore should be. Generally corresponds
	 *            to 0=uniform and 1=most strongly clumped(solid spheres of ore
	 *            clumps to meet rarity goal)
	 * @param seed the seed
	 * @param params            Miscellaneous params for the map generator.
	 */
	public void registerOre(MapNode oreNode, double minDepth, double rarity,
			double clumping, int seed, Object... params);

	
}
