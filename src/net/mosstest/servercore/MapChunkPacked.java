package net.mosstest.servercore;

@Deprecated
public class MapChunkPacked {

	Position pos;
	byte[] light;
	byte[] heavy;
	
	
	public MapChunkPacked(Position pos, byte[] chunk) {
		this.pos=pos;
		this.light=chunk;
	}

	
}
