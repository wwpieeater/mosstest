package net.mosstest.servercore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

public class MapChunk {
	Position pos;
	int[][][] lightNodes = new int[16][16][16];
	private byte[] light;
	private byte[] heavy;
	boolean[][][] modified = new boolean[16][16][16];
	boolean compressed;
	transient MapDatabase db;
	static final int MAPCHUNK_SERIALIZATION_VERSION = 1;

	/*
	 * Compressed format: Short values act as follows: val&32768: 0 means take
	 * this node literally, 1 means is repeating
	 * 
	 * val&16384: 0: This is repeating an unchanged node. Next short shall be
	 * the start of a new node/run 1: This is repeating a *changed* node. Next
	 * short value identifies node type
	 */
	public MapChunk(Position pos, byte[] light, MapDatabase db)
			throws IOException {
		this.db = db;
		this.pos = pos;
		this.light = Arrays.copyOf(light, light.length);
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
			this.heavy = db.getHeavy(pos);
			loadHeavies();
		}
		this.compressed = (((flags & 0x04)) != 0);
		if (compressed) {
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
			MapGenerator.fillInChunk(lightNodes, pos);
			// throw new NotImplementedException();
		} else {
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y < 16; y++) {
					for (int z = 0; z < 16; z++) {
						lightNodes[x][y][z] = lightStreamIn.readShort();
					}
				}
			}
		}

	}

	private void loadHeavies() {
		// TODO Heavies not here yet

	}

	public MapChunk(Position pos2, int[][][] nodes, boolean[][][] modified) {
		this.pos = pos2;
		lightNodes = Arrays.copyOf(nodes, nodes.length);
		this.modified = Arrays.copyOf(modified, modified.length);
	}

	public int getNodeId(byte x, byte y, byte z) {
		return lightNodes[x][y][z];
	}

	public byte[] writeLight(boolean compressed) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try (DataOutputStream dos = new DataOutputStream(bos);) {
			dos.writeShort(13);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bos.toByteArray();
	}

}
