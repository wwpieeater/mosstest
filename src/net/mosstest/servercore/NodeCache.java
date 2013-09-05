package net.mosstest.servercore;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class NodeCache {

	private static final long serialVersionUID = 8915103950141113423L;
	private static HashMap<Position, SoftReference<MapChunk>> chunks = new HashMap<>();
	private static ReferenceQueue<MapChunk> rqueue = new ReferenceQueue<>();
	private static AtomicBoolean runFinalizeThread=new AtomicBoolean(true);
//	public static void init() {
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				while(runFinalizeThread.get()) {
//					Reference<? extends MapChunk> ref;
//					try {
//						ref = rqueue.remove();
//					
//					synchronized (chunks) {
//						synchronized (MapDatabase.class) {
//							MapDatabase.addMapChunk(ref.get().pos, ref.get());
//						}
//					}
//						ref.clear();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				
//			}
//			
//		}, "mapCacheGcHandler").run();
//	}
	public static MapChunk getChunk(Position pos) {
		synchronized (chunks) {
			synchronized (MapDatabase.class) {
				MapChunk ourChunk = null;
				ourChunk = chunks.get(pos).get();
				if (ourChunk == null) {
					ourChunk = MapDatabase.getChunk(pos);
					chunks.put(pos, new SoftReference<MapChunk>(ourChunk,
							rqueue));
				}
				return ourChunk;
			}
		}
	}

	public static MapChunk getChunkClient(Position pos) {
		synchronized (chunks) {

			MapChunk ourChunk = null;
			ourChunk = chunks.get(pos).get();
			if (ourChunk == null)
				ClientManager.getApplicationLevelNetworkingManager().sendChunkRequest(pos);

			return ourChunk;

		}
	}

	public static void setChunkClient(Position pos, MapChunk chunk) {
		synchronized (chunks) {
			synchronized (MapDatabase.class) {
				chunks.put(pos, new SoftReference<MapChunk>(chunk, rqueue));
			}
		}

	}
	
	
	public static void shutdown() {
		runFinalizeThread.set(false);
	}

	public static void setChunk(Position pos, MapChunk chunk) {
		synchronized (chunks) {
			synchronized (MapDatabase.class) {
				chunks.put(pos, new SoftReference<MapChunk>(chunk));
				MapDatabase.addMapChunk(pos, chunk);
			}
		}

	}

}
