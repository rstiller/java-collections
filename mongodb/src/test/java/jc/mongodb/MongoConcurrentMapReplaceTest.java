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
public class MongoConcurrentMapReplaceTest {
    
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
    DBObject keyResultObject;
    @Mock
    DBObject valueResultObject;
    @Mock
    DBObject resultObject;
    
    @Before
    public void createMap() throws UnknownHostException, MongoException {
        map = new MongoConcurrentMap<String, TestBean>(collection, entrySerializer, keySerializer, valueSerializer);
    }
    
    @Test
    public void replaceValueEqual() {
        TestBean testBean = new TestBean("testBean");
        TestBean otherBean = new TestBean("otherBean");
        
        when(keySerializer.toDBObject("key", true, false)).thenReturn(keyQueryObject);
        when(keySerializer.toDBObject("key", false, false)).thenReturn(keyResultObject);
        when(valueSerializer.toDBObject(testBean, false, false)).thenReturn(valueResultObject);
        when(valueSerializer.toElement(resultObject)).thenReturn(otherBean);
        when(collection.findAndModify(keyQueryObject, null, null, false, keyResultObject, false, false)).thenReturn(
                resultObject);
        
        assertSame(otherBean, map.replace("key", testBean));
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(keySerializer).toDBObject("key", false, false);
        verify(valueSerializer).toDBObject(testBean, false, false);
        verify(valueSerializer).toElement(resultObject);
        verify(collection).findAndModify(keyQueryObject, null, null, false, keyResultObject, false, false);
        verify(keyResultObject).putAll(valueResultObject);
    }
    
    @Test
    public void replaceValueNotEqual() {
        TestBean testBean = new TestBean("testBean");
        
        when(keySerializer.toDBObject("key", true, false)).thenReturn(keyQueryObject);
        when(keySerializer.toDBObject("key", false, false)).thenReturn(keyResultObject);
        when(valueSerializer.toDBObject(testBean, false, false)).thenReturn(valueResultObject);
        when(collection.findAndModify(keyQueryObject, null, null, false, keyResultObject, false, false)).thenReturn(
                null);
        
        assertNull(map.replace("key", testBean));
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(keySerializer).toDBObject("key", false, false);
        verify(valueSerializer).toDBObject(testBean, false, false);
        verify(valueSerializer, never()).toElement(resultObject);
        verify(collection).findAndModify(keyQueryObject, null, null, false, keyResultObject, false, false);
        verify(keyResultObject).putAll(valueResultObject);
    }
    
    @Test
    public void replaceValueNotMatchingQuery() {
        TestBean newValue = new TestBean("newValue");
        TestBean oldValue = new TestBean("oldValue");
        
        when(keySerializer.toDBObject("key", true, false)).thenReturn(keyQueryObject);
        when(keySerializer.toDBObject("key", false, false)).thenReturn(keyResultObject);
        when(valueSerializer.toDBObject(oldValue, true, false)).thenReturn(valueQueryObject);
        when(valueSerializer.toDBObject(newValue, false, false)).thenReturn(valueResultObject);
        when(collection.update(keyQueryObject, keyResultObject).getN()).thenReturn(0);
        
        assertFalse(map.replace("key", oldValue, newValue));
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(keySerializer).toDBObject("key", false, false);
        verify(valueSerializer).toDBObject(oldValue, true, false);
        verify(valueSerializer).toDBObject(newValue, false, false);
        verify(collection, times(2)).update(keyQueryObject, keyResultObject);
        verify(keyQueryObject).putAll(valueQueryObject);
        verify(keyResultObject).putAll(valueResultObject);
    }
    
    @Test
    public void replaceMatchingQuery() {
        TestBean newValue = new TestBean("newValue");
        TestBean oldValue = new TestBean("oldValue");
        
        when(keySerializer.toDBObject("key", true, false)).thenReturn(keyQueryObject);
        when(keySerializer.toDBObject("key", false, false)).thenReturn(keyResultObject);
        when(valueSerializer.toDBObject(oldValue, true, false)).thenReturn(valueQueryObject);
        when(valueSerializer.toDBObject(newValue, false, false)).thenReturn(valueResultObject);
        when(collection.update(keyQueryObject, keyResultObject).getN()).thenReturn(1);
        
        assertTrue(map.replace("key", oldValue, newValue));
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(keySerializer).toDBObject("key", false, false);
        verify(valueSerializer).toDBObject(oldValue, true, false);
        verify(valueSerializer).toDBObject(newValue, false, false);
        verify(collection, times(2)).update(keyQueryObject, keyResultObject);
        verify(keyQueryObject).putAll(valueQueryObject);
        verify(keyResultObject).putAll(valueResultObject);
    }
    
}
