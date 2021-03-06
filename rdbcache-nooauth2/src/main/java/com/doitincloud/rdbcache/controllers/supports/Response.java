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

package com.doitincloud.rdbcache.controllers.supports;

import com.doitincloud.rdbcache.supports.Context;
import com.doitincloud.rdbcache.supports.KvPairs;
import com.doitincloud.rdbcache.models.KvPair;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.*;

public class Response  {

    private static final Logger LOGGER = LoggerFactory.getLogger(Response.class);

    private static DecimalFormat durationFormat = new DecimalFormat("#.######");

    public static ResponseEntity<Map<String, Object>> send(Context context, KvPairs pairs) {
        return send(context, pairs, null);
    }

    public static ResponseEntity<Map<String, Object>> send(Context context, Map<String, Object> data) {
        return send(context, null, data);
    }

    public static ResponseEntity<Map<String, Object>> send(Context context, KvPairs pairs,Map<String, Object> data) {

        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Long now = System.currentTimeMillis();
        map.put("timestamp", now);
        Long duration = context.getDuration();
        if (duration != null) {
            double db = ((double) duration) / 1000000000.0;
            map.put("duration", durationFormat.format(db));
        }
        if (pairs != null) {
            if (pairs.size() == 0) {
                if (data == null) {
                    map.put("data", new HashMap<>());
                } else {
                    map.put("data", data);
                }
            } else if (pairs.size() == 1 && !context.isBatch()) {
                KvPair pair = pairs.get(0);
                map.put("key", pair.getId());
                if (context.isSendValue()) {
                    map.put("data",pair.getMapValue());
                }
            } else {
                if (context.isSendValue()) {
                    Map<String, Object> dmap = new LinkedHashMap<String, Object>();
                    map.put("data", dmap);
                    for (KvPair pair : pairs) {
                        dmap.put(pair.getId(), pair.getMapValue());
                    }
                } else {
                    List<String> keys = new ArrayList<>();
                    for (KvPair pair : pairs) {
                        keys.add(pair.getId());
                    }
                    map.put("data", keys);
                }
            }
        } else if (data != null) {
            map.put("data", data);
        }
        String traceId = context.getTraceId();
        if ( traceId != null) {
            map.put("trace_id", traceId);
        }

        LOGGER.info("200 Ok - " + context.getAction());

        return ResponseEntity.ok(map);
    }
}
