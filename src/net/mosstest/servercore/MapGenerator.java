package net.mosstest.servercore;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.triangulate.VoronoiDiagramBuilder;


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
