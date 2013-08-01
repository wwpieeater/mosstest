package net.mosstest.servercore;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.collections4.map.LRUMap;

public class NodeCache{

	private static final long serialVersionUID = 8915103950141113423L;

	

	private static boolean init;
	private static HashMap<Position, SoftReference<MapChunk>> chunks=new HashMap<>();

	public static MapChunk getChunk(Position pos) {
		synchronized (chunks) {
			synchronized (MapDatabase.class) {
				MapChunk ourChunk = null;
				ourChunk = chunks.get(pos).get();
				if (ourChunk == null)
					ourChunk = MapDatabase.getChunk(pos);

				return ourChunk;
			}
		}
	}
	
	public static MapChunk getChunkClient(Position pos) {
		synchronized (chunks) {
			
				MapChunk ourChunk = null;
				ourChunk = chunks.get(pos).get();
				if (ourChunk == null)
					ClientManager.getNetworkingManager().sendChunkRequest(pos);

				return ourChunk;
			
		}
	}

	public static void setChunkClient(Position pos, MapChunk chunk) {
		synchronized (chunks) {
			synchronized (MapDatabase.class) {
				chunks.put(pos, new SoftReference<MapChunk>(chunk));
			}
		}

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
