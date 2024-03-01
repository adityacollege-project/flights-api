/**
 * 
 */
package com.ardor.flights.model.ancillary;

import java.util.List;
import java.util.Map;

import com.ardor.flights.common.ErrorCodeConstants;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@GroupSequence({SeatMapRequest.class, NotNull.class, NotEmpty.class})
public class SeatMapRequest {

	@NotNull(message = ErrorCodeConstants.SME_0001, groups = NotNull.class)
	@NotEmpty(message = ErrorCodeConstants.SME_0001, groups = NotEmpty.class)
	private List<String> jIds;

	private Map<String, String> brandedFareIds;

	private String supplier;
	private Map<String, String> jIdSupplierMap;

	//Mondee supplier
	private List<String> itineraryIds;
	private Map<String, String> fareIds;

}
