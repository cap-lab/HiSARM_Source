package com.scriptparser.parserdatastructure.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class KeyValueList<K, V> {
    private List<KeyValue<K, V>> list = new ArrayList<>();

    public int size() {
        return list.size();
    }

    public void add(int index, K key, V value) {
        list.add(index, new KeyValue<K, V>(key, value));
    }

    public void add(int index, KeyValue<K, V> kv) {
        list.add(index, kv);
    }

    public void add(KeyValue<K, V> kv) {
        list.add(kv);
    }

    public void add(K key, V value) {
        list.add(new KeyValue<K, V>(key, value));
    }

    public boolean containKey(K key) {
        return list.stream().anyMatch(kv -> kv.key.equals(key));
    }

    private void removeValues(K key) {
        list = new ArrayList<KeyValue<K, V>>(
                list.stream().filter(kv -> !kv.key.equals(key)).collect(Collectors.toList()));
    }

    private void removeFirst(K key) {
        for (KeyValue<K, V> kv : list) {
            if (kv.key.equals(key)) {
                list.remove(kv);
                break;
            }
        }
    }

    public <T> List<T> values(Class<T> classType) {
        List<T> values = new ArrayList<>();
        for (KeyValue<K, V> kv : list) {
            values.add(classType.cast(kv.value));
        }
        return values;
    }

    public <T> List<T> values(K key, Class<T> classType) {
        List<T> values = new ArrayList<>();
        for (KeyValue<K, V> kv : list) {
            if (kv.key.equals(key)) {
                values.add(classType.cast(kv.value));
            }
        }
        return values;
    }

    public <T> T findFirst(K key, Class<T> classType) {
        T value = null;
        for (KeyValue<K, V> kv : list) {
            if (kv.key.equals(key)) {
                value = classType.cast(kv.value);
            }
        }
        return value;
    }

    public Iterator<KeyValue<K, V>> iterator() {
        return list.iterator();
    }

    public <T> List<T> popValues(K key, Class<T> classType) {
        List<T> values = values(key, classType);
        removeValues(key);
        return values;
    }

    public <T> T popFirst(K key, Class<T> classType) {
        T value = findFirst(key, classType);
        removeFirst(key);
        return value;
    }

}
