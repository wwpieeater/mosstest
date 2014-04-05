package net.mosstest.tests;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import net.mosstest.scripting.MapChunk;
import net.mosstest.scripting.MapGenerators;
import net.mosstest.scripting.Position;
import net.mosstest.scripting.SimplexMapGenerator;
import net.mosstest.servercore.MapGeneratorException;

import org.junit.Before;
import org.junit.Test;

public class MapChunkTest {
	public static final Position[] positions = {
		new Position(1,1,1,0), new Position(2,4,6,0), new Position(-24,14,62,0), new Position(2,3,5,0), new Position(7,11,-13,0),
	};
	
	@Before
	public void setUp() throws MapGeneratorException {
		MapGenerators.setDefaultMapGenerator(new SimplexMapGenerator(), new MockNodeManager(), 1123581321, null);
	}

	@Test
	public void testByteArraySerialization() throws MapGeneratorException, IOException {
		for(Position p : positions) {
			MapChunk mc1 = MapGenerators.getDefaultMapgen().generateChunk(p);
			byte[] bytes = mc1.writeLight(false);
			MapChunk mc2 = new MapChunk(bytes);
			assertTrue(mc1.equals(mc2));
		}
	}

}
