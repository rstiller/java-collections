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

import java.util.Map.Entry;

import jc.command.MapEntrySetCommand;
import jc.mongodb.MongoMapContext;
import jc.mongodb.MongoSet;


public class DefaultMapEntrySetCommand<K, V> implements MapEntrySetCommand<MongoMapContext<K, V>, K, V> {
    
    @Override
    public MongoSet<Entry<K, V>> entrySet(MongoMapContext<K, V> ctx) {
        return new MongoSet<Entry<K, V>>(ctx.getCollection(), ctx.getEntrySerializer());
    }
    
}
