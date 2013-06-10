package org.nodetest.servercore;

public class MapChunk {

	private int[][][] nodes=new int[16][16][16];
	int x,y,z;
	public MapChunk(int x, int y, int z, String chunkLightStorage,
			String heavy) {
	this.x=x;
	this.y=y;
	this.z=z;
	for(int i=0; i<4096; i++){
		int thisNode=chunkLightStorage.charAt(4*i)<<24+chunkLightStorage.charAt(4*i+1)<<16+chunkLightStorage.charAt(4*i+2)<<8+chunkLightStorage.charAt(4*i+3);
		nodes[(i/256)][(i/16)%16][i%16]=thisNode;
	}
	}

	public MapChunkPacked pack(){
		//TODO
		return new MapChunkPacked();
	};

}
