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
public class Seat {
	
	@JsonProperty("seatColumn")
	private String column;
	@JsonProperty("chargeAmount")
	private double charge;
	@JsonProperty("chargeCurrency")
	private String currency;
	@JsonProperty("facilities")
	private List<String> facilities;
	@JsonProperty("limitations")
	private List<String> limitations;
	@JsonProperty("paxSegmentChargeRefs")
	private List<String> paxSegmentChargeRefs;
	@JsonProperty("empty")
	private boolean empty;
	@JsonProperty("pantry")
	private boolean pantry;
	@JsonProperty("premium")
	private boolean premium;
	@JsonProperty("restRoom")
	private boolean restRoom;
	@JsonProperty("occupied")
	private boolean occupied;
	@JsonProperty("legRoomSeat")
	private boolean legRoomSeat;
	@JsonProperty("tpOptionalService")
	private String tpOptionalService;
	
}
