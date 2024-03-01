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
public class Cabin {
	
	@JsonProperty("seatOrder")
	private List<String> seatOrder;
	
	@JsonProperty("cabinType")
	private String cabinType;
	
	@JsonProperty("rows")
	private List<Row> rows;
	
	@JsonProperty("rowStart")
	private int rowStart;
	
	@JsonProperty("rowEnd")
	private int rowEnd;
	
	@JsonProperty("facilities")
	private List<String> facilities;
	

}
