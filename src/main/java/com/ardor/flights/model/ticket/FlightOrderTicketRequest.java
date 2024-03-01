/**
 *
 */
package com.ardor.flights.model.ticket;

import com.ardor.flights.common.ErrorCodeConstants;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotNull;
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
@GroupSequence({FlightOrderTicketRequest.class, NotNull.class})
public class FlightOrderTicketRequest {

	@NotNull(message = ErrorCodeConstants.OTE_0001, groups = NotNull.class)
	private String PNR;
	@NotNull(message = ErrorCodeConstants.OTE_0006, groups = NotNull.class)
	private String referenceNumber;
	private String supplierReference;
}
