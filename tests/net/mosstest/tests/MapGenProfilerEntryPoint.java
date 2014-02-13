package net.mosstest.tests;

import java.util.HashMap;
import java.util.Random;

import net.mosstest.scripting.MapChunk;
import net.mosstest.scripting.MapGenerators;
import net.mosstest.scripting.Position;
import net.mosstest.scripting.SimplexMapGenerator;
import net.mosstest.servercore.MapGeneratorException;

public class MapGenProfilerEntryPoint {
	public static void main(String[] args) throws MapGeneratorException, InterruptedException {
		int chks = 0;
		Thread.sleep(10000); // sleep to allow profiler launch
		MapGenerators.setDefaultMapGenerator(new SimplexMapGenerator(),
				new MockNodeManager(), 1011, null);
		HashMap<Position, MapChunk> chunks = new HashMap<>();
		Random rand = new Random();
		whileLoop: while (true) {
			long sTime = System.currentTimeMillis();
			for (int i = 0; i < 100; i++) {
				Position pos = new Position(rand.nextInt(), rand.nextInt(),
						rand.nextInt(), 0);

				MapChunk chk = MapGenerators.getDefaultMapgen().generateChunk(
						pos);

				chunks.put(pos, chk);
				chks++;
				if(chks > 10000) {
					break whileLoop;
				}
			}
			System.err.println(System.currentTimeMillis() - sTime + ", " + chks);
		}
		while(true) {
			Thread.sleep(1000);
		}
	}
	
}
