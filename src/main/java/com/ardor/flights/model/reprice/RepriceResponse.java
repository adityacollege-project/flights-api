package com.ardor.flights.model.reprice;

import com.ardor.flights.model.reprice.RepriceFlightItinerary;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepriceResponse {
	
	private List<RepriceFlightItinerary> flights;

}
