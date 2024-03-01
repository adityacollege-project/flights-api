/**
 * 
 */
package com.ardor.flights.supplier.mondee.model.readpnr;

import java.util.List;

import com.ardor.flights.supplier.mondee.model.search.MondeeFlightItinerary;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class MondeeReadPnrResponse {

	@JsonProperty("Pnr")
	private String PNR;
	
	@JsonProperty("PproductId")
	private String referenceId;
	
	@JsonProperty("ProductId")
	private String referenceNumber;
	
	@JsonProperty("ProductStatus")
	private String status;
	
	@JsonProperty("Itinerary")
	private MondeeFlightItinerary flight;
	
	@JsonProperty("Travellers")
	private List<MondeeReadPnrTraveller> travellers;
	
	@JsonProperty("TicketInfos")
	private List<MondeeReadPnrTicketInfos> ticketInfos;
	
	@JsonProperty("ticketingDeadLine")
	private String ticketingDeadline;

	@JsonProperty("error")
	private boolean error;

	@JsonProperty("errorMsg")
	private String errorMsg;
	
}
