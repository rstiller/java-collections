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

import jc.command.IsEmptyCommand;
import jc.command.QueuePeekCommand;
import jc.mongodb.MongoContext;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;

public class DefaultQueuePeekCommand<T> implements QueuePeekCommand<MongoContext<T>, T> {
    
    private static final BasicDBObject ORDER_BY_ASC = new BasicDBObject("_id", 1);
    private static final BasicDBObject ORDER_BY_DESC = new BasicDBObject("_id", -1);
    
    boolean asc;
    IsEmptyCommand<MongoContext<T>> isEmptyCommand;
    
    public DefaultQueuePeekCommand(boolean asc, IsEmptyCommand<MongoContext<T>> isEmptyCommand) {
        this.asc = asc;
        this.isEmptyCommand = isEmptyCommand;
    }
    
    @Override
    public T peek(MongoContext<T> ctx) {
        DBCursor cursor;
        
        if (isEmptyCommand.isEmpty(ctx)) {
            return null;
        }
        
        cursor = ctx.getCollection().find().sort(asc ? ORDER_BY_ASC : ORDER_BY_DESC);
        try {
            if (cursor.hasNext()) {
                return ctx.getSerializer().toElement(cursor.next());
            }
            return null;
        } finally {
            cursor.close();
        }
    }
    
}
