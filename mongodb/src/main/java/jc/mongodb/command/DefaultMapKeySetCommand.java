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

import jc.command.MapKeySetCommand;
import jc.mongodb.MongoMapContext;
import jc.mongodb.MongoSet;

public class DefaultMapKeySetCommand<K, V> implements MapKeySetCommand<MongoMapContext<K, V>, K> {
    
    @Override
    public MongoSet<K> keySet(MongoMapContext<K, V> ctx) {
        return new MongoSet<K>(ctx.getCollection(), ctx.getKeySerializer());
    }
    
}
