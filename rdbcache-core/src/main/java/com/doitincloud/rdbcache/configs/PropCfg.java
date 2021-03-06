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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class PropCfg {

    private static String activeProfile = "prod";

    private static String hdataPrefix = "hdata";

    private static String hkeyPrefix = "hkey";

    private static String eventPrefix = "event";

    private static String queueName = "queue";

    private static String defaultExpire = "180";

    private static String defaultAttr = "async";

    private static Boolean enableMonitor = false;

    private static Long eventLockTimeout = 60L;

    private static Long keyMinCacheTTL = 180L;

    private static Long tableInfoCacheTTL = 3600L;

    private static Long maxCacheSize = 1024L;

    private static Long cacheRecycleSecs = 300L;  // 5 minutes

    private static Boolean enableDbFallback = false;

    private static Long dataMaxCacheTLL = 60L;

    private static String datasourceUrl;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Value("${spring.profiles.active:prod}")
    public void setActiveProfile(String name) {
        if (name != null && name.length() > 0) {
            activeProfile = name;
        }
    }

    public static String getActiveProfile() {
        return activeProfile;
    }

    @Value("${rdbcache.hdata_prefix:hdata}")
    public void setHdataPrefix(String prefix) {
        if (prefix != null && prefix.length() > 0) {
            hdataPrefix = prefix.replace("::", "");
        }
    }

    public static String getHdataPrefix() {
        return hdataPrefix;
    }

    @Value("${rdbcache.hkeys_prefix:hkey}")
    public void setHkeyPrefix(String prefix) {
        if (prefix != null && prefix.length() > 0) {
            hkeyPrefix = prefix.replace("::", "");
        }
    }

    public static String getHkeyPrefix() {
        return hkeyPrefix;
    }

    @Value("${rdbcache.event_prefix:event}")
    public void setEventPrefix(String prefix) {
        if (prefix != null && prefix.length() > 0) {
            eventPrefix = prefix.replace("::", "");
        }
    }

    public static String getEventPrefix() {
        return eventPrefix;
    }

    @Value("${rdbcache.queue_name:queue}")
    public void setQueueName(String name) {
        if (name != null && name.length() > 0) {
            queueName = name.replace("::", "");
        }
    }

    public static String getQueueName() {
        return queueName;
    }

    @Value("${rdbcache.default_expire:180}")
    public void setDefaultExpire(String expire) {
        defaultExpire = expire;
    }

    public static String getDefaultExpire() {
        return defaultExpire;
    }

    @Value("${rdbcache.default_attr:async}")
    public void setDefaultAttr(String attr) {
        defaultAttr = attr;
    }

    public static String getDefaultAttr() {
        return defaultAttr;
    }

    @Value("${rdbcache.enable_monitor:false}")
    public void setEnableMonitor(Boolean enable) {
        enableMonitor = enable;
    }

    public static Boolean getEnableMonitor() {
        return enableMonitor;
    }

    @Value("${rdbcache.event_lock_timeout:60}")
    public void setEventLockTimeout(Long timeout) { eventLockTimeout = timeout; }

    public static Long getEventLockTimeout() { return eventLockTimeout; }

    @Value("${rdbcache.key_min_cache_ttl:180}")
    public void setKeyInfoCacheTTL(Long ttl) {
        keyMinCacheTTL = ttl;
        if (keyMinCacheTTL < 60l) {
            keyMinCacheTTL = 60l;
        }
    }

    public static Long getKeyMinCacheTTL() {
        return keyMinCacheTTL;
    }

    @Value("${rdbcache.table_info_cache_ttl:3600}")
    public void setTableInfoCacheTTL(Long ttl) {
        tableInfoCacheTTL = ttl;
    }

    public static Long getTableInfoCacheTTL() {
        return tableInfoCacheTTL;
    }

    @Value("${rdbcache.local_cache_max_size:1024}")
    public void setMaxCacheSize(Long maxSize) {
        maxCacheSize = maxSize;
    }

    public static Long getMaxCacheSize() {
        return maxCacheSize;
    }

    @Value("${rdbcache.cache_recycle_secs:300}")
    public void setCacheRecycleSecs(Long secs) {
        cacheRecycleSecs = secs;
    }

    public static Long getCacheRecycleSecs() {
        return cacheRecycleSecs;
    }

    @Value("${rdbcache.enable_db_fallback:false}")
    public void setEnableDbFallback(Boolean enable) {
        enableDbFallback = enable;
    }

    public static Boolean getEnableDbFallback() {
        return enableDbFallback;
    }

    @Value("${rdbcache.data_max_cache_ttl:60}")
    public void setDataMaxCacheTLL(Long tll) {
        dataMaxCacheTLL = tll;
    }

    public static Long getDataMaxCacheTLL() {
        return dataMaxCacheTLL;
    }

    @Value("${spring.datasource.url}")
    public void setDatasourceUrl(String url) {
        if (url != null && url.length() > 0) {
            datasourceUrl = url;
        }
    }

    public static String getDatasourceUrl() {
        return datasourceUrl;
    }

    public static String printConfigurations() {
        return "{"+
          "\"hdataPrefix\": \"" + hdataPrefix + "\", " +
          "\"hkeyPrefix\": \"" + hkeyPrefix + "\", " +
          "\"eventPrefix\": \"" + eventPrefix + "\", " +
          "\"queueName\": \"" + queueName + "\", " +
          "\"defaultExpire\": \"" + defaultExpire + "\", " +
          "\"enableMonitor\": \"" + enableMonitor.toString() + "\", " +
          "\"eventLockTimeout\": \"" + eventLockTimeout.toString() + "\", " +
          "\"keyMinCacheTTL\": \"" + keyMinCacheTTL.toString() + "\", " +
          "\"tableInfoCacheTTL\": \"" + tableInfoCacheTTL.toString() + "\", " +
          "\"maxCacheSize\": \"" + maxCacheSize.toString() + "\", " +
          "\"cacheRecycleSecs\": \"" + cacheRecycleSecs.toString() + "\", " +
          "\"enableDbFallback\": \"" + enableDbFallback.toString() + "\", " +
          "\"dataMaxCacheTLL\": \"" + dataMaxCacheTLL.toString() + "\", "+
          "\"datasourceUrl\": \"" + datasourceUrl + "\"" +
           "}";
    }
}
