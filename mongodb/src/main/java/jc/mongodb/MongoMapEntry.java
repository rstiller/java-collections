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

import java.util.Map.Entry;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class MongoMapEntry<K, V> implements Entry<K, V> {
    
    DBCollection collection;
    DBObject document;
    K key;
    DBObjectSerializer<V> serializer;
    
    public MongoMapEntry(K key, DBCollection collection, DBObject document, DBObjectSerializer<V> serializer) {
        this.document = document;
        this.collection = collection;
        this.key = key;
        this.serializer = serializer;
    }
    
    @Override
    public K getKey() {
        return key;
    }
    
    @Override
    public V getValue() {
        return serializer.toElement(document);
    }
    
    @Override
    public V setValue(V value) {
        V old = getValue();
        document.putAll(serializer.toDBObject(value, false, false));
        collection.save(document);
        return old;
    }
    
}
