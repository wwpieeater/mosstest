package net.mosstest.servercore;

public class MossRenderChunkEvent extends MossRenderEvent {
	private MapChunk myChunk;
	private int x;
	private int y;
	private int z;
	public MossRenderChunkEvent (MapChunk ch) {
		myChunk = ch;
		x = ch.pos.x;
		y = ch.pos.y;
		z = ch.pos.z;
	}
	
	public int getX () {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getZ() {
		return z;
	}
	
	public int getNodeId (byte x, byte y, byte z) {
		return myChunk.getNodeId(x, y, z);
	}
}
