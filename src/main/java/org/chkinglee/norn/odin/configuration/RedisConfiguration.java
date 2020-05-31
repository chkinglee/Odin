package org.chkinglee.norn.odin.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * TODO
 *
 * @Author: lilinzhen
 * @Version: 2020/5/31
 **/
@Configuration
public class RedisConfiguration {

    @Bean(name = "stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    // @Bean
    // public RedisConnectionFactory connectionFactory() {
    //     JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
    //     return connectionFactory;
    // }



}
