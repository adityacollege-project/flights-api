package com.ardor.flights.model.cancel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightCancelResponse {

	private boolean cancelled;
	private String message;
}
