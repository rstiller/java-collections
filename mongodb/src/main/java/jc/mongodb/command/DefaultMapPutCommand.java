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
package jc.mongodb.command;

import java.util.Map;

import jc.command.ContainsCommand;
import jc.command.MapGetCommand;
import jc.command.MapPutCommand;
import jc.mongodb.MongoMapContext;


import com.mongodb.DBObject;

public class DefaultMapPutCommand<K, V> implements MapPutCommand<MongoMapContext<K, V>, K, V> {
    
    MapGetCommand<MongoMapContext<K, V>, K, V> getCommand;
    ContainsCommand<MongoMapContext<K, V>, K> containsCommand;
    
    public DefaultMapPutCommand() {
    }
    
    public DefaultMapPutCommand(MapGetCommand<MongoMapContext<K, V>, K, V> getCommand,
            ContainsCommand<MongoMapContext<K, V>, K> containsCommand) {
        this.getCommand = getCommand;
        this.containsCommand = containsCommand;
    }
    
    @Override
    public V put(MongoMapContext<K, V> ctx, K key, V value) {
        return put(ctx, key, value, true, false);
    }
    
    @Override
    public void putAll(MongoMapContext<K, V> ctx, Map<K, V> data) {
        for (Map.Entry<K, V> entry : data.entrySet()) {
            put(ctx, entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public V putIfAbsent(MongoMapContext<K, V> ctx, K key, V value) {
        if (!containsCommand.contains(ctx, key)) {
            return put(ctx, key, value, true, true);
        }
        return getCommand.get(ctx, key);
    }
    
    public V put(MongoMapContext<K, V> ctx, K key, V value, boolean insertIfAbsent, boolean returnNew) {
        DBObject dbObject;
        DBObject queryObject;
        V old = null;
        
        queryObject = ctx.getKeySerializer().toDBObject(key, true, false);
        dbObject = ctx.getKeySerializer().toDBObject(key, false, false);
        dbObject.putAll(ctx.getValueSerializer().toDBObject(value, false, false));
        
        dbObject = ctx.getCollection().findAndModify(queryObject, null, null, false, dbObject, returnNew,
                insertIfAbsent);
        if (dbObject != null) {
            old = ctx.getValueSerializer().toElement(dbObject);
        }
        
        return old;
    }
    
}
