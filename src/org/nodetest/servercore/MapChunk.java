package org.nodetest.servercore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MapChunk {
	Position pos;
	short[][][] lightNodes = new short[16][16][16];
	byte[] light;
	byte[] heavy;
	boolean modified;
	boolean compressed;

	public MapChunk(Position pos, byte[] light) throws IOException {
		this.pos = pos;
		this.light = light;
		DataInputStream lightStreamIn = new DataInputStream(
				new ByteArrayInputStream(light));
		int flags = lightStreamIn.readUnsignedByte();

		/*
		 * flags byte: 1=has heavies 2=has been modified 4=run-length diff
		 * compression (not implemented yet) 8...=reserved
		 */
		if (((flags & 0x01)) != 0)
			this.heavy = MapDatabase.getHeavy(pos);
		this.modified = (((flags & 0x02)) != 0);
		this.compressed = (((flags & 0x04)) != 0);
		if (compressed)
			throw new NotImplementedException();
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				for (int z = 0; z < 16; z++) {
					lightNodes[x][y][z] = lightStreamIn.readShort();
				}
			}
		}

	}

	public short getNodeId(byte x, byte y, byte z) {
		return lightNodes[x][y][z];
	}

}
