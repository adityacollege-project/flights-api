package com.ardor.flights.reprice.supplier.mondee.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MondeeRepriceRequestConfig {

  @Value("${flights.mondee.reprice.uri}")
  private String rootUri;

  @Value("${flights.mondee.reprice.api.connection.timeout.in.seconds}")
  private Integer connectionTimeOut;

  @Value("${flights.mondee.reprice.api.read.timeout.in.seconds}")
  private Integer readTimeOut;

  @Bean
  public RestTemplate repriceRestTemplate() {
    return new RestTemplateBuilder().rootUri(rootUri).
        setConnectTimeout(Duration.ofSeconds(connectionTimeOut)).
        setReadTimeout(Duration.ofSeconds(readTimeOut)).build();
  }
}
