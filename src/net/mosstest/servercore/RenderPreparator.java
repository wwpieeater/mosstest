package net.mosstest.servercore;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class RenderPreparator {
	private RenderProcessor rp;
	public ArrayBlockingQueue<Position> chunkRequests = new ArrayBlockingQueue<>(
			1024);
	private HashMap<Position, Position> outstandingChunks = new HashMap<>();
	private NodeCache cache;

	public MapChunk requestChunk(Position pos) throws MapGeneratorException {
		MapChunk chk = this.cache.getChunk(pos);
		if (chk == null) {
			this.outstandingChunks.put(pos, pos);
		}
		return chk;
	}

	public void recvOutstandingChunk(Position pos, MapChunk chk) {
		if (this.outstandingChunks.get(pos) != null)
			this.rp.renderEventQueue.offer(new MossRenderEvent()); // TODO
																// based
																// on
																// params
	}

	// TODO: Handle player movement, other server->client events affecting
	// rendering

	public RenderPreparator(NodeCache cache) {
		this.cache = cache;
		this.rp = RenderProcessor.init();
	}
}
