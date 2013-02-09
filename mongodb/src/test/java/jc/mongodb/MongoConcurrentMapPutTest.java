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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

@RunWith(MockitoJUnitRunner.class)
public class MongoConcurrentMapPutTest {
    
    MongoConcurrentMap<String, TestBean> map;
    @Mock
    DBCollection collection;
    @Mock
    DBObjectSerializer<String> keySerializer;
    @Mock
    DBObjectSerializer<TestBean> valueSerializer;
    @Mock
    DBObjectSerializer<Entry<String, TestBean>> entrySerializer;
    @Mock
    DBObject queryDBObject;
    @Mock
    DBObject keyDBObject;
    @Mock
    DBObject valueDBObject;
    @Mock
    DBObject oldDBObject;
    
    @Before
    public void createMap() throws UnknownHostException, MongoException {
        map = new MongoConcurrentMap<String, TestBean>(collection, entrySerializer, keySerializer, valueSerializer);
    }
    
    @Test
    public void putNotExisting() {
        TestBean testBean = new TestBean("testBean");
        
        when(keySerializer.toDBObject("key", true, false)).thenReturn(queryDBObject);
        when(keySerializer.toDBObject("key", false, false)).thenReturn(keyDBObject);
        when(valueSerializer.toDBObject(testBean, false, false)).thenReturn(valueDBObject);
        when(collection.findAndModify(queryDBObject, null, null, false, keyDBObject, false, true)).thenReturn(null);
        
        assertNull(map.put("key", testBean));
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(keySerializer).toDBObject("key", false, false);
        verify(valueSerializer).toDBObject(testBean, false, false);
        verify(valueSerializer, never()).toElement(any(DBObject.class));
        verify(collection).findAndModify(queryDBObject, null, null, false, keyDBObject, false, true);
        verify(keyDBObject).putAll(valueDBObject);
    }
    
    @Test
    public void replaceExisting() {
        TestBean testBean = new TestBean("testBean");
        TestBean old;
        
        when(keySerializer.toDBObject("key", true, false)).thenReturn(queryDBObject);
        when(keySerializer.toDBObject("key", false, false)).thenReturn(keyDBObject);
        when(valueSerializer.toDBObject(testBean, false, false)).thenReturn(valueDBObject);
        when(valueSerializer.toElement(oldDBObject)).thenReturn(testBean);
        when(collection.findAndModify(queryDBObject, null, null, false, keyDBObject, false, true)).thenReturn(
                oldDBObject);
        
        old = map.put("key", testBean);
        assertSame(testBean, old);
        
        verify(keySerializer).toDBObject("key", true, false);
        verify(keySerializer).toDBObject("key", false, false);
        verify(valueSerializer).toDBObject(testBean, false, false);
        verify(valueSerializer).toElement(oldDBObject);
        verify(collection).findAndModify(queryDBObject, null, null, false, keyDBObject, false, true);
        verify(keyDBObject).putAll(valueDBObject);
    }
    
}
