package net.mosstest.servercore;

import java.util.concurrent.ArrayBlockingQueue;

import net.mosstest.scripting.MapChunk;
import net.mosstest.scripting.Position;

public class LocalRenderPreparator implements IRenderPreparator {
	public class ChunkLookupRunnable implements Runnable {

		@Override
		public void run() {
			while (LocalRenderPreparator.this.run) {
				try {
					Position requested = LocalRenderPreparator.this.chunkRequests
							.take();
					System.out.println("REQUESTED " + requested.x + ","
							+ requested.y + "," + requested.z);
					MapChunk chk = LocalRenderPreparator.this.nc
							.getChunk(requested);
					chk.pos = requested;
					LocalRenderPreparator.this.rp.renderEventQueue
							.put(new MossRenderChunkEvent(chk));
				} catch (InterruptedException e) {
					// pass
				} catch (MapGeneratorException e) {
					System.out
							.print("The map generator has thrown an exception.");
				}
			}
		}

	}

	private RenderProcessor rp;
	private NodeCache nc;
	private volatile boolean run = true;
	public ArrayBlockingQueue<Position> chunkRequests = new ArrayBlockingQueue<>(
			1024);
	// private HashMap<Position, Position> outstandingChunks = new HashMap<>();

	private Thread lookupThread = new Thread(new ChunkLookupRunnable());

	@Override
	public MapChunk requestChunk(Position pos) throws MapGeneratorException,
			InterruptedException {
		try {
			MapChunk chk = this.nc.getChunkFailFast(pos);
			System.out.println(chk == null ? "null chunk failfast" : chk
					.toString());
			if (chk == null) {
				this.chunkRequests.put(pos);
			}
			return chk;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public LocalRenderPreparator(RenderProcessor rp, NodeCache nc) {
		this.rp = rp;
		this.nc = nc;
		this.lookupThread.start();
	}

	@Override
	public void shutdown() {
		this.run = false;
	}

	@Override
	public void recvOutstandingChunk(Position pos, MapChunk chk) {
		// TODO Auto-generated method stub

	}

	// TODO: Handle player movement, other server->client events affecting
	// rendering

}
