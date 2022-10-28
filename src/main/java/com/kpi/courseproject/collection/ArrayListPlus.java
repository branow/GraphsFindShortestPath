package com.kpi.courseproject.collection;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

public class ArrayListPlus<T> implements ListPlus<T>, Serializable, Iterable<T> {

    private Object[] array;
    private int capacity = 10;
    private int size;

    public ArrayListPlus () {
        array = new Object[capacity];
    }

    public ArrayListPlus (int capacity) {
        this.capacity = capacity;
        array = new Object[capacity];
    }

    public ArrayListPlus (Object[] array) {
        this.capacity = array.length;
        this.array = array;
        this.size = array.length;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(T o) {
        array[size] = o;
        size++;
        if (size>=capacity) {
            increaseArray();
        }
        return contains(o);
    }

    @Override
    public T get(int index) {
        if (index>=size) {
            return null;
        }
        return (T)array[index];
    }

    @Override
    public boolean remove(T o) {
        int index = indexOf(o);
        array[index] = null;
        movElement(index);
        return !contains(o);
    }

    @Override
    public int indexOf(T o) {
       for (int i=0; i<size; i++) {
           if (array[i].equals(o)) {
               return i;
           }
       }
       return -1;
    }

    @Override
    public boolean clear() {
        capacity = 10;
        array = new Object[capacity];
        size = 0;
        return isEmpty();
    }

    @Override
    public boolean isEmpty() {
        if (size==0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(T o) {
        for (int i=0; i<size; i++) {
            if (array[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new IteratorArray();
    }

    class IteratorArray implements Iterator<T> {
        private int index;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public T next() {
            return get(index++);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public boolean contains(ListPlus<T> l) {
        boolean flag = true;
        for (int i=0; i< l.size(); i++) {
            if (!contains(l.get(i))){
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        for (int i=0; i<array.length; i++) {
            array[i] = get(i);
        }
        return array;
    }

    @Override
    public List<T> getList() {
        List<T> list = new ArrayList<>();
        for (T t: this) {
            list.add(t);
        }
        return list;
    }

    @Override
    public ListPlus<T> copy() {
        return new ArrayListPlus<>(array);
    }

    @Override
    public boolean removeAll(ListPlus<T> l) {
        for (int i=0; i< l.size(); i++) {
            if (contains(l.get(i))){
                remove(l.get(i));
            }
        }
        size-= l.size();
        return !contains(l);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayListPlus<?> that = (ArrayListPlus<?>) o;
        return capacity == that.capacity && size == that.size && Arrays.equals(array, that.array);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(capacity, size);
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }

    @Override
    public String toString() {
        Object[] arr = Arrays.copyOf(array, size);
        return Arrays.toString(arr);
    }

    private void increaseArray() {
        increaseCapacity();
        Object[] arr = new Object[capacity];
        for (int i=0; i<array.length; i++) {
            arr[i] = array[i];
        }
        array = arr;
    }

    private void increaseCapacity() {
        capacity *=1.5;
    }

    private void movElement (int emptyIndex) {
        for (int i=emptyIndex+1; i<size; i++) {
            array[i-1] = array[i];
        }
        array[size-1] = null;
        size--;
    }
}
