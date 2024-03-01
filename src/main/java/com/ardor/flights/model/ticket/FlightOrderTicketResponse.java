/**
 * 
 */
package com.ardor.flights.model.ticket;

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
public class FlightOrderTicketResponse {

  private boolean ticketOrdered;
  private String message;

}
