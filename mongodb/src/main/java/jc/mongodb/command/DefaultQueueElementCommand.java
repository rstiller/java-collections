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

import java.util.NoSuchElementException;

import jc.command.IsEmptyCommand;
import jc.command.QueueElementCommand;
import jc.command.QueuePeekCommand;
import jc.mongodb.MongoContext;


public class DefaultQueueElementCommand<T> implements QueueElementCommand<MongoContext<T>, T> {
    
    IsEmptyCommand<MongoContext<T>> isEmptyCommand;
    QueuePeekCommand<MongoContext<T>, T> queuePeekCommand;
    
    public DefaultQueueElementCommand(IsEmptyCommand<MongoContext<T>> isEmptyCommand,
            QueuePeekCommand<MongoContext<T>, T> queuePeekCommand) {
        this.isEmptyCommand = isEmptyCommand;
        this.queuePeekCommand = queuePeekCommand;
    }
    
    @Override
    public T element(MongoContext<T> ctx) {
        if (isEmptyCommand.isEmpty(ctx)) {
            throw new NoSuchElementException();
        }
        return queuePeekCommand.peek(ctx);
    }
    
}
