package com.ardor.flights.search.supplier.mondee;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {
  @ExceptionHandler({CallNotPermittedException.class})
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  public void handle() {
    log.error("Search API failed, Circuit is opened");
  }
}
