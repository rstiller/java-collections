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
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

@RunWith(MockitoJUnitRunner.class)
public class MongoConcurrentMapContainsKeyTest {
    
    MongoConcurrentMap<String, TestBean> map;
    @Mock
    DBCollection collection;
    @Mock
    DBObjectSerializer<String> keySerializer;
    @Mock
    DBObject queryObject;
    @Mock
    DBObject resultObject;
    @Captor
    ArgumentCaptor<String> keyCaptor;
    
    @Before
    public void createMap() throws UnknownHostException, MongoException {
        when(keySerializer.toDBObject(anyString(), anyBoolean(), anyBoolean())).thenReturn(queryObject);
        map = new MongoConcurrentMap<String, TestBean>(collection, null, keySerializer, null);
    }
    
    @Test
    public void keyNotContained() {
        when(collection.findOne(queryObject)).thenReturn(null);
        
        assertFalse(map.containsKey("key"));
        
        verify(keySerializer).toDBObject(keyCaptor.capture(), eq(true), eq(false));
        assertEquals("key", keyCaptor.getValue());
    }
    
    @Test
    public void keyContained() {
        when(collection.count(queryObject)).thenReturn(1L);
        
        assertTrue(map.containsKey("key"));
        
        verify(keySerializer).toDBObject(keyCaptor.capture(), eq(true), eq(false));
        assertEquals("key", keyCaptor.getValue());
    }
    
}
