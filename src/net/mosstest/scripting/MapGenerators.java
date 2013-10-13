package net.mosstest.scripting;

import java.util.Random;

import net.mosstest.servercore.MapChunk;
import net.mosstest.servercore.MapGeneratorException;
import net.mosstest.servercore.MapNode;
import net.mosstest.servercore.NodeManager;
import net.mosstest.servercore.Position;
import toxi.math.noise.SimplexNoise;

public class MapGenerators {
	private static volatile MapGenerator mg;

	public static void setDefaultMapGenerator(MapGenerator g, long seed,
			Object... params) throws MapGeneratorException {
		synchronized (MapGenerators.class) {
			mg = g;
			mg.init(seed, params);
		}
	}

	public static MapGenerator getDefaultMapgen() {
		return mg;
	}

	public static class FlatMapGenerator implements MapGenerator {
		long seed;

		@Override
		public void init(long seed, Object... params)
				throws MapGeneratorException {
			this.seed = seed;
		}

		@Override
		public MapChunk generateChunk(Position pos)
				throws MapGeneratorException {
			int[][][] nodes = new int[16][16][16];
			try {
				int fillNode = (pos.getZ() >= 0) ? NodeManager.getNode(
						"mg:air", false).getNodeId() : NodeManager.getNode(
						"mg:ground", false).getNodeId();

				for (int x = 0; x < 16; x++) {
					for (int y = 0; y < 16; y++) {
						for (int z = 0; z < 16; z++) {
							nodes[x][y][z] = fillNode;
						}
					}

				}
				return new MapChunk(pos, nodes, new boolean[16][16][16]);
			} catch (NullPointerException e) {
				throw new MapGeneratorException();
			}

		}

		@Override
		public void fillInChunk(int[][][] lightNodes, Position pos)
				throws MapGeneratorException {
			int fillNode = (pos.getZ() >= 0) ? NodeManager.getNode("mg:air",
					false).getNodeId() : NodeManager
					.getNode("mg:ground", false).getNodeId();

			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 16; y++) {
					for (int z = 0; z < 16; z++) {
						if (lightNodes[x][y][z] == 0) {
							// operation in place on passed in array as per
							// contract
							lightNodes[x][y][z] = fillNode;
						}
					}
				}

			}

		}

		@Override
		public void registerOre(MapNode oreNode, double minDepth,
				double rarity, double clumping, Object... params) {
			// TODO Auto-generated method stub

		}

	}

	public static class SimplexMapGenerator implements MapGenerator {
		// todo tweak parameters
		//TODO finish simplex generator including ores
		double seed;
		SimplexNoise sn=new SimplexNoise();
		@Override
		public void init(long seed, Object... params)
				throws MapGeneratorException {
			Random rand = new Random(seed);

			// not sure if this is really going to work in terms of the range of
			// simplex. I'm guessing 2^32 is enough variation.
			this.seed = rand.nextInt();

		}

		@Override
		public MapChunk generateChunk(Position pos)
				throws MapGeneratorException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void fillInChunk(int[][][] lightNodes, Position pos)
				throws MapGeneratorException {
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 16; y++) {
					inner: for (int z = 0; z < 16; z++) {
						if (lightNodes[x][y][z] != 0)
							continue inner;
						
						double simplexVal=this.sn.noise(pos.getX()+(x/16.0), pos.getY()+(y/16.0), pos.getZ()+(z/16.0), this.seed);
						
						
					}
				}

			}

		}

		@Override
		public void registerOre(MapNode oreNode, double minDepth,
				double rarity, double clumping, Object... params) {
			// TODO Auto-generated method stub

		}

	}
}
