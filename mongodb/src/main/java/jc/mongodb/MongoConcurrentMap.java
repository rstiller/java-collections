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

import jc.ContextConcurrentMap;
import jc.mongodb.command.DefaultMapClearCommand;
import jc.mongodb.command.DefaultMapContainsKeyCommand;
import jc.mongodb.command.DefaultMapContainsValueCommand;
import jc.mongodb.command.DefaultMapEntrySetCommand;
import jc.mongodb.command.DefaultMapGetCommand;
import jc.mongodb.command.DefaultMapIsEmptyCommand;
import jc.mongodb.command.DefaultMapKeySetCommand;
import jc.mongodb.command.DefaultMapPutCommand;
import jc.mongodb.command.DefaultMapRemoveCommand;
import jc.mongodb.command.DefaultMapReplaceCommand;
import jc.mongodb.command.DefaultMapSizeCommand;
import jc.mongodb.command.DefaultMapValuesCommand;

import com.mongodb.DBCollection;

public class MongoConcurrentMap<K, V> extends ContextConcurrentMap<MongoMapContext<K, V>, K, V> {
    
    protected DBCollection collection;
    protected DBObjectSerializer<Map.Entry<K, V>> entrySerializer;
    protected DBObjectSerializer<K> keySerializer;
    protected DBObjectSerializer<V> valueSerializer;
    
    public MongoConcurrentMap(final DBCollection collection, final DBObjectSerializer<Entry<K, V>> entrySerializer,
            final DBObjectSerializer<K> keySerializer, final DBObjectSerializer<V> valueSerializer) {
        this.collection = collection;
        this.entrySerializer = entrySerializer;
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
        
        contexts = new ThreadLocal<MongoMapContext<K, V>>() {
            
            @Override
            protected MongoMapContext<K, V> initialValue() {
                MongoMapContext<K, V> ctx = new MongoMapContext<K, V>();
                
                ctx.setCollection(collection);
                ctx.setEntrySerializer(entrySerializer);
                ctx.setKeySerializer(keySerializer);
                ctx.setValueSerializer(valueSerializer);
                
                return ctx;
            }
            
        };
        
        sizeCommand = new DefaultMapSizeCommand<>();
        isEmptyCommand = new DefaultMapIsEmptyCommand<>();
        containsKeyCommand = new DefaultMapContainsKeyCommand<>();
        containsValueCommand = new DefaultMapContainsValueCommand<>();
        clearCommand = new DefaultMapClearCommand<>();
        keySetCommand = new DefaultMapKeySetCommand<>();
        valuesCommand = new DefaultMapValuesCommand<>();
        entrySetCommand = new DefaultMapEntrySetCommand<>();
        getCommand = new DefaultMapGetCommand<>();
        putCommand = new DefaultMapPutCommand<>(getCommand, containsKeyCommand);
        removeCommand = new DefaultMapRemoveCommand<>();
        replaceCommand = new DefaultMapReplaceCommand<>();
    }
    
    @Override
    public MongoSet<Entry<K, V>> entrySet() {
        return (MongoSet<Entry<K, V>>) super.entrySet();
    }
    
    @Override
    public MongoSet<K> keySet() {
        return (MongoSet<K>) super.keySet();
    }
    
    @Override
    public MongoCollection<V> values() {
        return (MongoCollection<V>) super.values();
    }
    
    @Override
    protected ThreadLocal<MongoMapContext<K, V>> getContexts() {
        return contexts;
    }
    
}
