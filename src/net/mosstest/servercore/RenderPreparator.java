package net.mosstest.servercore;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class RenderPreparator {
	private RenderProcessor rp;
	private ArrayBlockingQueue<Position> chunkRequests = new ArrayBlockingQueue<>(
			1024);
	private HashMap<Position, Position> outstandingChunks = new HashMap<>();
	private NodeCache cache;

	public MapChunk requestChunk(Position pos) {
		MapChunk chk = cache.getChunk(pos);
		if (chk == null) {
			outstandingChunks.put(pos, pos);
		}
		return chk;
	}

	public void recvOutstandingChunk(Position pos, MapChunk chk) {
		if (outstandingChunks.get(pos) != null)
			rp.renderEventQueue.offer(new MossRenderEvent()); // TODO
																// based
																// on
																// params
	}

	// TODO: Handle player movement, other server->client events affecting
	// rendering

	public RenderPreparator(NodeCache cache) {
		this.cache = cache;
		rp = RenderProcessor.init();
	}
}
