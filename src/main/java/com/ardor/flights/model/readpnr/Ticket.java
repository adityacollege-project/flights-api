/**
 * 
 */
package com.ardor.flights.model.readpnr;

import java.util.List;

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
public class Ticket {
	
	private String paxRef;
	private String paxType;
    private String gender;
    private String userTitle;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dateOfBirth;
	private String passengerName;
	
	private String ticketNumber;
	private String ticketCode;
	private long ticketDate;
	private List<String> sectors;
	private String airline;

}
