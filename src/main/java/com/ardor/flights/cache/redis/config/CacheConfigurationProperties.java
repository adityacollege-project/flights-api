package com.ardor.flights.cache.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "redis.cache")
@Data
public class CacheConfigurationProperties {

  private long timeoutSeconds = 60;
  private int redisPort = 6379;
  private String redisHost = "localhost";

}
