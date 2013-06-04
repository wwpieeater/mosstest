package org.nodetest.servercore;


public class MapChunkPacked {

	private long chunkId;
	private String chunkLightStorage;
	private boolean heavy;
	public String getChunkLightStorage() {
		return chunkLightStorage;
	}
	public void setChunkLightStorage(String chunkLightStorage) {
		this.chunkLightStorage = chunkLightStorage;
	}
	public long getChunkId() {
		return chunkId;
	}
	public void setChunkId(long chunkId) {
		this.chunkId = chunkId;
	}
	public boolean isHeavy() {
		return heavy;
	}
	public void setHeavy(boolean hasHeavies) {
		this.heavy = hasHeavies;
	}
	public MapChunk unpack(){
		return new MapChunk(this.chunkId, this.chunkLightStorage, this.heavy);
	}
	
}
