package com.kpi.courseproject.collection;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

public interface MapPlus <K, V> extends Iterable<MapPlus.Entry<K, V>> {

    int size();

    V get (Object key);

    V put (K key, V value);

    void putAll(MapPlus<? extends K, ? extends V> m);

    V remove (Object key);

    boolean isEmpty();

    boolean containsKey(Object key);

    boolean containsValue(Object value);

    void clear();

    SetPlus<K> keySet();

    SetPlus<MapPlus.Entry<K, V>> entrySet();

    interface Entry<K, V> {

        K getKey();

        V getValue();

        V setValue(V value);

        Entry<K, V> getNext();

        static <K extends Comparable<? super K>, V> Comparator<Map.Entry<K, V>> comparingByKey() {
            return (Comparator<Map.Entry<K, V>> & Serializable)
                    (c1, c2) -> c1.getKey().compareTo(c2.getKey());
        }

        static <K, V extends Comparable<? super V>> Comparator<Map.Entry<K, V>> comparingByValue() {
            return (Comparator<Map.Entry<K, V>> & Serializable)
                    (c1, c2) -> c1.getValue().compareTo(c2.getValue());
        }

        static <K, V> Comparator<Map.Entry<K, V>> comparingByKey(Comparator<? super K> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<Map.Entry<K, V>> & Serializable)
                    (c1, c2) -> cmp.compare(c1.getKey(), c2.getKey());
        }

        static <K, V> Comparator<Map.Entry<K, V>> comparingByValue(Comparator<? super V> cmp) {
            Objects.requireNonNull(cmp);
            return (Comparator<Map.Entry<K, V>> & Serializable)
                    (c1, c2) -> cmp.compare(c1.getValue(), c2.getValue());
        }

    }
}
