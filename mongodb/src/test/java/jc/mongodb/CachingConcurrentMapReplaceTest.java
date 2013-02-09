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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.MongoException;

@RunWith(MockitoJUnitRunner.class)
public class CachingConcurrentMapReplaceTest {

    CachingConcurrentMap<String, String> map;
    @Mock
    ConcurrentMap<String, String> cache;
    @Mock
    MongoConcurrentMap<String, String> backstore;

    @Before
    public void createMap() throws UnknownHostException, MongoException {
        map = new CachingConcurrentMap<String, String>(cache, backstore);
    }

    @Test
    public void replaceEqual() {
        when(cache.containsKey("key")).thenReturn(true);
        when(cache.get("key")).thenReturn("oldValue");

        assertTrue(map.replace("key", "oldValue", "newValue"));

        verify(cache, times(2)).containsKey("key");
        verify(cache).get("key");
        verify(cache).put("key", "newValue");
        verify(backstore).put("key", "newValue");
    }

    @Test
    public void replaceNotEqual() {
        when(cache.containsKey("key")).thenReturn(true);
        when(cache.get("key")).thenReturn("otherValue");

        assertFalse(map.replace("key", "oldValue", "newValue"));

        verify(cache, times(2)).containsKey("key");
        verify(cache).get("key");
        verify(cache, never()).put("key", "newValue");
        verify(backstore, never()).put("key", "newValue");
    }

    @Test
    public void replaceNotExisting() {
        when(cache.containsKey("key")).thenReturn(false);
        when(backstore.containsKey("key")).thenReturn(false);

        assertFalse(map.replace("key", "oldValue", "newValue"));

        verify(cache).containsKey("key");
        verify(backstore).containsKey("key");
        verify(cache, never()).get("key");
        verify(backstore, never()).get("key");
        verify(cache, never()).put("key", "newValue");
        verify(backstore, never()).put("key", "newValue");
    }

}
