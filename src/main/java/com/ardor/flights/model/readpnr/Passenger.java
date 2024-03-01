/**
 * 
 */
package com.ardor.flights.model.readpnr;

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
public class Passenger {
	
	private String paxRef;
	private String paxType;
    private String gender;
    private String userTitle;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dateOfBirth;
    private boolean primaryPassenger;
    
    private String passportNumber;
    private String countryOfIssue;
    private String nationality;
    private String passportIssueDate;
    private String passportExpiryDate;
    
    private String tsaDocs;    
    
}
