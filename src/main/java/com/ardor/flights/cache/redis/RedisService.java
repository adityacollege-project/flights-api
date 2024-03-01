package com.ardor.flights.cache.redis;

import java.util.Optional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class RedisService {

  public static final String CACHE_NAME="flightCache";

  @Cacheable(cacheNames = CACHE_NAME)
  public String getFromCache(String key) {
    return "";
  }

  @CachePut(cacheNames = CACHE_NAME, key = "#key")
  public String populateCache(String key, String value) {
    return value;
  }

  @CacheEvict(cacheNames = CACHE_NAME)
  public boolean removeFromCache(String key) {
    return true;
  }

}
