/**
 * 
 */
package com.ardor.flights.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.ardor.flights.model.cancel.FlightCancelRequest;
import com.ardor.flights.model.cancel.FlightCancelResponse;
import com.ardor.flights.search.supplier.mondee.MondeeSupplier;

import jakarta.validation.Valid;

/**
 * 
 */

@Component
@RequiredArgsConstructor
public class FlightCancelHelper {

	private static final String MONDEE = "mondee";
	private final MondeeSupplier mondeeSupplier;

	
	public FlightCancelResponse cancelPnr(@Valid FlightCancelRequest cancelRequest) {
		FlightCancelResponse response = mondeeSupplier.cancelPnr(cancelRequest);
		return response;
	}

}
