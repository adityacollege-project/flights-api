/**
 *
 */
package com.ardor.flights.model.readpnr;

import com.ardor.flights.common.ErrorCodeConstants;
import com.ardor.flights.model.cancel.FlightCancelRequest;

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
@GroupSequence({FlightReadPnrRequest.class, NotNull.class})
public class FlightReadPnrRequest {

	@NotNull(message = ErrorCodeConstants.FRE_0001, groups = NotNull.class)
	private String PNR;

	@NotNull(message = ErrorCodeConstants.FRE_0006, groups = NotNull.class)
	private String referenceNumber;
	private String supplierReference;

}
