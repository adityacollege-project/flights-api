package com.ardor.flights.search.supplier.mondee.config;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;

import com.ardor.flights.common.Constants;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MondeeSearchRequestConfig {

  @Value("${flights.mondee.search.uri}")
  private String rootUri;

  @Value("${flights.mondee.search.api.connection.timeout.in.seconds}")
  private Integer connectionTimeOut;

  @Value("${flights.mondee.search.api.read.timeout.in.seconds}")
  private Integer readTimeOut;

  @Value("${resilience4j.circuitbreaker.instances.mondeeFlightSearch.slidingWindowType}")
  private String slidingWindowType;

  @Value("${resilience4j.circuitbreaker.instances.mondeeFlightSearch.slidingWindowSize}")
  private Integer slidingWindowSize;

  @Value("${resilience4j.circuitbreaker.instances.mondeeFlightSearch.failureRateThreshold}")
  private Integer failureRateThreshold;

  @Value("${resilience4j.circuitbreaker.instances.mondeeFlightSearch.slowCallRateThreshold}")
  private Integer slowCallRateThreshold;

  @Value("${resilience4j.circuitbreaker.instances.mondeeFlightSearch.slowCallDurationThreshold}")
  private Integer slowCallDurationThreshold;

  @Value("${resilience4j.circuitbreaker.instances.mondeeFlightSearch.permittedNumberOfCallsInHalfOpenState}")
  private Integer permittedNumberOfCallsInHalfOpenState;

  @Value("${resilience4j.circuitbreaker.instances.mondeeFlightSearch.waitDurationInOpenState}")
  private Integer waitDurationInOpenState;

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplateBuilder().rootUri(rootUri).
        setConnectTimeout(Duration.ofSeconds(connectionTimeOut)).
        setReadTimeout(Duration.ofSeconds(readTimeOut)).build();
  }

  @Bean
  public CircuitBreakerConfig circuitBreakerConfig() {
    return CircuitBreakerConfig.custom()
        .slidingWindowType(COUNT_BASED)
        .slidingWindowSize(slidingWindowSize)
        .failureRateThreshold(failureRateThreshold)
        .slowCallRateThreshold(slowCallRateThreshold)
        .slowCallDurationThreshold(Duration.ofSeconds(slowCallDurationThreshold))
        .permittedNumberOfCallsInHalfOpenState(permittedNumberOfCallsInHalfOpenState)
        .waitDurationInOpenState(Duration.ofSeconds(waitDurationInOpenState))
        .build();
  }

  @Bean
  public CircuitBreakerRegistry circuitBreakerRegistry(CircuitBreakerConfig circuitBreakerConfig) {
    return CircuitBreakerRegistry.of(circuitBreakerConfig);
  }

  @Bean
  public CircuitBreaker circuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
    return circuitBreakerRegistry.circuitBreaker(Constants.SEARCH_CB_INSTANCE_NAME);
  }
}
