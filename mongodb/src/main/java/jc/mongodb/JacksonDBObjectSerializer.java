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

import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class JacksonDBObjectSerializer<E> implements DBObjectSerializer<E> {
    
    ObjectMapper mapper;
    String field;
    Class<E> type;
    
    public JacksonDBObjectSerializer(String field, Class<E> type) {
        this.field = field;
        this.type = type;
        mapper = new ObjectMapper();
    }
    
    public JacksonDBObjectSerializer(String field, Class<E> type, ObjectMapper mapper) {
        this.field = field;
        this.type = type;
        this.mapper = mapper;
    }
    
    @Override
    public DBObject toDBObject(E element, boolean equalFunctions, boolean negate) {
        if (equalFunctions && negate) {
            return new BasicDBObject(field, new BasicDBObject("$ne", mapper.convertValue(element, HashMap.class)));
        }
        return new BasicDBObject(field, mapper.convertValue(element, HashMap.class));
    }
    
    @Override
    public E toElement(DBObject dbObject) {
        Object obj;
        
        if (dbObject.containsField(field) && (obj = dbObject.get(field)) != null && obj instanceof DBObject) {
            return mapper.convertValue(((DBObject) obj).toMap(), type);
        }
        
        return null;
    }
    
}
