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

package com.doitincloud.rdbcache.configs;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.doitincloud.commons.Utils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class RedisJsonSerializer implements RedisSerializer<Object> {

    private static byte[] bytesNull = "null".getBytes();

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        if (o == null) {
            return bytesNull;
        }
        try {
            return Utils.getObjectMapper().writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        if (bytesNull.equals(bytes)) {
            return null;
        }
        try {
            return Utils.getObjectMapper().readValue(bytes, Object.class);
        } catch (Exception e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }
}
