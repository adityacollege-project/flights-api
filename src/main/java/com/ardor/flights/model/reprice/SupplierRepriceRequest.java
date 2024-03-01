/**
 * 
 */
package com.ardor.flights.model.reprice;

import java.util.List;
import java.util.Map;

import com.ardor.flights.model.search.FlightSearchRequest;

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
public class SupplierRepriceRequest {

	private String supplier;
	private boolean flightInfoRequired;
	private List<String> jIds;
	private Map<String, String> jIdSupplierMap;
	private String searchKey;
	private long responseTime;

	//Mondee supplier
	private List<String> itineraryIds;
	private Map<String, String> fareIds;
	private FlightSearchRequest.PaxDetails paxDetails;
	private String tripType;
	
}
