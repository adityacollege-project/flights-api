package com.ardor.flights.exception;

public class FlightNotFoundException extends ArdorException{
  public FlightNotFoundException(String msg)
  {
    super(msg);
  }
  public FlightNotFoundException(String msg,Throwable throwable)
  {
    super(msg, throwable);
  }
}