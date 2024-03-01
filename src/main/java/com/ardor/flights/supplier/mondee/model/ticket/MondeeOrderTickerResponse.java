/**
 * 
 */
package com.ardor.flights.supplier.mondee.model.ticket;

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
public class MondeeOrderTickerResponse {
	
	@JsonProperty("msg")
	private String message;
	
	@JsonProperty("error")
	private boolean error;
	
	@JsonProperty("errorMsg")
	private String errorMessage;

}
