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

import jc.ContextCollection;
import jc.mongodb.command.DefaultAddCommand;
import jc.mongodb.command.DefaultClearCommand;
import jc.mongodb.command.DefaultContainsCommand;
import jc.mongodb.command.DefaultIsEmptyCommand;
import jc.mongodb.command.DefaultIteratorCommand;
import jc.mongodb.command.DefaultRemoveCommand;
import jc.mongodb.command.DefaultSizeCommand;

import com.mongodb.DBCollection;

public class MongoCollection<T> extends ContextCollection<MongoContext<T>, T> {
    
    public MongoCollection(final DBCollection collection, final DBObjectSerializer<T> serializer) {
        contexts = new ThreadLocal<MongoContext<T>>() {
            
            @Override
            protected MongoContext<T> initialValue() {
                MongoContext<T> ctx = new MongoContext<T>();
                
                ctx.setCollection(collection);
                ctx.setSerializer(serializer);
                
                return ctx;
            }
            
        };
        
        sizeCommand = new DefaultSizeCommand<>();
        isEmptyCommand = new DefaultIsEmptyCommand<>();
        containsCommand = new DefaultContainsCommand<>();
        clearCommand = new DefaultClearCommand<>();
        iteratorCommand = new DefaultIteratorCommand<>();
        removeCommand = new DefaultRemoveCommand<>();
        addCommand = new DefaultAddCommand<>();
    }
    
    @Override
    protected ThreadLocal<MongoContext<T>> getContexts() {
        return contexts;
    }
    
}
