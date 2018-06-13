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

import com.doitincloud.rdbcache.exceptions.ServerErrorException;
import com.doitincloud.rdbcache.models.KvIdType;
import com.doitincloud.rdbcache.models.KvPair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KvPairs extends ArrayList<KvPair>{

    public KvPairs(KvIdType idType) {
        add(new KvPair(idType));
    }

    public KvPairs(String id, String value) {
        add(new KvPair(id, "data", value));
    }

    public KvPairs(String id) {
        add(new KvPair(id));
    }

    public KvPairs(List list) {
        for (Object object: list) {
            if (object instanceof  String) {
                String key = (String) object;
                add(new KvPair(key));
            } else if (object instanceof Map) {
                KvPair pair = new KvPair("*");
                pair.setObject(object);
                add(pair);
            }
        }
    }

    public KvPairs(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry: map.entrySet()) {
            KvPair pair = new KvPair(entry.getKey());
            pair.setObject(entry.getValue());
            add(pair);
        }
    }

    public KvPairs(KvPair pair) {
        add(pair);
    }

    public KvPairs() {
    }

    public void setPair(KvPair pair) {
        clear();
        add(pair);
    }

    public KvPair getPair() {
        if (size() == 0) {
            return null;
        }
        return get(0);
    }

    public String getKey() {
        if (size() == 0) {
            return null;
        }
        return get(0).getId();

    }

    public KvPair getAny() {
        return getAny(0);
    }

    public KvPair getAny(int index) {
        if (index > size()) {
            throw new ServerErrorException("getAny index out of range");
        } else if (index == size()) {
            if (index == 0) {
                add(new KvPair("*"));
            } else {
                String type = get(0).getType();
                add(new KvPair("*", type));
            }
        }
        return get(index);
    }

    public List<String> getKeys() {
        List<String> keys = new ArrayList<String>();
        for (int i = 0; i < size(); i++) {
            keys.add(get(i).getId());
        }
        return keys;
    }

    public String printKey() {
        int size = size();
        if (size == 0) {
            return "null";
        }
        String key = get(0).printKey();
        if (size > 1) {
            key += "... ";
        }
        return key;
    }

    public KvPairs clone() {
        KvPairs clone = new KvPairs();
        for (KvPair pair: this) {
            clone.add(pair.clone());
        }
        return clone;
    }
}
