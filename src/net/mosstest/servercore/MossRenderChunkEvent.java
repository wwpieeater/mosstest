package net.mosstest.servercore;

import net.mosstest.scripting.MapChunk;
import net.mosstest.scripting.Position;

// TODO: Auto-generated Javadoc
/**
 * The Class MossRenderChunkEvent.
 */
public class MossRenderChunkEvent extends MossRenderEvent {
	
	/** The my chunk. */
	private MapChunk myChunk;
	
	/**
	 * Instantiates a new moss render chunk event.
	 *
	 * @param ch the ch
	 */
	public MossRenderChunkEvent (MapChunk ch) {
		myChunk = ch;
	}
	
	/**
	 * Gets the chk.
	 *
	 * @return the chk
	 */
	public MapChunk getChk () {
		return myChunk;
	}
	
	/**
	 * Gets the pos.
	 *
	 * @return the pos
	 */
	public Position getPos () {
		return myChunk.pos;
	}
	
	/*public int getNodeId (byte x, byte y, byte z) {
		return myChunk.getNodeId(x, y, z);
	}*/
}
