/**
 * @file: HashTable.java
 * @description: This file implements a Hashtable from scratch
 * @author: Connor Olive
 */

import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;

/**
 * Class representing a hashtable
 */
public class HashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private final LinkedList<Entry<K, V>>[] table;
    private int size = 0;

    public HashTable() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public HashTable(int capacity) {
        table = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }
    }

    /**
     * Returns the size of the hashtable
     * @return size -> number of keys
     */
    public int size() {
        return size;
    }

    /**
     * Adds a key value pair to the hashtable. If the key exists, the value gets overwritten
     * @param key -> key
     * @param value -> value
     */
    public void put(K key, V value) {
        int index = getIndex(key);
        Entry<K, V> entry = new Entry<>(key, value);
        for (Entry<K, V> e : table[index]) {
            if (e.key.equals(key)) {
                e.value = value;
                return;
            }
        }
        table[index].add(entry);
        size++;
    }

    /**
     * Returns the value of an associated key
     * @param key -> key to search for
     * @return e.value -> value associated with key
     */
    public V get(K key) {
        int index = getIndex(key);
        for (Entry<K, V> e : table[index]) {
            if (e.key.equals(key)) {
                return e.value;
            }
        }
        return null;
    }

    /**
     * Returns every key in a set
     * @return Set -> set containing every key
     */
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (LinkedList<Entry<K, V>> list : table) {
            for (Entry<K, V> entry : list) {
                keys.add(entry.key);
            }
        }
        return keys;
    }

    /**
     * Returns the index of a key
     * @param key -> key
     * @return int -> index of key
     */
    private int getIndex(K key) {
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    /**
     * Class representing a key-value pair in the hashtable
     * @param <K>
     * @param <V>
     */
    private static class Entry<K, V> {
        private final K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
