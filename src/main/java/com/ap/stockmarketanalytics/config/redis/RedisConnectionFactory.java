package com.ap.stockmarketanalytics.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConnectionFactory {

    //Keeping the below commented code in case if I need to switch to Jedis for some reason.
//    @Bean
//    JedisConnectionFactory jedisConnectionFactory() {
//
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//        redisStandaloneConfiguration.setHostName("192.168.4.146");
//        redisStandaloneConfiguration.setPort(6379);
//
//        return new JedisConnectionFactory(redisStandaloneConfiguration);
//    }

//    @Bean
//    public RedisTemplate<String, Object> redisTemplate() {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(jedisConnectionFactory());
//        return template;
//    }

    @Bean
    public RedissonClient redissonClient(){

        Config config = new Config();
        config.useSingleServer()
              .setAddress("redis://192.168.4.146:6379");

        return Redisson.create(config);

    }

}
