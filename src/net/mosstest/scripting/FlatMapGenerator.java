package net.mosstest.scripting;

import net.mosstest.servercore.INodeManager;
import net.mosstest.servercore.MapGeneratorException;

public class FlatMapGenerator implements IMapGenerator {
	long seed;
	INodeManager nm;
	@Override
	public void init(long seed, INodeManager nm, Object... params)
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