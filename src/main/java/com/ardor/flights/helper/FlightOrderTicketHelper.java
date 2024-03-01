/**
 * 
 */
package com.ardor.flights.helper;

import org.springframework.stereotype.Component;

import com.ardor.flights.model.ticket.FlightOrderTicketRequest;
import com.ardor.flights.model.ticket.FlightOrderTicketResponse;
import com.ardor.flights.search.supplier.mondee.MondeeSupplier;

import jakarta.validation.Valid;

/**
 * 
 */
@Component
public class FlightOrderTicketHelper {

	private static final String MONDEE = "mondee";
	private final MondeeSupplier mondeeSupplier;
	
	public FlightOrderTicketHelper(MondeeSupplier mondeeSupplier) {
		this.mondeeSupplier = mondeeSupplier;
	}

	public FlightOrderTicketResponse orderTicket(@Valid FlightOrderTicketRequest flightOrderTicketRequest) {
		return mondeeSupplier.orderTicket(flightOrderTicketRequest);
	}

}
