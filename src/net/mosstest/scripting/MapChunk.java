package net.mosstest.scripting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import net.mosstest.servercore.ExceptionHandler;
import net.mosstest.servercore.MapDatabase;
import net.mosstest.servercore.MapGeneratorException;
import net.mosstest.servercore.Messages;
import net.mosstest.servercore.MossWorldLoadException;

// TODO: Auto-generated Javadoc
/**
 * The Class MapChunk.
 */
public class MapChunk {
	
	/** The pos. */
	public Position pos;
	
	/** The light nodes. */
	int[][][] lightNodes = new int[16][16][16];
	
	/** The modified. */
	boolean[][][] modified = new boolean[16][16][16];
	
	/** The compressed. */
	boolean compressed;
	
	/** The db. */
	transient MapDatabase db;
	
	/** The Constant MAPCHUNK_SERIALIZATION_VERSION. */
	static final int MAPCHUNK_SERIALIZATION_VERSION = 1;

	/*
	 * Compressed format: Short values act as follows: val&32768: 0 means take
	 * this node literally, 1 means is repeating
	 * 
	 * val&16384: 0: This is repeating an unchanged node. Next short shall be
	 * the start of a new node/run 1: This is repeating a *changed* node. Next
	 * short value identifies node type
	 */
	/**
	 * Instantiates a new map chunk.
	 *
	 * @param pos the pos
	 * @param light the light
	 * @param db the db
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public MapChunk(Position pos, byte[] light, MapDatabase db)
			throws IOException {
		this.db = db;
		this.pos = pos;
		Arrays.copyOf(light, light.length);
		DataInputStream lightStreamIn = new DataInputStream(
				new ByteArrayInputStream(light));
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
			db.getHeavy(pos);
			loadHeavies();
		}
		this.compressed = (((flags & 0x04)) != 0);
		if (this.compressed) {
			int cursor = 0;
			int[] lightTmp = new int[16 * 16 * 16];
			while (lightStreamIn.available() > 0) {
				int curShort = lightStreamIn.readUnsignedShort();
				if ((curShort & 16384) != 0) {
					lightTmp[cursor] = curShort;
					cursor++;
				} else {
					if ((curShort & 16384) != 0) {
						for (int i = 0; i < (curShort & 0b0011111111111111); i++) {
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
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 16; y++) {
					for (int z = 0; z < 16; z++) {
						this.lightNodes[x][y][z] = lightStreamIn.readShort();
					}
				}
			}
		}

	}

	/**
	 * Load heavies.
	 */
	private void loadHeavies() {
		// TODO Heavies not here yet

	}

	/**
	 * Instantiates a new map chunk.
	 *
	 * @param pos2 the pos2
	 * @param nodes the nodes
	 * @param modified the modified
	 */
	public MapChunk(Position pos2, int[][][] nodes, boolean[][][] modified) {
		this.pos = pos2;
		this.lightNodes = Arrays.copyOf(nodes, nodes.length);
		this.modified = modified==null?new boolean[16][16][16]:Arrays.copyOf(modified, modified.length);
	}

	/**
	 * Gets the node id.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @return the node id
	 */
	public int getNodeId(byte x, byte y, byte z) {
		return this.lightNodes[x][y][z];
	}

	/**
	 * Updates the chunk to set a node. The chunk is not written to the database
	 * or committed across the network.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @param node the node
	 */
	public void setNode(byte x, byte y, byte z, short node) {
		this.lightNodes[x][y][z] = node;
	}

	/**
	 * Write light.
	 *
	 * @param compressed the compressed
	 * @return the byte[]
	 */
	public byte[] writeLight(boolean compressed) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try (DataOutputStream dos = new DataOutputStream(bos);) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bos.toByteArray();
	}

}