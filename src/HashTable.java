import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;

public class HashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private LinkedList<Entry<K, V>>[] table;
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

    public int size() {
        return size;
    }

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

    public V get(K key) {
        int index = getIndex(key);
        for (Entry<K, V> e : table[index]) {
            if (e.key.equals(key)) {
                return e.value;
            }
        }
        return null;
    }

    public void remove(K key) {
        int index = getIndex(key);
        for (Entry<K, V> e : table[index]) {
            if (e.key.equals(key)) {
                table[index].remove(e);
                size--;
                return;
            }
        }
    }

    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (LinkedList<Entry<K, V>> list : table) {
            for (Entry<K, V> entry : list) {
                keys.add(entry.key);
            }
        }
        return keys;
    }

    private int getIndex(K key) {
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    private static class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
