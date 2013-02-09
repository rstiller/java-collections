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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MongoConcurrentMapRemoveIT extends AbstractMongoIT {
    
    @Test
    public void removeExisting() {
        DBObjectSerializer<String> keySerializer = new SimpleFieldDBObjectSerializer<String>("key");
        DBObjectSerializer<TestBean> valueSerializer = new JacksonDBObjectSerializer<TestBean>("value", TestBean.class);
        MongoConcurrentMap<String, TestBean> map = new MongoConcurrentMap<String, TestBean>(dbCollection, null,
                keySerializer, valueSerializer);
        TestBean bean = new TestBean("bean1");
        
        assertFalse(map.containsKey("key1"));
        assertNull(map.put("key1", bean));
        assertTrue(map.containsKey("key1"));
        assertEquals(bean, map.get("key1"));
        
        assertEquals(bean, map.remove("key1"));
        assertFalse(map.containsKey("key1"));
        assertNull(map.get("key1"));
    }
    
    @Test
    public void removeNotExisting() {
        DBObjectSerializer<String> keySerializer = new SimpleFieldDBObjectSerializer<String>("key");
        DBObjectSerializer<TestBean> valueSerializer = new JacksonDBObjectSerializer<TestBean>("value", TestBean.class);
        MongoConcurrentMap<String, TestBean> map = new MongoConcurrentMap<String, TestBean>(dbCollection, null,
                keySerializer, valueSerializer);
        
        assertFalse(map.containsKey("key1"));
        assertNull(map.remove("key1"));
        assertFalse(map.containsKey("key1"));
        assertNull(map.get("key1"));
    }
    
}
