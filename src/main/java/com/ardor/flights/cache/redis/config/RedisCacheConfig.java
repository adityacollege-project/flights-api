package com.ardor.flights.cache.redis.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableConfigurationProperties(CacheConfigurationProperties.class)
@Slf4j
public class RedisCacheConfig extends CachingConfigurerSupport {

  @Bean
  public LettuceConnectionFactory redisConnectionFactory(CacheConfigurationProperties properties) {
    log.debug("Redis (/Lettuce) configuration enabled. With cache timeout " + properties.getTimeoutSeconds() + " seconds.");

    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setHostName(properties.getRedisHost());
    redisStandaloneConfiguration.setPort(properties.getRedisPort());
    return new LettuceConnectionFactory(redisStandaloneConfiguration);
  }

  @Bean
  public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf) {
    RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
    redisTemplate.setConnectionFactory(cf);
    return redisTemplate;
  }

  @Bean
  public RedisCacheConfiguration cacheConfiguration(CacheConfigurationProperties properties) {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofSeconds(properties.getTimeoutSeconds()));
  }

  @Bean
  public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, CacheConfigurationProperties properties) {
    Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();


    return RedisCacheManager
        .builder(redisConnectionFactory)
        .cacheDefaults(cacheConfiguration(properties))
        .withInitialCacheConfigurations(cacheConfigurations).build();
  }

}
