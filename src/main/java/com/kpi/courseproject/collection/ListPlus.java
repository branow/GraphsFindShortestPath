package com.kpi.courseproject.collection;

import java.util.Iterator;
import java.util.List;

public interface ListPlus<T> extends Iterable<T> {

    int size();

    boolean add(T o);

    T get(int index);

    boolean remove(T o);

    int indexOf(T o);

    boolean removeAll(ListPlus<T> list);

    boolean clear();

    boolean isEmpty();

    boolean contains(T o);

    @Override
    Iterator<T> iterator();

    boolean contains(ListPlus<T> l);

    Object[] toArray();

    List<T> getList();

    ListPlus<T> copy();

}
