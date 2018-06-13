/**
 * @link http://rdbcache.com/
 * @copyright Copyright (c) 2017-2018 Sam Wen
 * @license http://rdbcache.com/license/
 */

package com.doitincloud.rdbcache.services;

import com.doitincloud.commons.Utils;
import com.doitincloud.rdbcache.supports.AnyKey;
import com.doitincloud.rdbcache.supports.KvPairs;
import com.doitincloud.rdbcache.configs.PropCfg;
import com.doitincloud.rdbcache.supports.Context;
import com.doitincloud.rdbcache.configs.AppCtx;
import com.doitincloud.rdbcache.models.KeyInfo;
import com.doitincloud.rdbcache.models.KvPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Service
public class RedisOps extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisOps.class);

    private Boolean enableMonitor = PropCfg.getEnableMonitor();

    private String queueName = PropCfg.getQueueName();

    private ListOperations listOps;

    @PostConstruct
    public void init() {
    }

    @EventListener
    public void handleEvent(ContextRefreshedEvent event) {
        enableMonitor = PropCfg.getEnableMonitor();
        queueName = PropCfg.getQueueName();
    }

    @EventListener
    public void handleApplicationReadyEvent(ApplicationReadyEvent event) {

        StringRedisTemplate template = AppCtx.getStringRedisTemplate();
        if (template == null) {
            LOGGER.error("failed to get redis template");
            return;
        }
        listOps = template.opsForList();
        // setup for test
        if (listOps == null) {
            return;
        }
        start();
    }

    public Boolean getEnableMonitor() {
        return enableMonitor;
    }

    public void setEnableMonitor(Boolean enableMonitor) {
        this.enableMonitor = enableMonitor;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    private boolean isRunning = false;

    public boolean isRunning() {
        return isRunning;
    }

    // make sure config set notify-keyspace-events Ex
    //
    public void ensureNotifyKeySpaceEventsEx() {

        StringRedisTemplate template = AppCtx.getStringRedisTemplate();
        if (template == null) {
            LOGGER.error("failed to get redis template");
            return;
        }
        RedisConnectionFactory factory = template.getConnectionFactory();
        if (factory == null) {
            LOGGER.error("failed to get redis connection factory");
            return;
        }
        RedisConnection connection = factory.getConnection();
        if (connection == null) {
            LOGGER.error("failed to get redis connection");
            return;
        }
        String config = null;
        String pattern = "notify-keyspace-events";
        Object object = connection.getConfig(pattern);
        if (object instanceof Properties) {                  // for boot 2.+
            Properties properties = (Properties) object;
            config = properties.getProperty(pattern);
        } else {                                             // for boot 1.+
            List<String> properties = (List<String>) object;
            if (properties.size() > 1) {
                config = properties.get(1);
            }
        }
        if (config == null) {
            LOGGER.trace("config set for " + pattern + " is empty"); // it happens during unit test, which is OK
            return;
        }

        LOGGER.trace("config get " + Utils.toJson(config));

        if (config.contains("E") && (config.contains("A") || config.contains("x"))) {
            return;
        }

        if (!config.contains("E")) {
            config += "E";
        }
        if (!config.contains("A") && !config.contains("x")) {
            config += "x";
        }
        connection.setConfig("notify-keyspace-events", config);

        LOGGER.trace("setConfig notify-keyspace-events " + config);

    }

    @Override
    public void interrupt() {
        isRunning = false;
        super.interrupt();
    }

    private boolean freshConnection = true;

    @Override
    public void run() {

        isRunning = true;

        while (isRunning) {

            try {

                if (freshConnection) {
                    ensureNotifyKeySpaceEventsEx();
                    freshConnection = false;
                }

                if (freshConnection) {
                    try { 
                        Thread.sleep(5000);
                        continue;
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }

                if (!isRunning) break;

                String task = (String) listOps.leftPop(queueName, 0, TimeUnit.SECONDS);
                if (task == null) continue;
                if (!isRunning) break;
                onReceiveTask(task);
                    
            } catch (RedisConnectionFailureException e) {

                LOGGER.warn("Connection failure occurred. Restarting task queue after 5000 ms");

                e.printStackTrace();

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

                freshConnection = true;

            } catch (Exception e) {
                String msg = e.getCause().getMessage();
                LOGGER.error(msg);
                e.printStackTrace();
            }


        }

        isRunning = false;
    }

    public void onReceiveTask(String task) {
        LOGGER.debug("Received Task: " + task);
    }
}
