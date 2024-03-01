/**
 * 
 */
package com.ardor.flights.supplier.mondee.model.readpnr;

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
public class MondeeReadPnrTicketInfos {
	
	@JsonProperty("eticketNumber")
	private String eticketNumber;

	@JsonProperty("fullTicketNumber")
	private String fullTicketNumber;
	
	@JsonProperty("ticketingCode")
	private String ticketingCode;

	@JsonProperty("ticketingDate")
	private long ticketingDate;
	
	@JsonProperty("nameNumber")
	private String nameNumber;

	@JsonProperty("passengerName")
	private String passengerName;
	
	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;
	
	@JsonProperty("airlineCode")
	private String airline;

	@JsonProperty("sectorList")
	private List<String> sectors;
	
	@JsonProperty("paymentTypeId")
	private int paymentTypeId;

	@JsonProperty("paxType")
	private String paxType;
	
	@JsonProperty("lccTicket")
	private boolean lccTicket;
	
	@JsonProperty("middleName")
	private String middleName;

	@JsonProperty("title")
	private String title;
	
	@JsonProperty("gender")
	private String gender;
	
	@JsonProperty("dateOfBirth")
	private String dateOfBirth;

	@JsonProperty("travelPreferences")
	private MondeeReadPnrTravelPreference travelPreference;
	
	@JsonProperty("passportInfo")
	private ReadPnrPassportInfo passportInfo;
}
