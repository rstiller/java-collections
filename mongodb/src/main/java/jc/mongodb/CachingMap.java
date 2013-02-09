/**
 * Copyright 2013 the project contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package jc.mongodb;

import java.util.Map;

public class CachingMap<K, V> implements Map<K, V> {
    
    Map<K, V> cache;
    MongoConcurrentMap<K, V> backstore;
    
    public CachingMap(Map<K, V> cache, MongoConcurrentMap<K, V> backstore) {
        this.cache = cache;
        this.backstore = backstore;
    }
    
    @Override
    public void clear() {
        cache.clear();
        backstore.clear();
    }
    
    @Override
    public boolean containsKey(Object key) {
        if (!cache.containsKey(key)) {
            return backstore.containsKey(key);
        }
        return true;
    }
    
    @Override
    public boolean containsValue(Object value) {
        if (!cache.containsValue(value)) {
            return backstore.containsValue(value);
        }
        return true;
    }
    
    @Override
    public MongoSet<Entry<K, V>> entrySet() {
        return backstore.entrySet();
    }
    
    @Override
    public V get(Object key) {
        if (!cache.containsKey(key)) {
            if (backstore.containsKey(key)) {
                return backstore.get(key);
            }
            return null;
        }
        return cache.get(key);
    }
    
    @Override
    public boolean isEmpty() {
        return cache.isEmpty() ? backstore.isEmpty() : false;
    }
    
    @Override
    public MongoSet<K> keySet() {
        return backstore.keySet();
    }
    
    @Override
    public V put(K key, V value) {
        cache.put(key, value);
        return backstore.put(key, value);
    }
    
    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        cache.putAll(map);
        backstore.putAll(map);
    }
    
    @Override
    public V remove(Object key) {
        cache.remove(key);
        return backstore.remove(key);
    }
    
    @Override
    public int size() {
        return backstore.size();
    }
    
    @Override
    public MongoCollection<V> values() {
        return backstore.values();
    }
    
}
