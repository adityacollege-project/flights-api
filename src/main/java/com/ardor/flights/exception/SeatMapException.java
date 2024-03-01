/**
 * 
 */
package com.ardor.flights.exception;

import com.ardor.flights.exception.ArdorException;

/**
 * 
 */
public class SeatMapException extends ArdorException {
	
	public SeatMapException(String msg) {
		super(msg);
	}

	public SeatMapException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

}
