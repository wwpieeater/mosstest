package net.mosstest.servercore;

import org.iq80.leveldb.DB;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * An in-memory map backed by a larger data-set stored in a levelDB byte[] to byte[] key-value store.
 * @param <K>
 * @param <V>
 */
public class LevelDBBackedMap<K extends AbstractByteArrayStorable<?>, V extends AbstractByteArrayStorable<M>, M> implements Map<K, V> {
    private final Class<V> valueClass;
    // TODO a map backed by a levelDB datastore

    HashMap<K,V> memoryBackingMap = new HashMap<>();

    DB diskBackingDatastore;

    @Override
    public int size() {
        return memoryBackingMap.size();
    }

    @Override
    public boolean isEmpty() {
        return memoryBackingMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return memoryBackingMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return memoryBackingMap.containsValue(value);
    }

    @Override
    public V get(Object key) {
        V v = memoryBackingMap.get(key);
        if(v == null){
            byte[] buf = diskBackingDatastore.get(((AbstractByteArrayStorable<?>) key).toBytes());
            if(buf == null) return null;
            try {
                if(manager == null){
                    v = (V) valueClass.newInstance();
                } else {
                    v = (V) valueClass.newInstance();
                    System.out.println(v.toString());
                }
                v.setManager(manager);
                v.loadBytes(buf);

            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return v;
    }

    @Override
    public V put(K key, V value) {
        memoryBackingMap.put(key, value);
        diskBackingDatastore.put(key.toBytes(), value.toBytes());
        return value;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }
    private M manager;

    public LevelDBBackedMap(DB diskBackingDatastore, Class<V> valueClass) {
        this.diskBackingDatastore = diskBackingDatastore;
        this.valueClass = valueClass;
    }

    public LevelDBBackedMap(M manager, DB diskBackingDatastore,  Class<V> valueClass) {
        this.manager = manager;
        this.diskBackingDatastore = diskBackingDatastore;
        this.valueClass = valueClass;
    }
}
