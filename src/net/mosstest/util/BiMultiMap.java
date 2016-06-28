package net.mosstest.util;
import java.util.HashMap;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

// based on code of Stack Overflow user gdejohn
    public class BiMultiMap<K, V>
    {
        private final HashMap<K, V> keysToValues = new HashMap<>();

        private final SetMultimap<V, K> valuesToKeys = HashMultimap.create();

        public V getValue(K key)
        {
            return keysToValues.get(key);
        }

        public Set<K> getKeys(V value)
        {
            return valuesToKeys.get(value);
        }

        public boolean put(K key, V value)
        {
            keysToValues.put(key, value);
            return valuesToKeys.put(value, key); // hacky workaround

        }

        public boolean putAll(Iterable<? extends K> keys, V value)
        {
            boolean changed = false;

            for (K key : keys)
            {
                changed = put(key, value) || changed;
            }

            return changed;
        }

}
