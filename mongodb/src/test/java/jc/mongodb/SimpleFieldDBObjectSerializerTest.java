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
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBObject;

@RunWith(MockitoJUnitRunner.class)
public class SimpleFieldDBObjectSerializerTest {

    SimpleFieldDBObjectSerializer<String> serializer;
    @Mock
    DBObject dbObject;

    @Before
    public void createSerializer() {
        serializer = new SimpleFieldDBObjectSerializer<String>("field");
    }

    @Test
    public void toElement() {
        when(dbObject.get("field")).thenReturn("value");
        assertEquals("value", serializer.toElement(dbObject));

        when(dbObject.get("field")).thenReturn(null);
        assertNull(serializer.toElement(dbObject));
    }

    @Test
    public void toDBObject() throws Exception {
        DBObject obj;

        obj = serializer.toDBObject("value", true, true);
        assertEquals("{\"field\":{\"$ne\":\"value\"}}", new ObjectMapper().writeValueAsString(obj.toMap()));

        obj = serializer.toDBObject("value", true, false);
        assertEquals("{\"field\":\"value\"}", new ObjectMapper().writeValueAsString(obj.toMap()));

        obj = serializer.toDBObject("value", false, true);
        assertEquals("{\"field\":\"value\"}", new ObjectMapper().writeValueAsString(obj.toMap()));

        obj = serializer.toDBObject("value", false, false);
        assertEquals("{\"field\":\"value\"}", new ObjectMapper().writeValueAsString(obj.toMap()));
    }

}
