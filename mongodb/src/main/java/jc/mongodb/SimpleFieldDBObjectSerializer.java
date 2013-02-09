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

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


public class SimpleFieldDBObjectSerializer<E> implements DBObjectSerializer<E> {
    
    String field;
    
    public SimpleFieldDBObjectSerializer(String field) {
        this.field = field;
    }
    
    @Override
    public DBObject toDBObject(E element, boolean equalFunctions, boolean negate) {
        if (equalFunctions && negate) {
            return new BasicDBObject(field, new BasicDBObject("$ne", element));
        }
        return new BasicDBObject(field, element);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public E toElement(DBObject dbObject) {
        return (E) dbObject.get(field);
    }
    
}
