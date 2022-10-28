package com.kpi.courseproject.collection;

import java.io.Serializable;
import java.util.*;

public class LinkedListPlus<T> implements ListPlus<T>, Serializable, Iterable<T> {

    private Box<T> head, tail;
    private int size;

    public LinkedListPlus () {

    }

    public LinkedListPlus (Object[] array) {
        for (Object o: array) {
            add((T)o);
        }
    }

    @Override
    public int size() {
        return size;
    }

    public boolean addFirst(T o) {
        Box<T> box = new Box<>();
        box.setObject(o);
        Box<T> temp = null;
        if (head ==null) {
            tail = box;
        } else {
            head.setPrevious(box);
            box.setNext(head);
        }
        head = box;
        size++;
        return contains(o);
    }

    public boolean addLast(T o) {
        Box<T> box = new Box<>();
        box.setObject(o);
        if (head ==null) {
            head = box;
        } else {
            tail.setNext(box);
            box.setPrevious(tail);
        }
        tail = box;
        size++;
        return contains(o);
    }

    @Override
    public boolean add(T o) {
        return addLast(o);
    }

    public T getFirst() {
        return head.getObject();
    }

    public T getLast() {
        return tail.getObject();
    }

    @Override
    public T get(int index) {
        if (index>=size) {
            return null;
        }
        if (index ==0) {
            return head.getObject();
        } else if (index == size-1) {
            return tail.getObject();
        }
        Box<T> temp = head;
        for (int i=1; i<size; i++) {
            temp = temp.getNext();
            if (i == index) {
                return temp.getObject();
            }
        }
        return null;
    }

    @Override
    public boolean remove(T o) {
        Box<T> temp;
        if (o.equals(head.getObject()) || o.equals(tail.getObject())) {
            if (o.equals(head.getObject())) {
                temp = head.getNext();
                if (temp != null) {
                    temp.setPrevious(null);
                }
                head = temp;
            }
            if (o.equals(tail.getObject())) {
                temp = tail.getPrevious();
                if (temp != null) {
                    temp.setNext(null);
                }
                tail = temp;
            }
            size--;
            return !contains(o);
        }
        temp = head;
        while (temp!=null) {
            if (temp.getObject().equals(o)) {
                Box<T> previous = temp.getPrevious();
                Box<T> next = temp.getNext();
                if (previous!=null)
                    previous.setNext(next);
                if (next!=null)
                    next.setPrevious(previous);
                size--;
                return !contains(o);
            }
            temp = temp.getNext();
        }
        return !contains(o);
    }

    public T removeFirst() {
        T h = head.getObject();
        remove(head.getObject());
        return h;
    }

    public T removeLast() {
        T l = tail.object;
        remove(tail.object);
        return l;
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
    public int indexOf(T o) {
        if (head.getObject().equals(o)) {
            return 0;
        } else if (tail.getObject().equals(o)) {
            return size-1;
        }
        Box<T> temp = head;
        for (int i=1; i<size; i++) {
            temp = temp.getNext();
            if (temp.getObject().equals(o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean clear() {
        size = 0;
        head = null;
        tail = null;
        return isEmpty();
    }

    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean contains(T o) {
        if (size == 0) {
            return false;
        }
        if (head.getObject().equals(o)) {
            return true;
        } else if (tail.getObject().equals(o)) {
            return true;
        }
        Box<T> temp = head;
        for (int i=1; i<size; i++) {
            temp = temp.getNext();
            if (temp.getObject().equals(o)) {
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
    public ListPlus<T> copy() {
        return new LinkedListPlus<>(toArray());
    }

    @Override
    public List<T> getList() {
        List<T> list = new LinkedList<>();
        for (T t: this) {
            list.add(t);
        }
        return list;
    }

    @Override
    public boolean removeAll(ListPlus<T> l) {
        for (int i=0; i<l.size(); i++) {
            remove(l.get(i));
        }
        return !contains(l);
    }

    private class Box<T> implements Serializable {
        private T object;
        private Box<T> next;
        private Box<T> previous;

        public Box() {}

        public Box(T object, Box<T> next, Box<T> previous) {
            this.object = object;
            this.next = next;
            this.previous = previous;
        }

        public T getObject() {
            return object;
        }

        public void setObject(T object) {
            this.object = object;
        }

        public Box<T> getNext() {
            return next;
        }

        public void setNext(Box<T> next) {
            this.next = next;
        }

        public Box<T> getPrevious() {
            return previous;
        }

        public void setPrevious(Box<T> previous) {
            this.previous = previous;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedListPlus<?> that = (LinkedListPlus<?>) o;
        return size == that.size && head.equals(that.head) && tail.equals(that.tail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, tail, size);
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

}
