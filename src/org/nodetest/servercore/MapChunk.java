package org.nodetest.servercore;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class MapChunk {
	Position pos;
	int[][][] lightNodes = new int[16][16][16];
	byte[] light;
	byte[] heavy;
	boolean[][][] modified = new boolean[16][16][16];
	boolean compressed;

	static final int MAPCHUNK_SERIALIZATION_VERSION = 1;

	/*
	 * Compressed format: Short values act as follows: val&32768: 0 means take
	 * this node literally, 1 means is repeating
	 * 
	 * val&16384: 0: This is repeating an unchanged node. Next short shall be
	 * the start of a new node/run 1: This is repeating a *changed* node. Next
	 * short value identifies node type
	 */
	public MapChunk(Position pos, byte[] light) throws IOException {
		this.pos = pos;
		this.light = light;
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
		if (((flags & 0x01)) != 0)
			this.heavy = MapDatabase.getHeavy(pos);
		this.compressed = (((flags & 0x04)) != 0);
		if (compressed) {
			int cursor = 0;
			int[] lightTmp = new int[16 * 16 * 16];
			while (lightStreamIn.available() > 0) {
				int curShort = lightStreamIn.readUnsignedShort();
				if ((curShort & 16384) != 0) {
					lightTmp[cursor] = curShort;
					cursor++;
				}
				else{
					if((curShort&16384)!=0){
						for(int i=0; i<(curShort&0b0011111111111111); i++){
							lightTmp[cursor]=0;
							cursor++;
						}
					}
				}
			}
			MapGenerator.fillInChunk(lightNodes, pos);
			// throw new NotImplementedException();
		}
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				for (int z = 0; z < 16; z++) {
					lightNodes[x][y][z] = lightStreamIn.readShort();
				}
			}
		}

	}

	public MapChunk(Position pos2, int[][][] nodes, boolean[][][] modified) {
		lightNodes = nodes;
		this.modified = modified;
	}

	public int getNodeId(byte x, byte y, byte z) {
		return lightNodes[x][y][z];
	}

}
