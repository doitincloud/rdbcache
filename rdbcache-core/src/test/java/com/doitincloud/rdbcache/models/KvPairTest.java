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

package com.doitincloud.rdbcache.models;

import com.doitincloud.commons.Utils;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class KvPairTest {

    @Test
    public void convertJsonPojoMap() {
        Map<String, Object> map = Utils.toMap("{\n" +
                "    \"expire\" : \"30\",\n" +
                "    \"table\" : \"user_table\",\n" +
                "    \"clause\" : \"id = ?\",\n" +
                "    \"params\" : [ 12466 ],\n" +
                "    \"query_key\" : \"28f0a2d90b3c9d340e853b838d27845c\"\n" +
                "  }");

        KvPair pair1 = new KvPair("*", "keyInfo", map);

        Map<String, Object> pairMap1 = Utils.toMap(pair1);

        //System.out.println(Utils.toJsonMap(pairMap1));

        KvPair pair2 = Utils.toPojo(pairMap1, KvPair.class);

        assertEquals(pair1, pair2);

        Map<String, Object> pairMap2 = Utils.toMap(pair2);

        assertEquals(pairMap1, pairMap2);
    }
}