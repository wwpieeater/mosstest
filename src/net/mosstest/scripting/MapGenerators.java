package net.mosstest.scripting;

import java.util.ArrayList;
import java.util.Random;

import net.mosstest.servercore.MapChunk;
import net.mosstest.servercore.MapGeneratorException;
import net.mosstest.servercore.MapNode;
import net.mosstest.servercore.NodeManager;
import net.mosstest.servercore.Position;
import toxi.math.noise.SimplexNoise;

public class MapGenerators {
	private static volatile MapGenerator mg;

	public static void setDefaultMapGenerator(MapGenerator g, NodeManager nm, long seed,
			Object... params) throws MapGeneratorException {
		synchronized (MapGenerators.class) {
			mg = g;
			mg.init(seed, nm, params);
		}
	}

	public static MapGenerator getDefaultMapgen() {
		return mg;
	}

	public static class FlatMapGenerator implements MapGenerator {
		long seed;
		NodeManager nm;
		@Override
		public void init(long seed, NodeManager nm, Object... params)
				throws MapGeneratorException {
			this.seed = seed;
			this.nm=nm;
		}

		@Override
		public MapChunk generateChunk(Position pos)
				throws MapGeneratorException {
			int[][][] nodes = new int[16][16][16];
			try {
				int fillNode = (pos.getZ() >= 0) ? this.nm.getNode(
						"mg:air", false).getNodeId() : this.nm.getNode(
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
			int fillNode = (pos.getZ() >= 0) ? this.nm.getNode("mg:air",
					false).getNodeId() : this.nm
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
				double rarity, double clumping, int seed, Object... params) {
			// TODO Auto-generated method stub

		}

	}

	public static class SimplexMapGenerator implements MapGenerator {
		public static final int HEIGHT_AVG = 0;
		public static final int HEIGHT_JITTER = 100;
		public static final double SIMPLEX_SCALE_FACTOR = 1;
		public static final double SIMPLEX_LOCAL_SCALE_FACTOR = 10 * SIMPLEX_SCALE_FACTOR;
		public static final double SIMPLEX_ROOT_DEGREE = 2;
		public static final int SEA_LEVEL = 0;
		// todo tweak parameters
		// TODO finish simplex generator including ores
		long baseSeed;
		double elevationSeed;
		double dirtSeed;
		SimplexNoise elevationNoise = new SimplexNoise();
		ArrayList<Ore> ores = new ArrayList<>();
		NodeManager nm;
		@Override
		public void init(long seed, NodeManager nm,Object... params)
				throws MapGeneratorException {
			this.nm=nm;
			this.baseSeed = seed;
			Random rand = new Random(seed);

			// not sure if this is really going to work in terms of the range of
			// simplex. I'm guessing 2^32 is enough variation.
			this.elevationSeed = rand.nextInt();
			this.dirtSeed = rand.nextInt();

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
			//TODO make trees
			short grass = nm.getNode("mg:grass", false).getNodeId();
			short dirt = nm.getNode("mg:dirt", false).getNodeId();
			short stone = nm.getNode("mg:stone", false).getNodeId();
			short air = nm.getNode("mg:air", false).getNodeId();
			for (int x = 0; x < 16; x++) {
				long globalx = pos.getX() * 16 + x;
				for (int y = 0; y < 16; y++) {
					long globaly = pos.getY() * 16 + y;
					int elevation = (int) (HEIGHT_AVG + // average height
					(HEIGHT_JITTER * // jitter height
					(Math.pow(this.elevationNoise.noise( // get noise
							x * SIMPLEX_SCALE_FACTOR, // scale
							y * SIMPLEX_SCALE_FACTOR, // scale
							this.elevationSeed, this.elevationSeed), // seed
							SIMPLEX_ROOT_DEGREE) // emphasize peaks
					- 0.5))); // center on average height
					int dirtelevation = (int) (elevation - // max possible
															// height
					(3 * // jitter height
					(Math.pow(this.elevationNoise.noise( // get noise
							x * SIMPLEX_LOCAL_SCALE_FACTOR, // scale
							y * SIMPLEX_LOCAL_SCALE_FACTOR, // scale
							this.dirtSeed, this.elevationSeed), // seed
							1.0 / SIMPLEX_ROOT_DEGREE) // emphasize peaks
					)));
					inner: for (int z = 0; z < 16; z++) {
						long globalz = pos.getZ() * 16 + z;
						if (lightNodes[x][y][z] != 0)
							continue inner;
						if (globalz > elevation) {
							lightNodes[x][y][z] = air;
							continue inner;
						}
						if (globalz == elevation) {
							lightNodes[x][y][z] = grass;
							continue inner;
						}
						if (globalz > dirtelevation) {
							lightNodes[x][y][z] = dirt;
						}

						oreLoop: for (Ore ore : this.ores) {
							if (ore.checkOre(globalx, globaly, globalz)) {
								lightNodes[x][y][z] = ore.node.getNodeId();
								break oreLoop;
							} //if
						} //oreloop
					} //z
				} //y
			} //x
		} //method

		@Override
		public void registerOre(MapNode oreNode, double minDepth,
				double rarity, double clumping, int seed, Object... params) {
			Ore ore = new Ore(oreNode, seed, minDepth, rarity);

		}

		private class Ore {
			MapNode node;
			SimplexNoise noise;
			double minDepth;
			double cutoff;
			double simplexSeedPosition;
			int oreSeed;

			public Ore(MapNode node, int seed, double minDepth, double rarity) {
				Random random = new Random(seed);
				this.simplexSeedPosition = 10 * Integer.MAX_VALUE
						* random.nextDouble();
				this.oreSeed = seed;
				this.node = node;
				this.noise = new SimplexNoise();
				this.minDepth = minDepth;
				this.cutoff = 1.0 / rarity;
			}

			public boolean checkOre(long x, long y, long z) {
				Random r = new Random(SimplexMapGenerator.this.baseSeed
						- this.oreSeed - x + y - z);
				double preProb;
				if (z > (-1 * this.minDepth)) {
					preProb = 0;
					return false;
				}
				if (z > ((-1 * this.minDepth) - 100)) {
					preProb = ((100 + this.minDepth + z) / -100);
				} else
					preProb = 1;

				return (r.nextDouble() < preProb)
						&& (this.noise.noise(this.simplexSeedPosition, x, y, z) < this.cutoff);

			}
		}

	}
}
