package com.kpi.courseproject.collection;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

public class HashSetPlus<E> implements SetPlus<E>, Iterable<E>, Serializable {

    private HashMapPlus<E, Object> map = new HashMapPlus<>();

    public HashSetPlus() {

    }

    public HashSetPlus(E[] array) {
        for (E e: array) {
            add(e);
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    public E get(int index) {
        return map.get(index).getKey();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public Iterator<E> iterator() {
        return new IteratorArray();
    }

    class IteratorArray implements Iterator<E> {
        private int index;

        @Override
        public boolean hasNext() {
            return index < map.size();
        }

        @Override
        public E next() {
            return get(index++);
        }
    }

    @Override
    public boolean add(E e) {
        if (map.put(e, 3) == null) {
            return false;
        }
        return contains(e);
    }

    @Override
    public boolean remove(Object o) {
        if (map.remove((E)o) == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e: c) {
            add(e);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<? extends E> c) {
        for (E e: c) {
            remove(e);
        }
        return true;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public List<E> getList () {
        List<E> list = new ArrayList<>();
        for (E e: this) {
            list.add(e);
        }
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashSetPlus<?> that = (HashSetPlus<?>) o;
        return getList().equals(((HashSetPlus<?>) o).getList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }

    @Override
    public String toString() {
        return getList().toString();
    }
}
