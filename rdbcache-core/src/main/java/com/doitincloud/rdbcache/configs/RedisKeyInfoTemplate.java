package com.doitincloud.rdbcache.configs;

import com.doitincloud.rdbcache.models.KeyInfo;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisKeyInfoTemplate extends RedisTemplate<String, KeyInfo> {
}
