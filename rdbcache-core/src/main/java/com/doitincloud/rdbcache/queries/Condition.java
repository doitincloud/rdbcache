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

import java.util.*;

public class Condition extends LinkedHashMap<String, List<String>> {

    public Condition(String ops, String[] values) {
        push(ops, values);
    }

    public Condition() {
    }

    public void push(String ops, String[] values) {
        if (values == null || values.length == 0) {
            return;
        }
        List<String> list = (List<String>) get(ops);
        if (list == null) {
            list = new ArrayList<String>();
            put(ops, list);
        }
        for (int i = 0; i< values.length; i++) {
            if (!list.contains(values[i])) {
                list.add(values[i]);
            }
        }
    }

    public void push(String ops, String value) {
        if (value == null) {
            return;
        }
        List<String> list = (List<String>) get(ops);
        if (list == null) {
            list = new ArrayList<String>();
            put(ops, list);
        }
        if (!list.contains(value)) {
            list.add(value);
        }
    }
}
