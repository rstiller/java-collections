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

import jc.MapContext;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class MongoMapContext<K, V> extends MapContext<K, V> {
    
    DBCollection collection;
    DBObjectSerializer<Map.Entry<K, V>> entrySerializer;
    DBObjectSerializer<K> keySerializer;
    DBObjectSerializer<V> valueSerializer;
    
    public DBObjectSerializer<K> getKeySerializer() {
        return keySerializer;
    }
    
    public void setKeySerializer(DBObjectSerializer<K> keySerializer) {
        this.keySerializer = keySerializer;
    }
    
    public DBObjectSerializer<V> getValueSerializer() {
        return valueSerializer;
    }
    
    public void setValueSerializer(DBObjectSerializer<V> valueSerializer) {
        this.valueSerializer = valueSerializer;
    }
    
    public DBCollection getCollection() {
        return collection;
    }
    
    public void setCollection(DBCollection collection) {
        this.collection = collection;
    }
    
    public DBObjectSerializer<Map.Entry<K, V>> getEntrySerializer() {
        return entrySerializer;
    }
    
    public void setEntrySerializer(DBObjectSerializer<Map.Entry<K, V>> entrySerializer) {
        this.entrySerializer = entrySerializer;
    }
    
    public DBObject getFilter() {
        DBObject filter = null;
        
        // TODO
        
        if (filter == null) {
            filter = new BasicDBObject();
        }
        
        return filter;
    }
    
}
