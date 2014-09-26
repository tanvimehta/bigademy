package com.bigademy.utils;

/**
 * This pair class can be used when two objects need to be associated together, for instance if a function needs to return two objects
 * @param <K> the type of the first parameter
 * @param <V> the type of the second parameter
 */
public class Pair<K, V> {

    public K key;
    public V value;

    public Pair(K key1, V value1) {
        this.key = key1;
        this.value = value1;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Pair<?, ?>) {
            Pair<?, ?> p = (Pair<?, ?>) obj;
            return key.equals(p.key) && value.equals(p.value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return key.hashCode() ^ value.hashCode();
    }

    @Override
    public String toString() {
        return key + ": " + value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}