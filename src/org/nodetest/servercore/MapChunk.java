package org.nodetest.servercore;


public class MapChunk {
	Position pos;
	byte[] light;
	byte[] heavy;
	boolean modified;

	public MapChunk(Position pos, byte[] light) {
		this.pos = pos;
		this.light = light;
		byte flags = light[0];
	
		/*
		 * flags byte: 
		 * 1=has heavies 
		 * 2=has been modified
		 * 4=diff compression (not implemented yet)
		 * 8...=reserved
		 * 
		 */
		if (((flags & 0x01)) != 0)
			this.heavy = MapDatabase.getHeavy(pos);
		this.modified = (((flags & 0x02)) != 0);

	}
	
	public short getNodeId(byte x, byte y, byte z){
		return light[2*(256*x+16*y+z)+5];
	}

}
