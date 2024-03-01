package com.ardor.flights.user.supplier;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class config {
  @Value("${flights.user.uri}")
  private String rootUri;

  @Value("${flights.user.api.connection.timeout.in.seconds}")
  private Integer connectionTimeOut;

  @Value("${flights.user.api.read.timeout.in.seconds}")
  private Integer readTimeOut;

  @Bean
  public RestTemplate userRestTemplate() {
    return new RestTemplateBuilder().rootUri(rootUri).
        setConnectTimeout(Duration.ofSeconds(connectionTimeOut)).
        setReadTimeout(Duration.ofSeconds(readTimeOut)).build();
  }
}
