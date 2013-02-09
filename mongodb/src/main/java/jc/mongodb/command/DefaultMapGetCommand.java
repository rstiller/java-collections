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

import jc.command.MapGetCommand;
import jc.mongodb.MongoMapContext;

import com.mongodb.DBObject;

public class DefaultMapGetCommand<K, V> implements MapGetCommand<MongoMapContext<K, V>, K, V> {
    
    boolean equalFunctions = true;
    boolean negate = false;
    
    public DefaultMapGetCommand() {
    }
    
    public DefaultMapGetCommand(boolean equalFunctions, boolean negate) {
        this.equalFunctions = equalFunctions;
        this.negate = negate;
    }
    
    @Override
    public V get(MongoMapContext<K, V> ctx, K key) {
        DBObject result = ctx.getCollection().findOne(ctx.getKeySerializer().toDBObject(key, equalFunctions, negate));
        return result != null ? ctx.getValueSerializer().toElement(result) : null;
    }
    
}
