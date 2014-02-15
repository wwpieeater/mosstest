package net.mosstest.scripting;

import java.util.ArrayList;
import java.util.Random;

import org.apache.log4j.Logger;

import net.mosstest.servercore.INodeManager;
import net.mosstest.servercore.MapGeneratorException;
import net.mosstest.servercore.MossDebugUtils;
import toxi.math.noise.SimplexNoise;

// TODO: Auto-generated Javadoc
/**
 * The Class SimplexMapGenerator.
 */
public class SimplexMapGenerator implements IMapGenerator {

	/** The logger. */
	static Logger logger = Logger.getLogger(SimplexMapGenerator.class);

	/** The Constant HEIGHT_AVG. */
	public static final double HEIGHT_AVG = 0;
	
	/** The Constant HEIGHT_JITTER. */
	public static final double HEIGHT_JITTER = 10;
	
	/** The Constant SIMPLEX_SCALE_FACTOR. */
	public static final double SIMPLEX_SCALE_FACTOR = 0.03;
	
	/** The Constant SIMPLEX_LOCAL_SCALE_FACTOR. */
	public static final double SIMPLEX_LOCAL_SCALE_FACTOR = 10 * SIMPLEX_SCALE_FACTOR;
	
	/** The Constant SIMPLEX_ROOT_DEGREE. */
	public static final double SIMPLEX_ROOT_DEGREE = 2;
	
	/** The Constant SEA_LEVEL. */
	public static final int SEA_LEVEL = 0;
	// todo tweak parameters
	// TODO finish simplex generator including ores
	/** The base seed. */
	long baseSeed;
	
	/** The elevation seed. */
	double elevationSeed;
	
	/** The dirt seed. */
	double dirtSeed;
	
	/** The humidity seed. */
	double humiditySeed;
	
	/** The elevation noise. */
	SimplexNoise elevationNoise = new SimplexNoise();
	
	/** The ores. */
	ArrayList<Ore> ores = new ArrayList<>();
	
