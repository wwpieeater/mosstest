package net.mosstest.servercore;

import com.google.common.cache.*;
import org.apache.log4j.Logger;
import org.iq80.leveldb.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * An in-memory map backed by a larger data-set stored in a levelDB byte[] to byte[] key-value store.
 *
 * @param <K> Keys to be stored
 * @param <V> Values to be stored
 */
public class LevelDBBackedMap<K extends AbstractByteArrayStorable<?>, V extends AbstractByteArrayStorable<M>, M> implements Map<K, V> {
    private final Class<V> valueClass;
    // TODO a map backed by a levelDB datastore

    private LoadingCache<K, V> memoryBackingCache;


    private DB diskBackingDatastore;
    private static final Logger logger = Logger.getLogger(LevelDBBackedMap.class);
    private BackedMapCacheLoader loader = new BackedMapCacheLoader();

    @Override
    public int size() {
        if (memoryBackingCache.size() > Integer.MAX_VALUE) return Integer.MAX_VALUE;
        else return (int) memoryBackingCache.size();
    }

    @Override
    public boolean isEmpty() {
        return memoryBackingCache.asMap().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return memoryBackingCache.asMap().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return memoryBackingCache.asMap().containsValue(value);
    }

    @Override
    public V get(Object key) {
        V v = null;
        try {
            v = memoryBackingCache.get((K) key);
        } catch (ExecutionException e) {
            logger.error("Loader threw ExecutionException: " + e);
        }
        if (v == null) {
            try {
                v = loader.load((K) key);
            } catch (Exception e) {
                logger.error("Loader threw exception: " + e);
            }


        }
        return v;
    }

    @Override
    public V put(K key, V value) {
        memoryBackingCache.put(key, value);
        diskBackingDatastore.put(key.toBytes(), value.toBytes());
        return value;
    }


    /**
     * As this method will actually commit a deletion from the backing database,
     * it will make no sense to return the deleted value. Therefore null is returned.
     *
     * @param key The key to delete
     * @return Null at all times.
     */
    @Override
    public V remove(Object key) {
        this.memoryBackingCache.invalidate(key);
        this.memoryBackingCache.asMap().remove(key);
        if (key instanceof AbstractByteArrayStorable)
            this.diskBackingDatastore.delete(((AbstractByteArrayStorable) key).toBytes());
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        this.memoryBackingCache.putAll(m);
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            diskBackingDatastore.put(entry.getKey().toBytes(), entry.getValue().toBytes());
        }
    }

    /**
     * Due to limitations of levelDB the entire map cannot be cleared from disk efficiently--only the cache.
     * Use {@link LevelDBBackedMap#clearDb()} to clear the database and map.
     */
    @Override
    public void clear() {
        this.memoryBackingCache.asMap().clear();
    }

    /**
     * Clears the entire cache and database. This will use a snapshot, so any new entries added on another thread will not be deleted.
     * @throws IOException
     */
    public void clearDb() throws IOException {
        this.clear();
        Snapshot s = diskBackingDatastore.getSnapshot();
        for(DBIterator dbi = diskBackingDatastore.iterator(new ReadOptions().verifyChecksums(false).snapshot(s)); dbi.hasNext();){
            Entry<byte[], byte[]> entry = dbi.next();
            diskBackingDatastore.delete(entry.getKey(), new WriteOptions().sync(false));
        }
        s.close();
    }
    @Override
    public Set<K> keySet() {
        return memoryBackingCache.asMap().keySet();
    }

    @Override
    public Collection<V> values() {
        return memoryBackingCache.asMap().values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return memoryBackingCache.asMap().entrySet();
    }

    private M manager;

    public LevelDBBackedMap(DB diskBackingDatastore, Class<V> valueClass) {
        this(diskBackingDatastore, valueClass, false);
    }

    public LevelDBBackedMap(DB diskBackingDatastore, Class<V> valueClass, boolean soft) {
        this.diskBackingDatastore = diskBackingDatastore;
        this.valueClass = valueClass;
        this.initMap0(soft, valueClass);
    }

    private void initMap0(boolean soft, Class<V> valueClass) {
        CacheBuilder builder = CacheBuilder.newBuilder();
        if (soft) builder.softValues();

        // need this odd assignment as per javadoc of CacheBuilder#removalListener()
        CacheBuilder<K, V> checkedCacheBuilder = builder.removalListener(new BackedMapRemovalListener());

        this.memoryBackingCache = checkedCacheBuilder.build(new BackedMapCacheLoader());
    }

    public LevelDBBackedMap(M manager, DB diskBackingDatastore, Class<V> valueClass, boolean soft) {
        this.manager = manager;
        this.diskBackingDatastore = diskBackingDatastore;
        this.valueClass = valueClass;
        this.initMap0(soft, valueClass);
    }

    private class BackedMapRemovalListener implements RemovalListener<K, V> {

        @Override
        public void onRemoval(RemovalNotification<K, V> notification) {
            switch (notification.getCause()) {
                case COLLECTED:
                    logger.warn("Un-cacahing " + notification.getKey().toString() + " due to GC. Memory may be low.");
                    break;
                case EXPIRED:
                    logger.info("Un-cacahing " + notification.getKey().toString() + " as it expired");
                case SIZE:
                    logger.warn("Un-cacahing " + notification.getKey().toString() + " due to a size constraint");
            }

        }
    }

    private class BackedMapCacheLoader extends CacheLoader<K, V> {
        @Override
        public V load(K k) throws Exception {
            V v = null;
            byte[] buf = diskBackingDatastore.get(k.toBytes());
            // nothing in the DB...
            if (buf == null) return null;

            try {
                if (manager == null) {
                    v = (V) valueClass.newInstance();
                } else {
                    v = (V) valueClass.newInstance();
                }
                v.setManager(manager);
                v.loadBytes(buf);


            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return v;
        }
    }
}


