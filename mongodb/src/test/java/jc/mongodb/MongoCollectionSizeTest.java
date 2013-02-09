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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@RunWith(MockitoJUnitRunner.class)
public class MongoCollectionSizeTest {
    
    MongoCollection<String> collection;
    @Mock
    DBObjectSerializer<String> serializer;
    @Mock
    DBCollection dbCollection;
    
    @Before
    public void createCollection() {
        collection = new MongoCollection<String>(dbCollection, serializer);
    }
    
    @Test
    public void size() {
        when(dbCollection.count(any(DBObject.class))).thenReturn(123L);
        
        assertEquals(123, collection.size());
        
        verify(dbCollection).count(any(DBObject.class));
    }
}
