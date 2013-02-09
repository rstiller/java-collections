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
import jc.command.QueuePollCommand;
import jc.command.QueueRemoveCommand;
import jc.mongodb.MongoContext;


public class DefaultQueueRemoveCommand<T> implements QueueRemoveCommand<MongoContext<T>, T> {
    
    IsEmptyCommand<MongoContext<T>> isEmptyCommand;
    QueuePollCommand<MongoContext<T>, T> queuePollCommand;
    
    public DefaultQueueRemoveCommand(IsEmptyCommand<MongoContext<T>> isEmptyCommand,
            QueuePollCommand<MongoContext<T>, T> queuePollCommand) {
        this.isEmptyCommand = isEmptyCommand;
        this.queuePollCommand = queuePollCommand;
    }
    
    @Override
    public T remove(MongoContext<T> ctx) {
        if (isEmptyCommand.isEmpty(ctx)) {
            throw new NoSuchElementException();
        }
        return queuePollCommand.poll(ctx);
    }
    
}
