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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.net.UnknownHostException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mongodb.MongoException;

@RunWith(MockitoJUnitRunner.class)
public class CachingMapGetTest {

    CachingMap<String, String> map;
    @Mock
    Map<String, String> cache;
    @Mock
    MongoConcurrentMap<String, String> backstore;

    @Before
    public void createMap() throws UnknownHostException, MongoException {
        map = new CachingMap<String, String>(cache, backstore);
    }

    @Test
    public void getExistingFromCache() {
        when(cache.containsKey("key")).thenReturn(true);
        when(cache.get("key")).thenReturn("value");
        when(backstore.containsKey("key")).thenReturn(true);
        when(backstore.get("key")).thenReturn("value");

        assertEquals("value", map.get("key"));

        verify(cache).containsKey("key");
        verify(cache).get("key");
        verifyZeroInteractions(backstore);
    }

    @Test
    public void getExistingFromBackstore() {
        when(cache.containsKey("key")).thenReturn(false);
        when(cache.get("key")).thenReturn(null);
        when(backstore.containsKey("key")).thenReturn(true);
        when(backstore.get("key")).thenReturn("value");

        assertEquals("value", map.get("key"));

        verify(cache).containsKey("key");
        verify(cache, never()).get("key");
        verify(backstore).containsKey("key");
        verify(backstore).get("key");
    }

    @Test
    public void getNotExisting() {
        when(cache.containsKey("key")).thenReturn(false);
        when(cache.get("key")).thenReturn(null);
        when(backstore.containsKey("key")).thenReturn(false);
        when(backstore.get("key")).thenReturn(null);

        assertNull(map.get("key"));

        verify(cache).containsKey("key");
        verify(cache, never()).get("key");
        verify(backstore).containsKey("key");
        verify(backstore, never()).get("key");
    }

}
