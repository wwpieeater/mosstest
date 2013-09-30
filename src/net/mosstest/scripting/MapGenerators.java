package net.mosstest.scripting;

import net.mosstest.servercore.MapChunk;
import net.mosstest.servercore.MapGeneratorException;
import net.mosstest.servercore.NodeManager;
import net.mosstest.servercore.Position;

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

	}
}
