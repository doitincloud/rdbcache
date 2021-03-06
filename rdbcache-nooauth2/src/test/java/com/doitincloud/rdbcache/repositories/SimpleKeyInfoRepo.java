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

package com.doitincloud.rdbcache.repositories;

import com.doitincloud.rdbcache.supports.AnyKey;
import com.doitincloud.rdbcache.supports.Context;
import com.doitincloud.rdbcache.supports.KvPairs;
import com.doitincloud.commons.Utils;
import com.doitincloud.rdbcache.models.KeyInfo;
import com.doitincloud.rdbcache.models.KvPair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleKeyInfoRepo implements KeyInfoRepo {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleKeyInfoRepo.class);

    private Map<String, Object> data;

    public SimpleKeyInfoRepo(Map<String, Object> map) {
        data = new LinkedHashMap<>(map);
    }

    public SimpleKeyInfoRepo() {
        data = new LinkedHashMap<>();
    }

    @Override
    public boolean find(Context context, KvPair pair, KeyInfo keyInfo) {

        LOGGER.trace("find: " + pair.printKey() + " " + keyInfo.toString());

        boolean foundAll = true;
        String key = pair.getId();
        Map<String, Object> map = (Map<String, Object>) data.get(key);
        if (map == null) {
            foundAll = false;
            LOGGER.trace("find: Not Found " + key);
        } else {
            KeyInfo keyInfo2 = Utils.toPojo(map,  KeyInfo.class);
            keyInfo.copy(keyInfo2);
            LOGGER.trace("find: Found " + key);
        }
        return foundAll;
    }

    @Override
    public boolean find(Context context, KvPairs pairs, AnyKey anyKey) {

        LOGGER.trace("find pairs(" + pairs.size() + ") anyKey(" + anyKey.size() + ")");

        boolean foundAll = true;
        for (int i = 0; i < pairs.size(); i++) {
            KvPair pair = pairs.get(i);
            KeyInfo keyInfo = anyKey.getAny(i);
            String key = pair.getId();
            Map<String, Object> map = (Map<String, Object>) data.get(key);
            if (map == null) {
                foundAll = false;
                LOGGER.trace("find: Not Found " + key);
                continue;
            } else {
                keyInfo = Utils.toPojo(map,  KeyInfo.class);
                anyKey.set(i, keyInfo);
                LOGGER.trace("find: Found " + key);
            }
        }
        return foundAll;
    }

    @Override
    public boolean save(Context context, KvPair pair, KeyInfo keyInfo) {

        String key = pair.getId();
        Map<String, Object> map = Utils.toMap(keyInfo);
        data.put(key, map);
        LOGGER.trace("save: " + key);
        return true;
    }

    @Override
    public boolean save(Context context, KvPairs pairs, AnyKey anyKey) {

        Assert.isTrue(anyKey.size() == pairs.size(), anyKey.size() + " != " +
                pairs.size() + ", only supports that pairs and anyKey have the same size");

        for (int i = 0; i < pairs.size(); i++) {
            KvPair pair = pairs.get(i);
            KeyInfo keyInfo = anyKey.getAny(i);
            String key = pair.getId();
            Map<String, Object> map = Utils.toMap(keyInfo);
            data.put(key, map);
            LOGGER.trace("save: " + key);
        }
        return true;
    }

    @Override
    public void delete(Context context, KvPair pair) {

        LOGGER.trace("delete: " + pair.printKey());
        String key = pair.getId();
        data.remove(key);
        LOGGER.trace("delete: " + key);
    }

    @Override
    public void delete(Context context, KvPairs pairs) {

        LOGGER.trace("delete(" + pairs.size() + "): " + pairs.printKey());

        for (int i = 0; i < pairs.size(); i++) {
            KvPair pair = pairs.get(i);
            String key = pair.getId();
            data.remove(key);
            LOGGER.trace("delete: " + key);
        }
    }
}
