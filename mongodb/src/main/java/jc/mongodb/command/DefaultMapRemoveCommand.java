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

import jc.command.MapRemoveCommand;
import jc.mongodb.MongoMapContext;

import com.mongodb.DBObject;

public class DefaultMapRemoveCommand<K, V> implements MapRemoveCommand<MongoMapContext<K, V>, K, V> {
    
    @Override
    public boolean remove(MongoMapContext<K, V> ctx, K key, V value) {
        DBObject queryObject = ctx.getKeySerializer().toDBObject(key, true, false);
        queryObject.putAll(ctx.getValueSerializer().toDBObject(value, true, false));
        
        return ctx.getCollection().remove(queryObject).getN() > 0;
    }
    
    @Override
    public V remove(MongoMapContext<K, V> ctx, K key) {
        DBObject queryObject = ctx.getKeySerializer().toDBObject(key, true, false);
        DBObject result;
        V old = null;
        
        result = ctx.getCollection().findAndRemove(queryObject);
        if (result != null) {
            old = ctx.getValueSerializer().toElement(result);
        }
        
        return old;
    }
    
}
