/**
 * @link http://rdbcache.com/
 * @copyright Copyright (c) 2017-2018 Sam Wen
 * @license http://rdbcache.com/license/
 */

package com.doitincloud.rdbcache.configs;

import com.doitincloud.rdbcache.models.KeyInfo;
import com.doitincloud.rdbcache.services.ExpireOps;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean(name = "jedisConnectionFactory")
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public ExpireOps expireOps() {
        return new ExpireOps();
    }

    @Bean
    public RedisKeyInfoTemplate redisKeyInfoTemplate() {

        RedisKeyInfoTemplate template =  new RedisKeyInfoTemplate();

        template.setConnectionFactory(jedisConnectionFactory());

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<KeyInfo>(KeyInfo.class));

        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();

        template.setConnectionFactory(jedisConnectionFactory());
        template.setHashValueSerializer(new RedisJsonSerializer());

        return template;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter() {
        return new MessageListenerAdapter(expireOps(), "onExpireEvent");
    }

    @Bean
    RedisMessageListenerContainer container() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.addMessageListener(listenerAdapter(), new PatternTopic("__key*__:expired"));
        return container;
    }

}
