/**
 * 
 */
package com.ardor.flights.cancel.supplier.mondee.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 
 */

@Configuration
public class MondeeCancelRequestConfig {

	  @Value("${flights.mondee.cancel.uri}")
	  private String rootUri;

	  @Value("${flights.mondee.cancel.api.connection.timeout.in.seconds}")
	  private Integer connectionTimeOut;

	  @Value("${flights.mondee.cancel.api.read.timeout.in.seconds}")
	  private Integer readTimeOut;

	  @Bean
	  public RestTemplate cancelRestTemplate() {
	    return new RestTemplateBuilder().rootUri(rootUri).
	        setConnectTimeout(Duration.ofSeconds(connectionTimeOut)).
	        setReadTimeout(Duration.ofSeconds(readTimeOut)).build();
	  }

}
