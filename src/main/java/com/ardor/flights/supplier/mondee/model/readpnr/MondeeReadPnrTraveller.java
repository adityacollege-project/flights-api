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
public class MondeeReadPnrTraveller {
	
	@JsonProperty("nameNumber")
	private String nameNumber;

	@JsonProperty("paxTypeId")
	private int paxTypeId;
	
	@JsonProperty("paxType")
	private String paxType;

	@JsonProperty("firstName")
	private String firstName;
	
	@JsonProperty("middleName")
	private String middleName;

	@JsonProperty("lastName")
	private String lastName;
	
	@JsonProperty("gender")
	private String gender;

	@JsonProperty("passportInfo")
	private ReadPnrPassportInfo passportInfo;
	
	@JsonProperty("profileAdded")
	private boolean profileAdded;

	@JsonProperty("travelPreferences")
	private MondeeReadPnrTravelPreference travelPreference;
	
	@JsonProperty("leadPassenger")
	private boolean leadPassenger;
	
	@JsonProperty("ptNumber")
	private int ptNumber;

	@JsonProperty("tsaDocs")
	private String tsaDocs;
	
	@JsonProperty("pickedForExchRefund")
	private boolean pickedForExchRefund;
	
	@JsonProperty("dateOfBirth")
	private String dob;

}
