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

import com.doitincloud.rdbcache.repositories.*;
import com.doitincloud.rdbcache.services.*;

import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class AppCtx {

    private static ApplicationContext ctx;

    private static AsyncOps asyncOps;

    private static DbaseOps dbaseOps;

    private static ExpireOps expireOps;

    private static CacheOps cacheOps;

    private static RedisOps redisOps;

    private static DbaseRepo dbaseRepo;

    private static KeyInfoRepo keyInfoRepo;

    private static RedisKeyInfoTemplate redisKeyInfoTemplate;

    private static KvPairRepo kvPairRepo;

    private static MonitorRepo monitorRepo;

    private static RedisRepo redisRepo;

    private static JdbcTemplate jdbcTemplate;

    private static StringRedisTemplate stringRedisTemplate;

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }

    public static void setApplicationContext(ApplicationContext ctx) {
        AppCtx.ctx = ctx;
    }

    public static AsyncOps getAsyncOps() {
        if (ctx != null && asyncOps == null) {
            try {
                asyncOps = ctx.getBean(AsyncOps.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return asyncOps;
    }

    public static void setAsyncOps(AsyncOps asyncOps) {
        AppCtx.asyncOps = asyncOps;
    }

    public static DbaseOps getDbaseOps() {
        if (ctx != null && dbaseOps == null) {
            try {
                dbaseOps = ctx.getBean(DbaseOps.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dbaseOps;
    }

    public static void setDbaseOps(DbaseOps dbaseOps) {
        AppCtx.dbaseOps = dbaseOps;
    }

    public static ExpireOps getExpireOps() {
        if (ctx != null && expireOps == null) {
            try {
                expireOps = ctx.getBean(ExpireOps.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return expireOps;
    }

    public static void setExpireOps(ExpireOps expireOps) {
        AppCtx.expireOps = expireOps;
    }

    public static RedisOps getRedisOps() {
        if (ctx != null && redisOps == null) {
            try {
                redisOps = ctx.getBean(RedisOps.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return redisOps;
    }

    public static void setRedisOps(RedisOps redisOps) {
        AppCtx.redisOps = redisOps;
    }

    public static CacheOps getCacheOps() {
        if (ctx != null && cacheOps == null) {
            try {
                cacheOps = ctx.getBean(CacheOps.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cacheOps;
    }

    public static void setCacheOps(CacheOps cache) {
        cacheOps = cache;
    }

    public static DbaseRepo getDbaseRepo() {
        if (ctx != null && dbaseRepo == null) {
            try {
                dbaseRepo = ctx.getBean(DbaseRepo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dbaseRepo;
    }

    public static void setDbaseRepo(DbaseRepo dbaseRepo) {
        AppCtx.dbaseRepo = dbaseRepo;
    }

    public static KeyInfoRepo getKeyInfoRepo() {
        if (ctx != null && keyInfoRepo == null) {
            try {
                keyInfoRepo = ctx.getBean(KeyInfoRepo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return keyInfoRepo;
    }

    public static void setKeyInfoRepo(KeyInfoRepo keyInfoRepo) {
        AppCtx.keyInfoRepo = keyInfoRepo;
    }

    public static KvPairRepo getKvPairRepo() {
        if (ctx != null && kvPairRepo == null) {
            try {
                kvPairRepo = ctx.getBean(KvPairRepo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return kvPairRepo;
    }

    public static void setKvPairRepo(KvPairRepo kvPairRepo) {
        AppCtx.kvPairRepo = kvPairRepo;
    }

    public static RedisKeyInfoTemplate getRedisKeyInfoTemplate() {
        if (ctx != null && redisKeyInfoTemplate == null) {
            try {
                redisKeyInfoTemplate = ctx.getBean(RedisKeyInfoTemplate.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return redisKeyInfoTemplate;
    }

    public static void setRedisKeyInfoTemplate(RedisKeyInfoTemplate template) {
        AppCtx.redisKeyInfoTemplate = template;
    }

    public static MonitorRepo getMonitorRepo() {
        if (ctx != null && monitorRepo == null) {
            try {
                monitorRepo = ctx.getBean(MonitorRepo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return monitorRepo;
    }

    public static void setMonitorRepo(MonitorRepo monitorRepo) {
        AppCtx.monitorRepo = monitorRepo;
    }

    public static RedisRepo getRedisRepo() {
        if (ctx != null && redisRepo == null) {
            try {
                redisRepo = ctx.getBean(RedisRepo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return redisRepo;
    }

    public static void setRedisRepo(RedisRepo redisRepo) {
        AppCtx.redisRepo = redisRepo;
    }

    public static JdbcTemplate getJdbcTemplate() {
        if (ctx != null && jdbcTemplate == null) {
            try {
                jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jdbcTemplate;
    }

    public static DataSource getJdbcDataSource() {

        JdbcTemplate template = getJdbcTemplate();
        if (template == null) return null;

        return template.getDataSource();
    }

    public static void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        AppCtx.jdbcTemplate = jdbcTemplate;
    }

    public static StringRedisTemplate getStringRedisTemplate() {
        if (ctx != null && stringRedisTemplate == null) {
            try {
                stringRedisTemplate = (StringRedisTemplate) ctx.getBean("stringRedisTemplate");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stringRedisTemplate;
    }

    public static void setRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        AppCtx.stringRedisTemplate = stringRedisTemplate;
    }
}
