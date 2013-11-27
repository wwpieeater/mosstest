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
					System.out.println(Messages.getString("LocalRenderPreparator.MSG_REQUESTED") + requested.x + "," //$NON-NLS-1$ //$NON-NLS-2$
							+ requested.y + "," + requested.z); //$NON-NLS-1$
					MapChunk chk = LocalRenderPreparator.this.nc
							.getChunk(requested);
					chk.pos = requested;
					LocalRenderPreparator.this.rend.renderEventQueue
							.put(new MossRenderChunkEvent(chk));
				} catch (InterruptedException e) {
					// pass
				} catch (MapGeneratorException e) {
					System.out
							.print(Messages.getString("LocalRenderPreparator.MG_EXCEPT")); //$NON-NLS-1$
				}
			}
		}

	}

	private RenderProcessor rend;
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
			System.out.println(chk == null ? "null chunk failfast" : chk //$NON-NLS-1$
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
		this.rend = rp;
		this.nc = nc;
	}

	@Override
	public void shutdown() {
		this.run = false;
	}

	@Override
	public void recvOutstandingChunk(Position pos, MapChunk chk) {
		// pass
	}

	@Override
	public void start() {
		System.out.println(Messages.getString("LocalRenderPreparator.START_MSG")); //$NON-NLS-1$
		this.lookupThread.start();

	}

	@Override
	public void setRenderProcessor(RenderProcessor rend) {
		this.rend = rend;
	}

	// TODO: Handle player movement, other server->client events affecting
	// rendering

}
