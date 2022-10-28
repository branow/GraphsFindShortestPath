package com.kpi.courseproject.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface SetPlus<E> extends Iterable<E> {

    int size();

    boolean isEmpty();

    boolean contains(Object o);

    boolean add(E e);

    boolean remove(Object o);

    boolean addAll(Collection<? extends E> c);

    boolean removeAll(Collection<? extends E> c);

    void clear();

    List<E> getList();

}
