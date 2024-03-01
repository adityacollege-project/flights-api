package com.ardor.flights.exception;

public class UserNotFoundExcpetion extends ArdorException{

  public UserNotFoundExcpetion(String msg) {
    super(msg);
  }

  public UserNotFoundExcpetion(String msg, Throwable throwable) {
    super(msg, throwable);
  }
}
