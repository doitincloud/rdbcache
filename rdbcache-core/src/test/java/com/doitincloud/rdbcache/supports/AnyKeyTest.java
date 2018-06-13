/*
 *  Copyright 2017-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.doitincloud.rdbcache.supports;

import com.doitincloud.rdbcache.configs.AppCtx;
import com.doitincloud.rdbcache.models.KeyInfo;

import com.doitincloud.rdbcache.services.CacheOps;
import com.doitincloud.rdbcache.supports.AnyKey;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnyKeyTest {

    @Test
    public void setKey() {

        AnyKey anyKey;
        KeyInfo keyInfo;

        anyKey = new AnyKey();
        keyInfo = new KeyInfo();
        keyInfo.setExpire("100");
        keyInfo.setTable("table");
        anyKey.setKeyInfo(keyInfo);

        assertEquals(1, anyKey.size());
        assertTrue(keyInfo == anyKey.get(0));

        anyKey = new AnyKey(new KeyInfo());
        anyKey.setKeyInfo(keyInfo);
        assertEquals(1, anyKey.size());
        assertTrue(keyInfo == anyKey.get(0));
    }

    @Test
    public void getKey() {

        AnyKey anyKey;
        KeyInfo keyInfo;

        anyKey = new AnyKey();

        keyInfo = anyKey.getKeyInfo();
        assertNull(keyInfo);

        keyInfo = new KeyInfo();
        keyInfo.setExpire("100");
        keyInfo.setTable("table");
        anyKey = new AnyKey(keyInfo);

        KeyInfo keyInfo2 = anyKey.getKeyInfo();

        assertNotNull(keyInfo2);
        assertTrue(keyInfo == keyInfo2);
    }

    @Test
    public void getAny() {

        AnyKey anyKey;
        KeyInfo keyInfo;

        anyKey = new AnyKey();

        keyInfo = anyKey.getKeyInfo();
        assertNull(keyInfo);

        keyInfo = new KeyInfo();
        keyInfo.setExpire("100");
        keyInfo.setTable("table");
        anyKey = new AnyKey(keyInfo);
        KeyInfo keyInfo2 = anyKey.getKeyInfo();

        assertNotNull(keyInfo2);
        assertTrue(keyInfo == keyInfo2);

        keyInfo2 = anyKey.get(0);
        assertNotNull(keyInfo2);
        assertTrue(keyInfo == keyInfo2);

        CacheOps cacheOps = new CacheOps();
        cacheOps.handleEvent(null);

        AppCtx.setCacheOps(cacheOps);

        try {
            for (int i = 0; i < 10; i++) {
                keyInfo = anyKey.getAny(i);
                assertNotNull(keyInfo);
                if (i == 0) assertFalse(keyInfo.getIsNew());
                else assertTrue(keyInfo.getIsNew());
                assertEquals(i + 1, anyKey.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getCause().getMessage());
        }
    }

}