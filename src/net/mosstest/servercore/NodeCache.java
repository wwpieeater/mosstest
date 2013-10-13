package net.mosstest.servercore;

import java.lang.ref.SoftReference;
import java.util.HashMap;


public class NodeCache {

	private HashMap<Position, SoftReference<MapChunk>> chunks = new HashMap<>();
	private MapDatabase db ;
	public MapChunk getChunk(Position pos) throws MapGeneratorException {
		synchronized (this.chunks) {
			synchronized (MapDatabase.class) {
				MapChunk ourChunk = null;
				ourChunk = this.chunks.get(pos).get();
				if (ourChunk == null) {
					ourChunk = this.db.getChunk(pos);
					this.chunks.put(pos, new SoftReference<>(ourChunk));
				}
				return ourChunk;
			}
		}
	}

	public MapChunk getChunkClient(Position pos) {
		synchronized (this.chunks) {

			MapChunk ourChunk = null;
			ourChunk = this.chunks.get(pos).get();
			if (ourChunk == null)
				ClientManager.getApplicationLevelNetworkingManager().sendChunkRequest(pos);

			return ourChunk;

		}
	}

	public void setChunkClient(Position pos, MapChunk chunk) {
		synchronized (this.chunks) {
			synchronized (MapDatabase.class) {
				this.chunks.put(pos, new SoftReference<>(chunk));
			}
		}

	}
	
	
	

	public void setChunk(Position pos, MapChunk chunk) {
		synchronized (this.chunks) {
			synchronized (MapDatabase.class) {
				this.chunks.put(pos, new SoftReference<>(chunk));
				this.db.addMapChunk(pos, chunk);
			}
		}

	}
	public NodeCache(MapDatabase db) {
		this.db =db;
	}

	public MapChunk getChunkNoGenerate(Position chunk) {
		synchronized (this.chunks) {
			 return this.chunks.get(chunk).get();
		}
	}

}
