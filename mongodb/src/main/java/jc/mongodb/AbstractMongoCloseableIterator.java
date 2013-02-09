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

import jc.CloseableIterator;

import com.mongodb.DBCursor;

public abstract class AbstractMongoCloseableIterator<T> implements CloseableIterator<T> {
    
    protected DBCursor cursor;
    protected DBObjectSerializer<T> serializer;
    
    public AbstractMongoCloseableIterator(DBCursor cursor, DBObjectSerializer<T> serializer) {
        this.cursor = cursor;
        this.serializer = serializer;
    }
    
    @Override
    public boolean hasNext() {
        boolean next = cursor.hasNext();
        if (!next) {
            cursor.close();
        }
        return next;
    }
    
    @Override
    public T next() {
        return serializer.toElement(cursor.next());
    }
    
    @Override
    public void remove() {
        cursor.remove();
    }
    
    @Override
    public void close() {
        cursor.close();
    }
    
}
