package net.mosstest.servercore;

import net.mosstest.scripting.MapChunk;
import net.mosstest.scripting.Position;

public class MossRenderChunkEvent extends MossRenderEvent {
	private MapChunk myChunk;
	public MossRenderChunkEvent (MapChunk ch) {
		myChunk = ch;
	}
	
	public MapChunk getChk () {
		return myChunk;
	}
	public Position getPos () {
		return myChunk.pos;
	}
	
	/*public int getNodeId (byte x, byte y, byte z) {
		return myChunk.getNodeId(x, y, z);
	}*/
}
 