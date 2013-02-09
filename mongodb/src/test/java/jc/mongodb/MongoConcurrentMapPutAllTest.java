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

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
public class MongoConcurrentMapPutAllTest {
    
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
    DBObject keyQueryObject;
    @Captor
    ArgumentCaptor<Map<String, TestBean>> dataCaptor;
    @Captor
    ArgumentCaptor<MongoMapContext<String, TestBean>> contextCaptor;
    
    @Before
    public void createMap() throws UnknownHostException, MongoException {
        when(keySerializer.toDBObject(anyString(), anyBoolean(), anyBoolean())).thenReturn(keyQueryObject);
        map = spy(new MongoConcurrentMap<String, TestBean>(collection, entrySerializer, keySerializer, valueSerializer));
        map.setPutCommand(spy(map.getPutCommand()));
    }
    
    @Test
    public void verifyPuts() {
        final TestBean testBean1 = new TestBean("testebean1");
        final TestBean testBean2 = new TestBean("testebean2");
        
        map.putAll(new HashMap<String, TestBean>() {
            
            private static final long serialVersionUID = 6031071434688590652L;
            
            {
                put("key1", testBean1);
                put("key2", testBean2);
            }
            
        });
        
        verify(map.getPutCommand()).putAll(contextCaptor.capture(), dataCaptor.capture());
        assertSame(testBean1, dataCaptor.getValue().get("key1"));
        assertSame(testBean2, dataCaptor.getValue().get("key2"));
    }
    
}
