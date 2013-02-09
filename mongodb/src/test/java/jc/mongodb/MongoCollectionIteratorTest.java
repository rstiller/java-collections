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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import jc.CloseableIterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@RunWith(MockitoJUnitRunner.class)
public class MongoCollectionIteratorTest {
    
    MongoCollection<String> collection;
    @Mock
    DBObjectSerializer<String> serializer;
    @Mock
    DBCollection dbCollection;
    @Mock
    DBObject serializerResult;
    @Mock
    DBCursor cursor;
    @Mock
    DBObject cursorResult;
    
    @Before
    public void createCollection() {
        collection = new MongoCollection<String>(dbCollection, serializer);
    }
    
    @Test
    public void iterator() {
        CloseableIterator<String> iter;
        
        when(dbCollection.find()).thenReturn(cursor);
        when(serializer.toElement(cursorResult)).thenReturn("value");
        
        iter = collection.iterator();
        
        when(cursor.hasNext()).thenReturn(true);
        when(cursor.next()).thenReturn(cursorResult);
        
        assertTrue(iter.hasNext());
        assertEquals("value", iter.next());
        
        verify(cursor).hasNext();
        verify(cursor).next();
        verify(serializer).toElement(cursorResult);
        
        iter.remove();
        
        verify(cursor).remove();
        
        assertTrue(iter.hasNext());
        assertEquals("value", iter.next());
        
        verify(cursor, times(2)).hasNext();
        verify(cursor, times(2)).next();
        verify(serializer, times(2)).toElement(cursorResult);
        
        iter.close();
        
        verify(cursor).close();
        
        when(cursor.hasNext()).thenReturn(false);
        
        assertFalse(iter.hasNext());
        
        verify(dbCollection).find();
    }
    
}
