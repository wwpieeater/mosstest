package org.nodetest.servercore;

import java.util.Map;

import org.apache.commons.collections4.map.LRUMap;

public class NodeCache extends LRUMap<Position, MapChunk> {

	private static final long serialVersionUID = 8915103950141113423L;

	@Override
	protected boolean removeLRU(LinkEntry<Position, MapChunk> entry) {
		MapDatabase.addMapChunk(entry.getKey(), entry.getValue());
		return true;
	}

	private static boolean init;
	private static NodeCache chunks = new NodeCache(1024, true);

	public static MapChunk getChunk(Position pos) {
		synchronized (chunks) {
			synchronized (MapDatabase.class) {
				MapChunk ourChunk = null;
				ourChunk = chunks.get(pos);
				if (ourChunk == null)
					ourChunk = MapDatabase.getChunk(pos);

				return ourChunk;
			}
		}
	}
	
	public static MapChunk getChunkClient(Position pos) {
		synchronized (chunks) {
			
				MapChunk ourChunk = null;
				ourChunk = chunks.get(pos);
				if (ourChunk == null)
					ClientManager.getNetworkingManager().sendChunkRequest(pos);

				return ourChunk;
			
		}
	}

	public static void setChunk(Position pos, MapChunk chunk) {
		synchronized (chunks) {
			synchronized (MapDatabase.class) {
				chunks.put(pos, chunk);
			}
		}

	}

	private NodeCache() {
		super();
	}

	private NodeCache(int maxSize, boolean scanUntilRemovable) {
		super(maxSize, scanUntilRemovable);
	}

	private NodeCache(int maxSize, float loadFactor, boolean scanUntilRemovable) {
		super(maxSize, loadFactor, scanUntilRemovable);
	}

	private NodeCache(int maxSize, float loadFactor) {
		super(maxSize, loadFactor);
	}

	private NodeCache(int maxSize) {
		super(maxSize);
	}

	private NodeCache(Map<Position, MapChunk> map, boolean scanUntilRemovable) {
		super(map, scanUntilRemovable);
	}

	private NodeCache(Map<Position, MapChunk> map) {
		super(map);
		
	}
}
