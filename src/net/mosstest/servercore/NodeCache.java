package net.mosstest.servercore;

import net.mosstest.scripting.MapChunk;
import net.mosstest.scripting.Position;

import java.lang.ref.SoftReference;
import java.util.HashMap;


// TODO: Auto-generated Javadoc
/**
 * The Class NodeCache.
 */
public class NodeCache {

	/** The chunks. */
    private final HashMap<Position, SoftReference<MapChunk>> chunks = new HashMap<>();

    /**
     * The db.
     */
    private MapDatabase db ;
	
	/**
	 * Gets the chunk.
	 *
	 * @param pos the pos
	 * @return the chunk
	 * @throws MapGeneratorException the map generator exception
	 */
	public MapChunk getChunk(Position pos) throws MapGeneratorException {
		synchronized (this.chunks) {
			synchronized (MapDatabase.class) {

                SoftReference<MapChunk> ref = this.chunks.get(pos);
                MapChunk ourChunk = (ref == null) ? null : ref.get();
                if (ourChunk == null) {
                    ourChunk = this.db.getChunk(pos);
					this.chunks.put(pos, new SoftReference<>(ourChunk));
				}
				return ourChunk;
			}
		}
	}
	
	
	/**
	 * Gets the chunk fail fast.
	 *
	 * @param pos the pos
	 * @return the chunk fail fast
	 */
	public MapChunk getChunkFailFast(Position pos) {
		SoftReference<MapChunk> ref = this.chunks.get(pos);
		return (ref==null?null:ref.get());
	}
	//public void requestMapChunk
	
	//public MapChunk getChunkClient(Position pos) {
	//	synchronized (this.chunks) {
	//
	//		MapChunk ourChunk = null;
	//		ourChunk = this.chunks.get(pos).get();
	//		if (ourChunk == null)
	//			ClientManager.getApplicationLevelNetworkingManager().sendChunkRequest(pos);
	//		return ourChunk;
	//	}
	//}

	/**
	 * Sets the chunk client.
	 *
	 * @param pos the pos
	 * @param chunk the chunk
	 */
	public void setChunkClient(Position pos, MapChunk chunk) {
		synchronized (this.chunks) {
			synchronized (MapDatabase.class) {
				this.chunks.put(pos, new SoftReference<>(chunk));
			}
		}

	}
	
	
	

	/**
	 * Sets the chunk.
	 *
	 * @param pos the pos
	 * @param chunk the chunk
	 */
	public void setChunk(Position pos, MapChunk chunk) {
		synchronized (this.chunks) {
			synchronized (MapDatabase.class) {
				this.chunks.put(pos, new SoftReference<>(chunk));
				this.db.addMapChunk(pos, chunk);
			}
		}

	}
	
	/**
	 * Instantiates a new node cache.
	 *
	 * @param db the db
	 */
	public NodeCache(MapDatabase db) {
		this.db =db;
	}

	/**
	 * Gets the chunk no generate.
	 *
	 * @param chunk the chunk
	 * @return the chunk no generate
	 */
	public MapChunk getChunkNoGenerate(Position chunk) {
		synchronized (this.chunks) {
			 return this.chunks.get(chunk).get();
		}
	}

}
