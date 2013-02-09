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
public class CachingMapIsEmptyTest {

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
    public void isEmptyWithEmptyCache() {
        when(cache.isEmpty()).thenReturn(true);
        when(backstore.isEmpty()).thenReturn(false);

        assertFalse(map.isEmpty());

        verify(cache).isEmpty();
        verify(backstore).isEmpty();
    }

    @Test
    public void isEmptyWithEmptyBackstore() {
        when(cache.isEmpty()).thenReturn(true);
        when(backstore.isEmpty()).thenReturn(true);

        assertTrue(map.isEmpty());

        verify(cache).isEmpty();
        verify(backstore).isEmpty();
    }

    @Test
    public void isEmptyWithFilledCache() {
        when(cache.isEmpty()).thenReturn(false);
        when(backstore.isEmpty()).thenReturn(false);

        assertFalse(map.isEmpty());

        verify(cache).isEmpty();
        verifyZeroInteractions(backstore);
    }

}
