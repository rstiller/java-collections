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

import java.util.Locale;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class MongoLocaleSerializer implements DBObjectSerializer<Locale> {
    
    String field;
    
    public MongoLocaleSerializer(String field) {
        this.field = field;
    }
    
    @Override
    public DBObject toDBObject(Locale element, boolean equalFunctions, boolean negate) {
        DBObject dbObject = new BasicDBObject(field, element.toString());
        
        if (equalFunctions && negate) {
            dbObject = new BasicDBObject("$ne", dbObject);
        }
        
        return dbObject;
    }
    
    @Override
    public Locale toElement(DBObject dbObject) {
        return new Locale((String) dbObject.get(field));
    }
    
}
