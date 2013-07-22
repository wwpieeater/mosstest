package org.nodetest.servercore;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections4.map.AbstractLinkedMap;
import org.apache.commons.collections4.map.LRUMap;

public class NodeCache extends LRUMap<Position, MapChunk> {
	@Override
	protected boolean removeLRU(LinkEntry<Position, MapChunk> entry) {
		MapDatabase.addMapChunk(entry.getKey(), entry.getValue());
		return true;
	}

	private static boolean init;
	private static NodeCache chunks = new NodeCache(1024, true);

	public static MapChunk getChunk(Position pos) {
		MapChunk ourChunk = null;
		ourChunk = chunks.get(pos);
		if (ourChunk == null)
			ourChunk = MapDatabase.getChunk(pos);

		return ourChunk;

	}

	public static void setChunk(Position pos, MapChunk chunk) {
		chunks.put(pos, chunk);

	}

	public NodeCache() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NodeCache(int maxSize, boolean scanUntilRemovable) {
		super(maxSize, scanUntilRemovable);
		// TODO Auto-generated constructor stub
	}

	public NodeCache(int maxSize, float loadFactor, boolean scanUntilRemovable) {
		super(maxSize, loadFactor, scanUntilRemovable);
		// TODO Auto-generated constructor stub
	}

	public NodeCache(int maxSize, float loadFactor) {
		super(maxSize, loadFactor);
		// TODO Auto-generated constructor stub
	}

	public NodeCache(int maxSize) {
		super(maxSize);
		// TODO Auto-generated constructor stub
	}

	public NodeCache(Map<Position, MapChunk> map, boolean scanUntilRemovable) {
		super(map, scanUntilRemovable);
		// TODO Auto-generated constructor stub
	}

	public NodeCache(Map<Position, MapChunk> map) {
		super(map);
		// TODO Auto-generated constructor stub
	}
}
