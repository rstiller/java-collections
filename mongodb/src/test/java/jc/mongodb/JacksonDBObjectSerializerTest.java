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

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class JacksonDBObjectSerializerTest {

    JacksonDBObjectSerializer<TestBean> serializer;

    @Before
    public void createSerializer() {
        serializer = new JacksonDBObjectSerializer<TestBean>("field", TestBean.class);
    }

    @Test
    public void toElement() {
        BasicDBObject obj;
        TestBean bean;

        obj = new BasicDBObject();
        obj.put("name", "testbean");
        obj.put("count", 123);

        bean = serializer.toElement(new BasicDBObject("field", obj));

        assertEquals("testbean", bean.getName());
        assertEquals(123, bean.getCount());
    }

    @Test
    public void toDBObject() throws Exception {
        DBObject obj;
        TestBean bean;
        ObjectWriter mapper = new ObjectMapper().writer().without(SerializationFeature.WRITE_NULL_MAP_VALUES);

        bean = new TestBean("testbean");
        bean.setCount(123);

        obj = serializer.toDBObject(bean, true, false);
        assertEquals("{\"field\":{\"count\":123,\"beans\":[],\"name\":\"testbean\"}}",
                mapper.writeValueAsString(obj.toMap()));

        obj = serializer.toDBObject(bean, true, true);
        assertEquals("{\"field\":{\"$ne\":{\"count\":123,\"beans\":[],\"name\":\"testbean\"}}}",
                mapper.writeValueAsString(obj.toMap()));

        obj = serializer.toDBObject(bean, false, false);
        assertEquals("{\"field\":{\"count\":123,\"beans\":[],\"name\":\"testbean\"}}",
                mapper.writeValueAsString(obj.toMap()));

        obj = serializer.toDBObject(bean, false, true);
        assertEquals("{\"field\":{\"count\":123,\"beans\":[],\"name\":\"testbean\"}}",
                mapper.writeValueAsString(obj.toMap()));
    }

}
