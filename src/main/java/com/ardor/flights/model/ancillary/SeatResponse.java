/**
 * 
 */
package com.ardor.flights.model.ancillary;

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
public class SeatResponse {
	
	@JsonProperty("segmentSeatMap")
	private List<SeatMap> seatMap;
	
	private String currency;
	
//	@JsonProperty("SeatAssignementFee")
//	private double assignementFee;
//
//	@JsonProperty("SeatAssignementFeePerSegment")
//	private double assignementFeePerSegment;
}
