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

import jc.command.ContainsCommand;
import jc.mongodb.MongoMapContext;

public class DefaultMapContainsKeyCommand<K, V> implements ContainsCommand<MongoMapContext<K, V>, K> {
    
    boolean equalFunctions = true;
    boolean negate = false;
    
    public DefaultMapContainsKeyCommand() {
    }
    
    public DefaultMapContainsKeyCommand(boolean equalFunctions, boolean negate) {
        this.equalFunctions = equalFunctions;
        this.negate = negate;
    }
    
    @Override
    public boolean contains(MongoMapContext<K, V> ctx, K object) {
        return ctx.getCollection().count(ctx.getKeySerializer().toDBObject(object, equalFunctions, negate)) > 0;
    }
    
}
