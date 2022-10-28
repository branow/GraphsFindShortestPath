package com.kpi.courseproject.collection;

import com.kpi.courseproject.logic.Edge;
import com.kpi.courseproject.logic.Vertical;

import java.io.Serializable;
import java.util.*;

public class BinaryTreeMapPlus<K, V> implements MapPlus<K, V>, Serializable {

    private final Comparator<K> comparator;
    private Node<K, V> root;
    private int size;

    public BinaryTreeMapPlus() {
        comparator = null;
    }

    public BinaryTreeMapPlus(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int size() {
        return size;
    }

    final int compare(Node<K, V> k1, Node<K, V> k2) {
        return comparator==null ? ((Comparable<? super K>)k1.key).compareTo((K)k2.key)
                : comparator.compare(k1.key, k2.key);
    }

    @Override
    public V put(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        if (root == null) {
            root = node;
            return value;
        }
        Node<K, V> temp = root;
        Node<K, V> parent = null;
        while (temp!=null) {
            parent = temp;
            int val = compare(node, temp);
            if (val < 0) {
                temp = temp.left;
            } else if (val == 0) {
                node.parent = temp.parent;
                node.left = temp.left;
                node.right = temp.left;
                break;
            } else {
                temp = temp.right;
            }
        }
        if (compare(parent, node) > 0) {
            parent.left = node;
            node.parent = parent;
        } else {
            parent.right = node;
            node.parent = parent;
        }
        size++;
        return value;
    }

    @Override
    public void putAll(MapPlus<? extends K, ? extends V> map) {
        for (Entry<? extends K,? extends V> entry: map.entrySet() ) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V get(Object key) {
        Node<K, V> node = new Node<>((K)key, null);
        Node<K, V> temp = root;
        while (temp!=null) {
            int val = compare(node, temp);
            if (val < 0) {
                temp = temp.left;
            } else if (val == 0) {
                return temp.value;
            } else {
                temp = temp.right;
            }
        }
        return null;
    }

    @Override
    public V remove(Object key) {
        Node<K, V> node = new Node<>((K)key, null);
        Node<K, V> temp = root;
        while (temp!=null) {
            int val = compare(node, temp);
            if (val < 0) {
                temp = temp.left;
            } else if (val == 0) {
                size--;
                Node<K, V> parent = temp.parent;
                Node<K, V> left = temp.left;
                Node<K, V> right = temp.right;
                if (parent.left == temp) {
                    parent.left = right;
                    right.parent = parent.left;
                } else {
                    parent.right = right;
                    right.parent = parent.right;
                }
                Node<K, V> t = right;
                while (t!=null) {
                    parent = t;
                    t = t.left;
                }
                t = left;
                left.parent = parent;
                return temp.value;
            } else {
                temp = temp.right;
            }
        }
        return null;
    }

    public K getKeyy (V value) {
        Node<K, V> temp = null;
        LinkedListPlus<Node<K,V>> nodes = new LinkedListPlus<>();
        nodes.add(root);
        while (!nodes.isEmpty()) {
            temp = nodes.getFirst();
            nodes.removeFirst();
            if (temp.value.equals(value)) return temp.key;
            if (temp.left != null) nodes.add(temp.left);
            if (temp.right != null) nodes.add(temp.right);
        }
        return null;
    }

    public K getKey(V value) {
        ListPlus<Node<K,V>> his = new ArrayListPlus<>();
        if (root!=null) {
            Node<K,V> temp = lastLeft(root);
            Node<K,V> last = lastRight(root);
            if (root.value.equals(value)) return root.key;
            if (last.value.equals(value)) return last.key;
            while (!temp.equals(last)) {
                his.add(temp);
                if (temp.value.equals(value)) return temp.key;
                if (temp.right != null) {
                    temp = temp.right;
                    temp = lastLeft(temp);
                } else {
                    temp = temp.parent;
                    while (his.contains(temp)) {
                        temp = temp.parent;
                    }
                }
            }
            his.add(last);
        }
        return null;
    }

    private Node<K, V> lastLeft(Node<K, V> start) {
        Node<K, V> temp = start;
        if (start == null) {
            return null;
        }
        while (temp.left !=null) {
            temp = temp.left;
        }
        return temp;
    }

    private Node<K, V> lastRight(Node<K, V> start) {
        Node<K, V> temp = start;
        if (start == null) {
            return null;
        }
        while (temp.right !=null) {
            temp = temp.right;
        }
        return temp;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        Node<K, V> node = new Node<>((K)key, null);
        Node<K, V> temp = root;
        while (temp!=null) {
            int val = compare(node, temp);
            if (val < 0) {
                temp = temp.left;
            } else if (val == 0) {
                return true;
            } else {
                temp = temp.right;
            }
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Entry<K, V> entry: entrySet()) {
            if (entry.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clear() {
        size =0;
        root = null;
    }

    @Override
    public SetPlus<K> keySet() {
        Entry<K, V>[] ar1 = (Entry<K, V>[]) toArray();
        K[] ar2 = (K[]) new Object[ar1.length];
        for (int i=0; i<ar2.length; i++) {
            ar2[i] = ar1[i].getKey();
        }
        return new HashSetPlus<>(ar2);
    }

    @Override
    public SetPlus<Entry<K, V>> entrySet() {
        return new HashSetPlus<>((Entry<K, V>[]) toArray());
    }

    public Object[] toArray () {
        Object[] ar = getList().toArray();
        return ar;
    }

    public ListPlus<Node<K, V>> getList () {
        ListPlus<Node<K,V>> his = new ArrayListPlus<>();
        if (root!=null) {
            Node<K,V> temp = lastLeft(root);
            Node<K,V> last = lastRight(root);
            while (!temp.equals(last)) {
                his.add(temp);
                if (temp.right != null) {
                    temp = temp.right;
                    temp = lastLeft(temp);
                } else {
                    temp = temp.parent;
                    while (his.contains(temp)) {
                        temp = temp.parent;
                    }
                }
            }
            his.add(last);
        }
        return his;
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryTreeMapPlus<?, ?> that = (BinaryTreeMapPlus<?, ?>) o;
        return  this.entrySet().equals(that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comparator, root, size);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new IteratorTree();
    }

    private class IteratorTree implements Iterator<MapPlus.Entry<K, V>> {
        private int index;
        private Entry<K, V>[] ar;

        IteratorTree () {
            ar = (Entry<K, V>[]) toArray();
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Entry<K, V> next() {
            return ar[index++];
        }
    }

    private class Node<K, V> implements MapPlus.Entry<K, V>, Serializable {
        private K key;
        private V value;
        private Node<K, V> parent, left, right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
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
        public Entry<K, V> getNext() {
            if (left!= null) {
                return left;
            } else if (right!= null) {
                return right;
            }
            return null;
        }

        public Node<K, V> getParent() {
            return parent;
        }

        public void setParent(Node<K, V> parent) {
            this.parent = parent;
        }

        public Node<K, V> getLeft() {
            return left;
        }

        public void setLeft(Node<K, V> left) {
            this.left = left;
        }

        public Node<K, V> getRight() {
            return right;
        }

        public void setRight(Node<K, V> right) {
            this.right = right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return key.equals(node.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
}
