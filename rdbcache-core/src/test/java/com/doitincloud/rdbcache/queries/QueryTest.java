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

package com.doitincloud.rdbcache.queries;

import com.doitincloud.rdbcache.configs.Configurations;
import com.doitincloud.rdbcache.supports.AnyKey;
import com.doitincloud.rdbcache.supports.Context;
import com.doitincloud.rdbcache.supports.KvPairs;
import com.doitincloud.commons.Utils;
import com.doitincloud.rdbcache.models.KeyInfo;
import com.doitincloud.rdbcache.models.KvPair;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Configurations.class} )
public class QueryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void selectTest() {

        try {
            Context context = new Context();
            KvPairs pairs = new KvPairs();
            AnyKey anyKey = new AnyKey();

            KeyInfo keyInfo = new KeyInfo();
            keyInfo.setExpire("100");
            keyInfo.setTable("user_table");

            String json = "{\"table\":\"user_table\",\"conditions\":{\"id\":{\">\":[\"1\"]}},\"limit\":2}";
            QueryInfo queryInfo = Utils.toPojo(Utils.toMap(json), QueryInfo.class);
            keyInfo.setQuery(queryInfo);
            anyKey.setKeyInfo(keyInfo);
            Query query = new Query(context, jdbcTemplate, pairs, anyKey);

            assertTrue(query.ifSelectOk());

            assertTrue(query.executeSelect());

            //System.out.println(Utils.toJsonMap(pairs));
            assertEquals(2, pairs.size());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getCause().getMessage());
        }
    }

    @Test
    public void insertTest() {

        try {
            Context context = new Context();
            KvPairs pairs = new KvPairs();

            String json = "{\n" +
                    "    \"email\" : \"sam@example.com\",\n" +
                    "    \"name\" : \"Sam W.\",\n" +
                    "    \"dob\" : \"1975-08-12\"\n" +
                    "  }";

            Map<String, Object> map1 = Utils.toMap(json);

            KvPair pair = new KvPair("*", "data", map1);

            pairs.setPair(pair);

            AnyKey anyKey = new AnyKey();

            KeyInfo keyInfo = new KeyInfo();
            keyInfo.setExpire("100");
            keyInfo.setTable("user_table");

            anyKey.setKeyInfo(keyInfo);
            Query query = new Query(context, jdbcTemplate, pairs, anyKey);

            assertTrue(query.ifInsertOk());

            assertTrue(query.executeInsert(false, false));

            //System.out.println(Utils.toJsonMap(pairs));
            Map<String, Object> map2 = pair.getData();
            assertEquals("4", map2.get("id"));
            map2.remove("id");
            assertEquals(map1, map2);

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getCause().getMessage());
        }
    }

    @Test
    public void updateTest() {
        try {
            Context context = new Context();
            KvPairs pairs = new KvPairs();
            AnyKey anyKey = new AnyKey();

            KeyInfo keyInfo = new KeyInfo();
            keyInfo.setExpire("100");
            keyInfo.setTable("user_table");

            String json1 = "{\"table\":\"user_table\",\"conditions\":{\"email\":{\"=\":[\"david@example.com\"]}}}";
            QueryInfo queryInfo1 = Utils.toPojo(Utils.toMap(json1), QueryInfo.class);
            keyInfo.setQuery(queryInfo1);
            anyKey.setKeyInfo(keyInfo);
            Query query = new Query(context, jdbcTemplate, pairs, anyKey);

            assertTrue(query.ifSelectOk());

            assertTrue(query.executeSelect());

            //System.out.println(Utils.toJsonMap(pairs));
            assertEquals(1, pairs.size());

            String key = pairs.getPair().getId();
            Integer id =  (Integer) pairs.getPair().getData().get("id");

            assertNotNull(id);

            //System.out.println("id = "+id);

            String json2 = "{\"name\" : \"David Copper\"}";
            Map<String, Object> map1 = Utils.toMap(json2);

            KvPair pair = new KvPair(key, "data", map1);
            pairs.setPair(pair);
            KeyInfo keyInfo2 = new KeyInfo();
            keyInfo2.setExpire("100");
            keyInfo2.setTable("user_table");
            keyInfo2.setClause("id = ?");
            keyInfo2.setParams(Arrays.asList(id));
            anyKey.setKeyInfo(keyInfo2);

            query = new Query(context, jdbcTemplate, pairs, anyKey);

            assertTrue(query.ifUpdateOk());

            assertTrue(query.executeUpdate());

            pairs.clear();

            query = new Query(context, jdbcTemplate, pairs, anyKey);

            assertTrue(query.ifSelectOk());

            assertTrue(query.executeSelect());

            map1 = pairs.getPair().getData();
            assertEquals("David Copper", map1.get("name"));

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getCause().getMessage());
        }
    }

    @Test
    public void deleteTest() {
        try {
            Context context = new Context();
            KvPairs pairs = new KvPairs();
            AnyKey anyKey = new AnyKey();

            KeyInfo keyInfo = new KeyInfo();
            keyInfo.setExpire("100");
            keyInfo.setTable("user_table");

            String json1 = "{\"table\":\"user_table\",\"conditions\":{\"id\":{\"=\":[\"3\"]}}}";
            QueryInfo queryInfo1 = Utils.toPojo(Utils.toMap(json1), QueryInfo.class);
            keyInfo.setQuery(queryInfo1);
            anyKey.setKeyInfo(keyInfo);
            Query query = new Query(context, jdbcTemplate, pairs, anyKey);

            assertTrue(query.ifSelectOk());
            assertTrue(query.executeSelect());

            //System.out.println(Utils.toJsonMap(pairs));
            assertEquals(1, pairs.size());

            String key = pairs.getPair().getId();

            KvPair pair = new KvPair(key);
            pairs.setPair(pair);
            KeyInfo keyInfo2 = new KeyInfo();
            keyInfo2.setExpire("100");
            keyInfo2.setTable("user_table");

            keyInfo2.setClause("id = ?");
            keyInfo2.setParams(Arrays.asList("3"));
            anyKey.setKeyInfo(keyInfo2);

            query = new Query(context, jdbcTemplate, pairs, anyKey);

            assertTrue(query.ifDeleteOk());

            assertTrue(query.executeDelete());

            pairs.clear();

            query = new Query(context, jdbcTemplate, pairs, anyKey);

            assertTrue(query.ifSelectOk());

            assertFalse(query.executeSelect());

            assertEquals(0, pairs.size());

        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getCause().getMessage());
        }
    }}