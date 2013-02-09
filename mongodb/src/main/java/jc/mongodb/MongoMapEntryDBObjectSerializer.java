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

public class MongoMapEntryDBObjectSerializer<K, V> implements DBObjectSerializer<Entry<K, V>> {
    
    DBCollection collection;
    DBObjectSerializer<K> keySerializer;
    DBObjectSerializer<V> valueSerializer;
    
    public MongoMapEntryDBObjectSerializer(DBCollection collection, DBObjectSerializer<K> keySerializer,
            DBObjectSerializer<V> valueSerializer) {
        this.collection = collection;
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
    }
    
    @Override
    public DBObject toDBObject(Entry<K, V> element, boolean equalFunctions, boolean negate) {
        DBObject obj = keySerializer.toDBObject(element.getKey(), equalFunctions, negate);
        obj.putAll(valueSerializer.toDBObject(element.getValue(), equalFunctions, negate));
        
        return obj;
    }
    
    @Override
    public MongoMapEntry<K, V> toElement(DBObject dbObject) {
        return new MongoMapEntry<K, V>(keySerializer.toElement(dbObject), collection, dbObject, valueSerializer);
    }
    
}
