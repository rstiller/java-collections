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

import jc.command.SizeCommand;
import jc.mongodb.MongoMapContext;

public class DefaultMapSizeCommand<K, V> implements SizeCommand<MongoMapContext<K, V>> {
    
    @Override
    public int size(MongoMapContext<K, V> ctx) {
        return (int) ctx.getCollection().count(ctx.getFilter());
    }
    
}