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

import jc.ContextQueue;
import jc.mongodb.command.DefaultAddCommand;
import jc.mongodb.command.DefaultClearCommand;
import jc.mongodb.command.DefaultContainsCommand;
import jc.mongodb.command.DefaultIsEmptyCommand;
import jc.mongodb.command.DefaultIteratorCommand;
import jc.mongodb.command.DefaultQueueElementCommand;
import jc.mongodb.command.DefaultQueueOfferCommand;
import jc.mongodb.command.DefaultQueuePeekCommand;
import jc.mongodb.command.DefaultQueuePollCommand;
import jc.mongodb.command.DefaultQueueRemoveCommand;
import jc.mongodb.command.DefaultRemoveCommand;
import jc.mongodb.command.DefaultSizeCommand;

import com.mongodb.DBCollection;

public class MongoQueue<E> extends ContextQueue<MongoContext<E>, E> {
    
    protected DBObjectSerializer<E> serializer;
    protected DBCollection collection;
    
    public MongoQueue(final DBCollection collection, final DBObjectSerializer<E> serializer) {
        this.collection = collection;
        this.serializer = serializer;
        
        contexts = new ThreadLocal<MongoContext<E>>() {
            
            @Override
            protected MongoContext<E> initialValue() {
                MongoContext<E> ctx = new MongoContext<E>();
                
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
        queuePollCommand = new DefaultQueuePollCommand<>(true, isEmptyCommand);
        queueRemoveCommand = new DefaultQueueRemoveCommand<>(isEmptyCommand, queuePollCommand);
        queuePeekCommand = new DefaultQueuePeekCommand<>(true, isEmptyCommand);
        queueOfferCommand = new DefaultQueueOfferCommand<>(addCommand);
        queueElementCommand = new DefaultQueueElementCommand<>(isEmptyCommand, queuePeekCommand);
    }
    
    @Override
    protected ThreadLocal<MongoContext<E>> getContexts() {
        return contexts;
    }
    
}
