package net.mosstest.scripting;

import net.mosstest.servercore.INodeManager;
import net.mosstest.servercore.MapGeneratorException;

// TODO: Auto-generated Javadoc

/**
 * The Class FlatMapGenerator.
 */
public class FlatMapGenerator implements IMapGenerator {

    public static final int CHUNK_DIMENSION = 16;
    /**
     * The seed.
     */
    long seed;

    /**
     * The nm.
     */
    INodeManager nm;

    /* (non-Javadoc)
     * @see net.mosstest.scripting.IMapGenerator#init(long, net.mosstest.servercore.INodeManager, java.lang.Object[])
     */
    @Override
    public void init(long seed, INodeManager nm, Object... params)
            throws MapGeneratorException {
        this.seed = seed;
        this.nm = nm;
    }

    /* (non-Javadoc)
     * @see net.mosstest.scripting.IMapGenerator#generateChunk(net.mosstest.scripting.Position)
     */
    @Override
    public MapChunk generateChunk(Position pos)
            throws MapGeneratorException {
        int[][][] nodes = new int[CHUNK_DIMENSION][CHUNK_DIMENSION][CHUNK_DIMENSION];
        try {
            int fillNode = (pos.getZ() >= 0) ? this.nm.getNode(
                    "mg:air", false).getNodeId() : this.nm.getNode( //$NON-NLS-1$
                    "mg:ground", false).getNodeId(); //$NON-NLS-1$

            for (int x = 0; x < CHUNK_DIMENSION; x++) {
                for (int y = 0; y < CHUNK_DIMENSION; y++) {
                    for (int z = 0; z < CHUNK_DIMENSION; z++) {
                        nodes[x][y][z] = fillNode;
                    }
                }

            }
            return new MapChunk(pos, nodes, new boolean[CHUNK_DIMENSION][CHUNK_DIMENSION][CHUNK_DIMENSION]);
        } catch (NullPointerException e) {
            throw new MapGeneratorException();
        }

    }

    /* (non-Javadoc)
     * @see net.mosstest.scripting.IMapGenerator#fillInChunk(int[][][], net.mosstest.scripting.Position)
     */
    @Override
    public void fillInChunk(int[][][] lightNodes, Position pos)
            throws MapGeneratorException {
        int fillNode = (pos.getZ() >= 0) ? this.nm.getNode("mg:air", //$NON-NLS-1$
                false).getNodeId() : this.nm
                .getNode("mg:ground", false).getNodeId(); //$NON-NLS-1$

        for (int x = 0; x < CHUNK_DIMENSION; x++) {
            for (int y = 0; y < CHUNK_DIMENSION; y++) {
                for (int z = 0; z < CHUNK_DIMENSION; z++) {
                    if (lightNodes[x][y][z] == 0) {
                        // operation in place on passed in array as per
                        // contract
                        lightNodes[x][y][z] = fillNode;
                    }
                }
            }

        }

    }

    /* (non-Javadoc)
     * @see net.mosstest.scripting.IMapGenerator#registerOre(net.mosstest.scripting.MapNode, double, double, double, int, java.lang.Object[])
     */
    @Override
    public void registerOre(MapNode oreNode, double minDepth,
                            double rarity, double clumping, int seed, Object... params) {
        // TODO Auto-generated method stub

    }

}