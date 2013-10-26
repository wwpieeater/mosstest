package net.mosstest.servercore;

import java.util.concurrent.ArrayBlockingQueue;

import net.mosstest.scripting.MapChunk;
import net.mosstest.scripting.Position;

public class LocalRenderPreparator implements IRenderPreparator {
	public class ChunkLookupRunnable implements Runnable {

		@Override
		public void run() {
			while(LocalRenderPreparator.this.run) {
				try {
					Position requested = LocalRenderPreparator.this.chunkRequests.take();
					MapChunk chk = LocalRenderPreparator.this.nc.getChunk(requested);
					chk.pos = requested;
					LocalRenderPreparator.this.rp.renderEventQueue.put(new MossRenderChunkEvent(chk));
				} catch (InterruptedException e) {
					//pass
				} catch (MapGeneratorException e) {
					System.out.print("The map generator has thrown an exception.");
				}
			}
		}

	}

	private RenderProcessor rp;
	private NodeCache nc;
	private volatile boolean run = true;
	public ArrayBlockingQueue<Position> chunkRequests = new ArrayBlockingQueue<>(
			1024);
	//private HashMap<Position, Position> outstandingChunks = new HashMap<>();
	private NodeCache cache;
	private Thread lookupThread = new Thread(new ChunkLookupRunnable());
	
	@Override
	public MapChunk requestChunk(Position pos) throws MapGeneratorException, InterruptedException {
		MapChunk chk = this.cache.getChunkFailFast(pos);
		if (chk == null) {
			this.chunkRequests.put(pos);
		}
		return chk;
	}

	
	@Override
	public void recvOutstandingChunk(Position pos, MapChunk chk) {
		//pass unused
	}

	public LocalRenderPreparator(RenderProcessor rp, NodeCache nc) {
		this.rp = rp;
		this.nc = nc;
		lookupThread.start();
	}
	@Override
	public void shutdown() {
		this.run = false;
	}

	// TODO: Handle player movement, other server->client events affecting
	// rendering
	
}
