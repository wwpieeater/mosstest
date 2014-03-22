package net.mosstest.servercore;

import org.iq80.leveldb.DB;

import java.util.*;

/**
 * An in-memory map backed by a larger data-set stored in a levelDB byte[] to byte[] key-value store.
 * @param <K>
 * @param <V>
 */
public class LevelDBBackedMap<K extends AbstractByteArrayStorable, V extends AbstractByteArrayStorable> implements Map<K, V> {
    // TODO a map backed by a levelDB datastore

    HashMap<K,V> memoryBackingMap;

    DB diskBackingDatastore;

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        return null;
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

}
