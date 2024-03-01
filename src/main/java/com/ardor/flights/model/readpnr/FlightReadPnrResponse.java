/**
 * 
 */
package com.ardor.flights.model.readpnr;

import java.util.List;

import com.ardor.flights.model.search.FlightItinerary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightReadPnrResponse {
	
	private String PNR;
	private String status;
	private FlightItinerary flight;
	private List<Passenger> passengers;
	private List<Ticket> tickets;
	private String ticketingDeadline;
}
