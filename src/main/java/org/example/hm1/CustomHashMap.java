package org.example.hm1;

import java.util.*;

public class CustomHashMap<K, V> implements Map<K, V> {
    private int size;
    private int initialCapacity = 16;
    private double loadFactor = 0.75;
    private Node<K, V>[] buckets;

    private class Node<K, V> implements Map.Entry<K, V> {
        int hash;
        K key;
        V value;
        Node<K, V> nextNode;

        Node(int hash, K key, V value, Node nextNode) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.nextNode = nextNode;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            this.value = value;
            return value;
        }

        public String toString() {
            return key + "=" + value;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;

            return o instanceof Map.Entry<?, ?> entry
                && Objects.equals(key, entry.getKey())
                && Objects.equals(value, entry.getValue());
        }
    }

    public CustomHashMap() {
        buckets = new Node[initialCapacity];
    }

    public CustomHashMap(int initialCapacity) {
        this.initialCapacity = roundToPowerTwo(initialCapacity);
        buckets = new Node[this.initialCapacity];
    }

    private int hash(Object key) {
        return (key != null) ? key.hashCode() : 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Map.Entry<K, V> entry : entrySet()) {
            if(entry.getValue().equals(value)) return true;
        }
        return false;
    }

    @Override
    public V get(Object key) {
        Node<K,V> node;
        return (node = getNode(key)) == null ? null : node.value;
    }

    private Node<K,V> getNode(Object key) {
        int hash = hash(key);
        int index = hash & (initialCapacity - 1);

        if(buckets != null && buckets[index] != null) {
            Node<K, V> node = buckets[index];

            if(hash == node.hash && key.equals(node.key)) {
                return node;
            }
            while (node.nextNode != null) {
                node = node.nextNode;
                if(hash == hash(node.key) && key.equals(node.key)) {
                    return node;
                }
            }
        }

        return null;
    }

    @Override
    public V put(K key, V value) {
        int hash = hash(key);
        int index = hash & (initialCapacity - 1);

        if(buckets == null || buckets[index] == null) {
            buckets[index] = new Node<>(hash, key, value, null);
            size++;
            if(size > initialCapacity * loadFactor) resize();
        }
        else {
            Node<K, V> node = buckets[index];
            if(hash == node.hash && key.equals(node.key)){
                node.value = value;
            }
            else {
                boolean keyNotFound = true;
                while (node.nextNode != null) {
                    node = node.nextNode;
                    if(hash == node.hash && key.equals(node.key)) {
                        node.value = value;
                        keyNotFound = false;
                        break;
                    }
                }

                if(keyNotFound) {
                    node.nextNode = new Node<>(hash, key, value, null);
                    size++;
                    if(size > initialCapacity * loadFactor) resize();

                    return value;
                }
            }
        }

        return null;
    }

    @Override
    public V remove(Object key) {
        if(key == null) return null;

        int hash = hash(key);
        int index = hash & (initialCapacity - 1);

        if(buckets != null && buckets[index] != null) {
            Node<K, V> node = buckets[index];

            if(hash == node.hash && key.equals(node.key)) {
                buckets[index] = node.nextNode != null ? node.nextNode : null;
                size--;
                return node.value;
            }
            while (node.nextNode != null) {
                Node<K, V> prevNode = node;
                node = node.nextNode;
                if(hash == node.hash && key.equals(node.key)){
                    prevNode.nextNode = node.nextNode != null ? node.nextNode : null;
                    size--;
                    return node.value;
                }
            }
        }

        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        if (buckets != null && size > 0) {
            size = 0;
            for (int i = 0; i < buckets.length; i++)
                buckets[i] = null;
        }
    }

    private Node<K,V>[] resize() {
        Node<K, V>[] oldBuckets = buckets;
        int oldCapacity = oldBuckets.length;

        initialCapacity = oldCapacity * 2;
        Node<K, V>[] newBuckets = new Node[initialCapacity];

        Node<K, V> current;
        for (int i = 0; i < oldCapacity; i++) {
            current = oldBuckets[i];

            while (current != null) {
                Node<K, V> next = current.nextNode;

                current.hash = hash(current.key);
                int newIndex = current.hash & (initialCapacity - 1);

                current.nextNode = newBuckets[newIndex];
                newBuckets[newIndex] = current;

                current = next;
            }
        }

        return buckets = newBuckets;
    }

    private static int roundToPowerTwo(int n) {
        if (n <= 16) return 16;

        int power = (int) Math.ceil(Math.log(n) / Math.log(2));
        return (int) Math.pow(2, power);
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();

        for (Node<K, V> bucket : buckets) {

            Node<K, V> current = bucket;
            while (current != null) {
                keys.add(current.key);
                current = current.nextNode;
            }
        }
        return keys;
    }

    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<>();

        for (Node<K, V> bucket : buckets) {

            Node<K, V> current = bucket;
            while (current != null) {
                values.add(current.value);
                current = current.nextNode;
            }
        }
        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entries = new HashSet<>();

        for (Node<K, V> bucket : buckets) {
            Node<K, V> current = bucket;
            while (current != null) {
                entries.add(current);
                current = current.nextNode;
            }
        }
        return entries;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder("{\n");

        for (Map.Entry<K, V> entry : entrySet()) {
            sb.append("\t").append(entry.toString()).append(",\n");
        }

        sb.setLength(sb.length() - 2);
        sb.append("\n}");

        return sb.toString();
    }
}
