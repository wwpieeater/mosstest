package net.mosstest.scripting;

import net.mosstest.servercore.*;
import net.mosstest.servercore.Messages;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Arrays;

// TODO: Auto-generated Javadoc

/**
 * The Class MapChunk.
 */
public class MapChunk implements IMapChunk {
    public static final Logger logger = Logger.getLogger(MapChunk.class);
    public static final int IS_CHANGED_MASK = 16384;
    public static final int UNSIGNED_IDENTITY_MASK = 0b0011111111111111;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapChunk mapChunk = (MapChunk) o;

        if (!pos.equals(mapChunk.pos)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return pos.hashCode();
    }

    /**
     * The pos.
     */
    public Position pos;

    /**
     * The light nodes.
     */
    int[][][] lightNodes = new int[CHUNK_DIMENSION][CHUNK_DIMENSION][CHUNK_DIMENSION];


    boolean compressed;

    transient MapDatabase db;

    static final int MAPCHUNK_SERIALIZATION_VERSION = 2;


    /**
     * Instantiates a new map chunk.
     *
     * @param pos   the pos
     * @param light the light
     * @param db    the db
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public MapChunk(Position pos, byte[] light, MapDatabase db)
            throws IOException {
        this.db = db;
        this.pos = pos;
        Arrays.copyOf(light, light.length);
        try (DataInputStream lightStreamIn = new DataInputStream(
                new ByteArrayInputStream(light))) {
            int flags = lightStreamIn.readUnsignedShort();
            int version = lightStreamIn.readUnsignedShort();

            if (version > MAPCHUNK_SERIALIZATION_VERSION)
                ExceptionHandler.registerException(new MossWorldLoadException(
                        Messages.getString("MapChunk.BAD_SER_VER"))); //$NON-NLS-1$
        /*
         * flags short: 1=has heavies 2=none yet 4=run-length diff compression
		 * (not implemented yet) 8...=reserved
		 */
            if (((flags & 0x01)) != 0) {
                this.loadHeavy(db.getHeavy(pos));
            }
            this.compressed = (((flags & 0x04)) != 0);
            if (this.compressed) {
                int cursor = 0;
                int[] lightTmp = new int[CHUNK_DIMENSION * CHUNK_DIMENSION * CHUNK_DIMENSION];
                while (lightStreamIn.available() > 0) {
                    int curShort = lightStreamIn.readUnsignedShort();
                    if ((curShort & IS_CHANGED_MASK) != 0) {
                        lightTmp[cursor] = curShort;
                        cursor++;
                    } else {
                        if ((curShort & IS_CHANGED_MASK) != 0) {
                            for (int i = 0; i < (curShort & UNSIGNED_IDENTITY_MASK); i++) {
                                lightTmp[cursor] = 0;
                                cursor++;
                            }
                        }
                    }
                }
                try {
                    MapGenerators.getDefaultMapgen().fillInChunk(this.lightNodes,
                            pos);
                } catch (MapGeneratorException e) {
                    // pass, we'll deal with a bad chunk later in the pipeline
                }
                // throw new NotImplementedException();
            } else {
                for (int x = 0; x < CHUNK_DIMENSION; x++) {
                    for (int y = 0; y < CHUNK_DIMENSION; y++) {
                        for (int z = 0; z < CHUNK_DIMENSION; z++) {
                            this.lightNodes[x][y][z] = lightStreamIn.readShort();
                        }
                    }
                }
            }
        }

    }

    private void loadHeavy(byte[] heavy) {

    }


    /**
     * Instantiates a new map chunk.
     *
     * @param pos2  the pos2
     * @param nodes the nodes
     */
    public MapChunk(Position pos2, int[][][] nodes) {
        this.pos = pos2;
        this.lightNodes = Arrays.copyOf(nodes, nodes.length);

    }

    /**
     * Gets the node id.
     *
     * @param x the x
     * @param y the y
     * @param z the z
     * @return the node id
     */
    @Override
    public int getNodeId(byte x, byte y, byte z) {
        return this.lightNodes[x][y][z];
    }

    /**
     * Updates the chunk to set a node. The chunk is not written to the database
     * or committed across the network.
     *
     * @param x    the x
     * @param y    the y
     * @param z    the z
     * @param node the node
     */
    @Override
    public void setNode(byte x, byte y, byte z, short node) {
        this.lightNodes[x][y][z] = node;
    }

    @Override
    public int[][][] getNodes() {
        return this.lightNodes;
    }

    @Override
    public byte[] writeLight(boolean compressed) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try (DataOutputStream dos = new DataOutputStream(bos)) {
            dos.writeShort(0);
            dos.writeShort(MAPCHUNK_SERIALIZATION_VERSION);
            for (int[][] nodelvl : this.lightNodes) {
                for (int[] nodelvl2 : nodelvl) {
                    for (int node : nodelvl2) {
                        dos.writeShort(node);
                    }
                }
            }
            dos.flush();
            bos.flush();
        } catch (IOException e) {
            // should never happen
            logger.warn("IOException writing light chunk data");
        }

        return bos.toByteArray();
    }

    @Override
    public void compact() {
        // noop in this version. Later versions may compact.
    }
}