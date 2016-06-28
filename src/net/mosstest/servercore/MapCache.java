package net.mosstest.servercore;

import com.google.common.cache.*;
import net.mosstest.scripting.MapChunk;
import net.mosstest.scripting.MapGenerators;
import net.mosstest.scripting.Position;
import org.apache.log4j.Logger;


import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


// TODO: Auto-generated Javadoc

/**
 * The Class MapCache.
 */
public class MapCache {
    private static final Logger logger = Logger.getLogger(MapCache.class);
    private final LoadingCache<Position, MapChunk> chunkCache;
    private final Map<Position, MapChunk> chunkCacheAsMap;

    {
        chunkCache = CacheBuilder.newBuilder()
                .concurrencyLevel(4)
                .softValues()
                .expireAfterAccess(EngineSettings.getInt("chunkCacheTTL", 240), TimeUnit.SECONDS)
                .removalListener(new RemovalListener())
                .build(new PositionMapChunkCacheLoader());
        chunkCacheAsMap = chunkCache.asMap();
    }


    /**
     * The db.
     */
    private MapDatabase db;

    /**
     * Gets the chunk.
     *
     * @param pos the pos
     * @return the chunk
     * @throws MapGeneratorException the map generator exception
     */
    public MapChunk getChunk(Position pos) throws MapGeneratorException {
        // we want loading implicitly (which will generate it)
        MapChunk chk = null;
        try {
            chk = chunkCache.get(pos);
        } catch (ExecutionException e) {
            logger.error(MessageFormat.format(Messages.getString("CHUNK_LDR_EXECUTION_EXCEPTION"), e.getMessage()));
        }
        if (chk == null) {
            // loading from DB is still done if no chunk exists after an async load
            chk = db.getChunk(pos);
            // fill in, in case it was stored as compressed
            MapGenerators.getDefaultMapgen().fillInChunk(chk.getNodes(), pos);
            if (chk == null) {
                // if still not loaded, generate
                chk = MapGenerators.getDefaultMapgen().generateChunk(pos);
            }
        }
        return chk;

    }


    /**
     * Gets the chunk fail fast.
     *
     * @param pos the pos
     * @return the chunk fail fast
     */
    public MapChunk getChunkFailFast(Position pos) {
        // we don't want loading from DB or generating
        return chunkCacheAsMap.get(pos);
    }


    /**
     * Sets the chunk, updating cache and database.
     *
     * @param pos   The position in question
     * @param chunk the chunk to be stored
     */
    public void setChunk(Position pos, MapChunk chunk) {
        // plain and simple, we just store it
        chunk.compact();
        db.addMapChunk(pos, chunk);
        chunkCache.put(pos, chunk);
    }

    /**
     * Instantiates a new node cache.
     *
     * @param db the db
     */
    public MapCache(MapDatabase db) {
        this.db = db;
    }

    /**
     * Gets the chunk, or null if it is not generated.
     *
     * @param pos The position in question
     * @return the chunk, or null if it does not exist on disk.
     */
    public MapChunk getChunkNoGenerate(Position pos) throws MapGeneratorException {
        // we don't want loading implicitly (which will generate it)
        MapChunk chk = chunkCacheAsMap.get(pos);
        if (chk == null) {
            // loading from DB is still done
            chk = db.getChunk(pos);
            if (chk == null) return null; // not found in DB
            // fill in, in case it was stored as compressed. This still requires mapgen use as chunks are not guaranteed to be stored fully.
            MapGenerators.getDefaultMapgen().fillInChunk(chk.getNodes(), pos);
        }
        return chk;
    }

    private class RemovalListener implements com.google.common.cache.RemovalListener<Position, MapChunk> {

        @Override
        public void onRemoval(RemovalNotification<Position, MapChunk> notification) {
            switch (notification.getCause()) {
                case COLLECTED:
                    logger.warn(MessageFormat.format(Messages.getString("GC_EVICT"), notification.getKey().toString()));
                    break;
                case EXPIRED:
                    logger.info(MessageFormat.format(Messages.getString("UNCACHE_EXPIRE"), notification.getKey().toString()));
                case SIZE:
                    logger.warn(MessageFormat.format(Messages.getString("EVICT_SIZE"), notification.getKey().toString()));
            }

        }
    }

    private class PositionMapChunkCacheLoader extends CacheLoader<Position, MapChunk> {
        @Override
        public MapChunk load(Position position) throws Exception {
            logger.info(MessageFormat.format(Messages.getString("RELOADING_INTO_CACHE"), position.toString()));
            MapChunk chk = MapCache.this.db.getChunk(position);
            if (chk == null) {
                chk = MapGenerators.getDefaultMapgen().generateChunk(position);
                MapCache.this.db.addMapChunk(position, chk);
            }
            return chk;
        }
    }
}
