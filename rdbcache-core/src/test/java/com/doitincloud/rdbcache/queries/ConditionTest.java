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

import com.doitincloud.commons.Utils;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class ConditionTest {

    @Test
    public void convertJsonPojoMap() {

        Condition condition1 = new Condition("=", new String[]{"1", "2", "3"});
        Map<String, Object> map1 = Utils.toMap(condition1);
        Condition condition2 = Utils.toPojo(map1, Condition.class);
        Map<String, Object> map2 = Utils.toMap("{\"=\":[\"1\",\"2\",\"3\"]}");

        assertEquals(map1, map2);
        assertEquals(condition1, condition2);
    }

}