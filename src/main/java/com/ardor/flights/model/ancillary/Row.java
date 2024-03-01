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
public class Row {
	
	@JsonProperty("rowNumber")
	private int rowNumber;
	
	@JsonProperty("seats")
	private List<Seat> seats;
	
	@JsonProperty("exitRow")
	private boolean exitRow;

}
