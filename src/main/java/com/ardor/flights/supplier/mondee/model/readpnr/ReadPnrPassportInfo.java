/**
 * 
 */
package com.ardor.flights.supplier.mondee.model.readpnr;

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
public class ReadPnrPassportInfo {
	
	@JsonProperty("passportNumber")
	private String passportNumber;
	
	@JsonProperty("countryOfIssue")
	private String countryOfIssue;
	
	@JsonProperty("nationality")
	private String nationality;
	
	@JsonProperty("dateOfExpiry")
	private String dateOfExpiry;
}
