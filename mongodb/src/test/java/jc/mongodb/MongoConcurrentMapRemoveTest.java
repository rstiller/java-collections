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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

@RunWith(MockitoJUnitRunner.class)
public class MongoConcurrentMapRemoveTest {
    
    MongoConcurrentMap<String, TestBean> map;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    DBCollection collection;
    @Mock
    DBObjectSerializer<String> keySerializer;
    @Mock
    DBObjectSerializer<TestBean> valueSerializer;
    @Mock
    DBObjectSerializer<Entry<String, TestBean>> entrySerializer;
    @Mock
    DBObject keyQueryObject;
    @Mock
    DBObject valueQueryObject;
    @Mock
    DBObject resultObject;
    
    @Before
    public void createMap() throws UnknownHostException, MongoException {
        map = new MongoConcurrentMap<String, TestBean>(collection, entrySerializer, keySerializer, valueSerializer);
    }
    
    @Test
    public void removeNotExisting() {
        when(keySerializer.toDBObject("key", true, false)).thenReturn(keyQueryObject);
        when(collection.findAndRemove(keyQueryObject)).thenReturn(null);
        
        assertNull(map.remove("key"));
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(collection).findAndRemove(keyQueryObject);
        verify(valueSerializer, never()).toElement(any(DBObject.class));
    }
    
    @Test
    public void removeExisting() {
        TestBean testBean = new TestBean("testBean");
        
        when(keySerializer.toDBObject("key", true, false)).thenReturn(keyQueryObject);
        when(collection.findAndRemove(keyQueryObject)).thenReturn(resultObject);
        when(valueSerializer.toElement(resultObject)).thenReturn(testBean);
        
        assertSame(testBean, map.remove("key"));
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(collection).findAndRemove(keyQueryObject);
        verify(valueSerializer).toElement(resultObject);
    }
    
    @Test
    public void removeValueEqual() {
        TestBean testBean = new TestBean("testBean");
        
        when(keySerializer.toDBObject("key", true, false)).thenReturn(keyQueryObject);
        when(valueSerializer.toDBObject(testBean, true, false)).thenReturn(valueQueryObject);
        when(collection.remove(keyQueryObject).getN()).thenReturn(1);
        
        assertTrue(map.remove("key", testBean));
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(valueSerializer).toDBObject(testBean, true, false);
        verify(collection, times(2)).remove(keyQueryObject);
        verify(keyQueryObject).putAll(valueQueryObject);
    }
    
    @Test
    public void removeValueNotEqual() {
        TestBean testBean = new TestBean("testBean");
        
        when(keySerializer.toDBObject("key", true, false)).thenReturn(keyQueryObject);
        when(valueSerializer.toDBObject(testBean, true, false)).thenReturn(valueQueryObject);
        when(collection.remove(keyQueryObject).getN()).thenReturn(0);
        
        assertFalse(map.remove("key", testBean));
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(valueSerializer).toDBObject(testBean, true, false);
        verify(collection, times(2)).remove(keyQueryObject);
        verify(keyQueryObject).putAll(valueQueryObject);
    }
    
    @Test
    public void removeValueNotExisting() {
        TestBean testBean = new TestBean("testBean");
        
        when(keySerializer.toDBObject("key", true, false)).thenReturn(keyQueryObject);
        when(valueSerializer.toDBObject(testBean, true, false)).thenReturn(valueQueryObject);
        when(collection.remove(keyQueryObject).getN()).thenReturn(0);
        
        assertFalse(map.remove("key", testBean));
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(valueSerializer).toDBObject(testBean, true, false);
        verify(collection, times(2)).remove(keyQueryObject);
        verify(keyQueryObject).putAll(valueQueryObject);
    }
    
}
