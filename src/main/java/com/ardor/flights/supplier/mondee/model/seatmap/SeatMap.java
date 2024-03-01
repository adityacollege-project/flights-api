/**
 * 
 */
package com.ardor.flights.supplier.mondee.model.seatmap;

import com.ardor.flights.model.ancillary.Cabin;
import java.util.List;

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
public class SeatMap {
	
	@JsonProperty("cabins")
	private List<Cabin> cabins;
	
	@JsonProperty("origin")
	private String from;
	
	@JsonProperty("destination")
	private String to;
	
	@JsonProperty("airline")
	private String airline;
	
	@JsonProperty("flightNumber")
	private String flightNumber;
	
	@JsonProperty("segmentRef")
	private String segmentReference;
	
	@JsonProperty("paxSeatSegmentCharges")
	private List<SegmentCharge> segmentCharges;

}
