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
public class CachingMapContainsKeyTest {

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
    public void containsExistingKeyInCache() {
        when(cache.containsKey("key")).thenReturn(true);

        assertTrue(map.containsKey("key"));
        verify(cache).containsKey("key");
        verifyZeroInteractions(backstore);
    }

    @Test
    public void containsExistingKeyInBackstore() {
        when(cache.containsKey("key")).thenReturn(false);
        when(backstore.containsKey("key")).thenReturn(true);

        assertTrue(map.containsKey("key"));
        verify(cache).containsKey("key");
        verify(backstore).containsKey("key");
    }

    @Test
    public void containsNotExistingKey() {
        when(cache.containsKey("key")).thenReturn(false);
        when(backstore.containsKey("key")).thenReturn(false);

        assertFalse(map.containsKey("key"));
        verify(cache).containsKey("key");
        verify(backstore).containsKey("key");
    }

}
