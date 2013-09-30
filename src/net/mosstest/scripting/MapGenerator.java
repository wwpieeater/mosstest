package net.mosstest.scripting;

import net.mosstest.servercore.MapChunk;
import net.mosstest.servercore.MapGeneratorException;
import net.mosstest.servercore.Position;


public interface MapGenerator {
	
	void init(long seed, Object... params) throws MapGeneratorException;
	
	public MapChunk generateChunk(Position pos) throws MapGeneratorException;

	/**
	 * Fills in a chunk as an array of light nodes, where 
	 * @param lightNodes
	 * @param pos
	 * @throws MapGeneratorException
	 */
	public void fillInChunk(int[][][] lightNodes, Position pos) throws MapGeneratorException;
	
}
