package org.nodetest.servercore;


public class MapChunkPacked {

	long x;
	long y;
	long z;
	String chunkLightStorage;
	String heavy;
	
	public MapChunkPacked(long x, long y, long z, String chunkLightStorage,
			String heavy) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.chunkLightStorage = chunkLightStorage;
		this.heavy = heavy;
	}

	public MapChunkPacked() {
		// TODO Auto-generated constructor stub
	}

	public MapChunk unpack(){
		return new MapChunk(this.x, this.y, this.z, this.chunkLightStorage, this.heavy);
	}
	
}
