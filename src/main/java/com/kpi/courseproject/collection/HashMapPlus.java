
package com.kpi.courseproject.collection;

import java.io.Serializable;
import java.util.*;

public class HashMapPlus<K, V> implements MapPlus<K, V>, Serializable, Iterable<MapPlus.Entry<K, V>>{

    private Node<K, V>[] hashTable;
    private int size;
    private final int capacity = 16;

    public HashMapPlus () {
        hashTable = new Node[capacity];
    }

    private int hash (K key) {
        return Math.abs(31 * 17 + (key.hashCode()-1)) % (hashTable.length-1);
    }

    @Override
    public V put(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        int index = hash(key);
        Node<K, V> temp = hashTable[index];
        while (temp != null) {
            if (temp.equals(node)) {
                temp.setValue(node.getValue());
                return node.getValue();
            }
            temp = temp.getNext();
        }
        hashTable[index] = node;
        size++;
        return value;
    }

    @Override
    public void putAll(MapPlus<? extends K, ? extends V> map) {
        for (MapPlus.Entry<? extends K, ? extends V> node: map.entrySet()) {
            if (node!=null) {
                put(node.getKey(), node.getValue());
                while (node.getNext() !=null) {
                    put(node.getNext().getKey(), node.getNext().getValue());
                    node = node.getNext();
                }
            }
        }
    }

    @Override
    public V get(Object key) {
        int index = hash((K)key);
        Node<K, V> temp = hashTable[index];
        while (temp != null) {
            if (temp.getKey().equals(key)) {
                return temp.getValue();
            }
            temp = temp.getNext();
        }
        return null;
    }

    @Override
    public V remove(Object key) {
        int index = hash((K)key);
        Node<K, V> temp = hashTable[index];
        if (temp == null) {
            return null;
        } else if (temp.getKey().equals(key)) {
            size--;
            if (temp.getNext() == null) {
                hashTable[index] = null;
            } else {
                hashTable[index] = temp.getNext();
            }
            return temp.getValue();
        }
        while (temp.getNext() != null) {
            if (temp.getNext().getKey().equals(key)) {
                V value = temp.getNext().getValue();
                temp.setNext(temp.getNext().getNext());
                size--;
                return value;
            }
            temp = temp.getNext();
        }
        return null;
    }

    public MapPlus.Entry<K, V> get(int index) {
        int i = 0;
        for (Node<K, V> node: hashTable) {
            while (node!=null) {
                if (i == index) {
                    return node;
                }
                node = node.getNext();
                i++;
            }
        }
        return null;
    }

    @Override
    public Iterator<MapPlus.Entry<K, V>> iterator() {
        return new IteratorMap();
    }

    class IteratorMap implements Iterator<MapPlus.Entry<K, V>> {
        private int index;

        @Override
        public boolean hasNext() {
            return index < size();
        }

        @Override
        public MapPlus.Entry<K, V> next() {
            return get(index++);
        }
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
        int index = hash((K)key);
        Node<K, V> temp = hashTable[index];
        while (temp != null) {
            if (temp.getKey().equals(key)) {
                return true;
            }
            temp = temp.getNext();
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Node<K, V> node: hashTable) {
            if (node!=null) {
                if (node.getValue().equals(value)) {
                    return true;
                }
                while (node.getNext()!=null) {
                    if (node.getNext().getValue().equals(value)) {
                        return true;
                    }
                    node = node.getNext();
                }
            }
        }
        return false;
    }

    @Override
    public void clear() {
        hashTable = new Node[capacity];
        size = 0;
    }

    @Override
    public SetPlus<K> keySet() {
        SetPlus<K> set = new HashSetPlus<>();
        for (Node<K, V> node: hashTable) {
            if (node!=null) {
                set.add(node.getKey());
                while (node.getNext()!=null) {
                    set.add(node.getNext().getKey());
                    node = node.getNext();
                }
            }
        }
        return set;
    }

    @Override
    public SetPlus<MapPlus.Entry<K, V>> entrySet() {
        SetPlus<MapPlus.Entry<K, V>> set = new HashSetPlus<>();
        for (Node<K, V> node: hashTable) {
            if (node!=null) {
                set.add(node);
                while (node.getNext()!=null) {
                    set.add(node.getNext());
                    node = node.getNext();
                }
            }
        }
        return set;
    }

    @Override
    public String toString() {
        return entrySet()+"";
    }

    private class Node<K, V> implements MapPlus.Entry<K, V>, Serializable {
        private Node<K, V> next;
        private K key;
        private V value;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o.getClass().equals(getClass())) {
                Node<K, V> node = (Node) o;
                if (hashCode() == node.hashCode()) {
                    if (key.equals(node.getKey()) && value.equals(node.getValue())) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Math.abs(31 * 17 + (key.hashCode()-1));
        }

        @Override
        public String toString() {
            return "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
}
