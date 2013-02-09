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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@RunWith(MockitoJUnitRunner.class)
public class MongoCollectionAddAllTest {

    MongoCollection<String> collection;
    @Mock
    DBObjectSerializer<String> serializer;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    DBCollection dbCollection;
    @Mock
    DBObject serializerResult;

    @Before
    public void createCollection() {
        collection = new MongoCollection<String>(dbCollection, serializer);
    }

    @Test
    public void addAll() {
        when(serializer.toDBObject(anyString(), eq(false), eq(false))).thenReturn(serializerResult);
        when(dbCollection.insert(serializerResult).getN()).thenReturn(1);

        assertTrue(collection.addAll(Arrays.asList("value1", "value2")));

        verify(serializer).toDBObject("value1", false, false);
        verify(serializer).toDBObject("value2", false, false);
        verify(dbCollection, times(3)).insert(serializerResult);
    }

    @Test
    public void addAllFailed() {
        when(serializer.toDBObject(anyString(), eq(false), eq(false))).thenReturn(serializerResult);
        when(dbCollection.insert(serializerResult).getN()).thenReturn(0);

        assertFalse(collection.addAll(Arrays.asList("value1", "value2")));

        verify(serializer).toDBObject("value1", false, false);
        verify(serializer).toDBObject("value2", false, false);
        verify(dbCollection, times(3)).insert(serializerResult);
    }

}