	/** The nm. */
	INodeManager nm;

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.IMapGenerator#init(long, net.mosstest.servercore.INodeManager, java.lang.Object[])
	 */
	@Override
	public void init(long seed, INodeManager nm, Object... params)
			throws MapGeneratorException {
		this.nm = nm;
		this.baseSeed = seed;
		Random rand = new Random(seed);

		// not sure if this is really going to work in terms of the range of
		// simplex. I'm guessing 2^32 is enough variation.
		this.elevationSeed = rand.nextInt(1 << 16) + rand.nextDouble();
		this.dirtSeed = rand.nextInt(1 << 16) + rand.nextDouble();
		this.humiditySeed = rand.nextInt(1 << 16) + rand.nextDouble();
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.IMapGenerator#generateChunk(net.mosstest.scripting.Position)
	 */
	@Override
	public MapChunk generateChunk(Position pos) throws MapGeneratorException {
		int[][][] lightNodes = new int[16][16][16];
		short grass = this.nm.getNode("mg:grass", false).getNodeId(); //$NON-NLS-1$
		short dirt = this.nm.getNode("mg:dirt", false).getNodeId(); //$NON-NLS-1$
		short stone = this.nm.getNode("mg:stone", false).getNodeId(); //$NON-NLS-1$
		short air = 0; //this.nm.getNode("mg:air", false).getNodeId(); //$NON-NLS-1$
		System.err.println("<<< AIR:" + air);
		short sand = this.nm.getNode("mg:sand", false).getNodeId(); //$NON-NLS-1$
		for (int x = 0; x < 16; x++) {
			long globalx = pos.getX() * 16 + x;
			for (int y = 0; y < 16; y++) {
				long globaly = pos.getY() * 16 + y;
				double elRawNoise = this.elevationNoise.noise( // get noise
						globalx * SIMPLEX_SCALE_FACTOR, // scale
						globaly * SIMPLEX_SCALE_FACTOR, // scale
						this.elevationSeed, this.elevationSeed);// , // seed
				int elevation = (int) (HEIGHT_AVG + (HEIGHT_JITTER * elRawNoise - 0.5)); // center
																							// on
																							// average
																							// height

				// System.err.println("("+x+", "+y+", "+elevation+")");
				int dirtelevation = (int) (elevation - // max possible
														// height
				(3 * // jitter height
				(Math.pow(this.elevationNoise.noise( // get noise
						x * SIMPLEX_LOCAL_SCALE_FACTOR, // scale
						y * SIMPLEX_LOCAL_SCALE_FACTOR, // scale
						this.dirtSeed, this.elevationSeed), // seed
						1.0 / SIMPLEX_ROOT_DEGREE) // emphasize peaks
				)));
				double humidity = this.elevationNoise.noise(this.elevationSeed,
						pos.getY() * 16 + y, this.humiditySeed, pos.getX() * 16
								+ x);
				inner: for (int z = 0; z < 16; z++) {
					long globalz = pos.getZ() * 16 + z;
					if (lightNodes[x][y][z] != 0)
						continue inner;
					if (globalz > elevation) {
						lightNodes[x][y][z] = air;
						continue inner;
					}
					if (globalz == elevation) {
						lightNodes[x][y][z] = (humidity >= 0) ? grass : sand;
						continue inner;
					}
					if (globalz > dirtelevation) {
						lightNodes[x][y][z] = (humidity >= 0) ? dirt : sand;
					}
					lightNodes[x][y][z] = stone;
					oreLoop: for (Ore ore : this.ores) {
						if (ore.checkOre(globalx, globaly, globalz)) {
							System.err.println(ore.node.getNodeId());
							lightNodes[x][y][z] = ore.node.getNodeId();
							break oreLoop;
						} // if
					} // oreloop
				} // z
			} // y
		} // x

		return new MapChunk(pos, lightNodes, null);
	}

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.IMapGenerator#fillInChunk(int[][][], net.mosstest.scripting.Position)
	 */
	@Override
	public void fillInChunk(int[][][] lightNodes, Position pos)
			throws MapGeneratorException {
		// TODO make trees
		short grass = this.nm.getNode("mg:grass", false).getNodeId(); //$NON-NLS-1$
		short dirt = this.nm.getNode("mg:dirt", false).getNodeId(); //$NON-NLS-1$
		short stone = this.nm.getNode("mg:stone", false).getNodeId(); //$NON-NLS-1$
		short air = this.nm.getNode("mg:air", false).getNodeId(); //$NON-NLS-1$
		short sand = this.nm.getNode("mg:sand", false).getNodeId(); //$NON-NLS-1$
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
				logger.debug("(" + x + ", " + y + ", " + elevation + ")");

				int dirtelevation = (int) (elevation - // max possible
														// height
				(3 * // jitter height
				(Math.pow(this.elevationNoise.noise( // get noise
						x * SIMPLEX_LOCAL_SCALE_FACTOR, // scale
						y * SIMPLEX_LOCAL_SCALE_FACTOR, // scale
						this.dirtSeed, this.elevationSeed), // seed
						1.0 / SIMPLEX_ROOT_DEGREE) // emphasize peaks
				)));
				double humidity = this.elevationNoise.noise(this.elevationSeed,
						pos.getY() * 16 + y, this.humiditySeed, pos.getX() * 16
								+ x);
				inner: for (int z = 0; z < 16; z++) {
					long globalz = pos.getZ() * 16 + z;
					if (lightNodes[x][y][z] != 0)
						continue inner;
					if (globalz > elevation) {
						lightNodes[x][y][z] = air;
						continue inner;
					}
					if (globalz == elevation) {
						lightNodes[x][y][z] = (humidity >= 0.5) ? grass : sand;
						continue inner;
					}
					if (globalz > dirtelevation) {
						lightNodes[x][y][z] = (humidity >= 0.5) ? dirt : sand;
					}

					oreLoop: for (Ore ore : this.ores) {
						if (ore.checkOre(globalx, globaly, globalz)) {
							lightNodes[x][y][z] = ore.node.getNodeId();
							break oreLoop;
						} // if
					} // oreloop
				} // z
			} // y
		} // x
	} // method

	/* (non-Javadoc)
	 * @see net.mosstest.scripting.IMapGenerator#registerOre(net.mosstest.scripting.MapNode, double, double, double, int, java.lang.Object[])
	 */
	@Override
	public void registerOre(MapNode oreNode, double minDepth, double rarity,
			double clumping, int seed, Object... params) {
		Ore ore = new Ore(oreNode, seed, minDepth, rarity);

	}

	/**
	 * The Class Ore.
	 */
	private class Ore {
		
		/** The node. */
		MapNode node;
		
		/** The noise. */
		SimplexNoise noise;
		
		/** The min depth. */
		double minDepth;
		
		/** The cutoff. */
		double cutoff;
		
		/** The simplex seed position. */
		double simplexSeedPosition;
		
		/** The ore seed. */
		int oreSeed;

		/**
		 * Instantiates a new ore.
		 *
		 * @param node the node
		 * @param seed the seed
		 * @param minDepth the min depth
		 * @param rarity the rarity
		 */
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

		/**
		 * Check ore.
		 *
		 * @param x the x
		 * @param y the y
		 * @param z the z
		 * @return true, if successful
		 */
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