/**
 *
 */
package com.ardor.flights.model.cancel;

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
@GroupSequence({FlightCancelRequest.class, NotNull.class})
public class FlightCancelRequest {

	@NotNull(message = ErrorCodeConstants.FCE_0001, groups = NotNull.class)
	private String PNR;
	private String reason;

	@NotNull(message = ErrorCodeConstants.FCE_0005, groups = NotNull.class)
	private String referenceNumber;

	private String supplierReference;

}
