/**
 * 
 */
package com.ardor.flights.model.reprice;

import java.util.List;

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
public class FlightRepriceResponse {

	private List<RepriceFlightItinerary> flights;
	
	private boolean validPrice;

}
