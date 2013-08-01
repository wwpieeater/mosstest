package org.nodetest.servercore;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class RenderPreparator {

	private static ArrayBlockingQueue<Position> chunkRequests=new ArrayBlockingQueue<>(1024);
	private static HashMap<Position, Position> outstandingChunks=new HashMap<>();
	
	public static MapChunk requestChunk(Position pos) {
		MapChunk chk=NodeCache.getChunk(pos);
		if(chk==null) {
			outstandingChunks.put(pos, pos);
		}
		return chk;
	}
	
	public static void recvOutstandingChunk(Position pos, MapChunk chk) {
		if(outstandingChunks.get(pos) != null) RenderProcessor.renderEventQueue.offer(new MossRenderEvent()); //TODO based on params
	}
	//TODO: Handle player movement, other server->client events affecting rendering

}
