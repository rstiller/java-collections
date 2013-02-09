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

import jc.command.MapReplaceCommand;
import jc.mongodb.MongoMapContext;

import com.mongodb.DBObject;

public class DefaultMapReplaceCommand<K, V> implements MapReplaceCommand<MongoMapContext<K, V>, K, V> {
    
    @Override
    public boolean replace(MongoMapContext<K, V> ctx, K key, V oldValue, V newValue) {
        DBObject queryObject = ctx.getKeySerializer().toDBObject(key, true, false);
        DBObject dbObject = ctx.getKeySerializer().toDBObject(key, false, false);
        
        queryObject.putAll(ctx.getValueSerializer().toDBObject(oldValue, true, false));
        dbObject.putAll(ctx.getValueSerializer().toDBObject(newValue, false, false));
        
        return ctx.getCollection().update(queryObject, dbObject).getN() > 0;
    }
    
    @Override
    public V replace(MongoMapContext<K, V> ctx, K key, V value) {
        DBObject dbObject;
        DBObject queryObject;
        V old = null;
        
        queryObject = ctx.getKeySerializer().toDBObject(key, true, false);
        dbObject = ctx.getKeySerializer().toDBObject(key, false, false);
        dbObject.putAll(ctx.getValueSerializer().toDBObject(value, false, false));
        
        dbObject = ctx.getCollection().findAndModify(queryObject, null, null, false, dbObject, false, false);
        if (dbObject != null) {
            old = ctx.getValueSerializer().toElement(dbObject);
        }
        
        return old;
    }
    
}
