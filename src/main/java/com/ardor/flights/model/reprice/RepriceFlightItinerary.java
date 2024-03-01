/**
 * 
 */
package com.ardor.flights.model.reprice;

import com.ardor.flights.model.search.Brands;
import java.util.List;

import com.ardor.flights.model.search.CityPairs;
import com.ardor.flights.model.search.Fares;

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
public class RepriceFlightItinerary {

	  private String itineraryId;
	  private String jId;
	  private String validatingCarrierCode;
	  private String validatingCarrierName;
	  private List<CityPairs> segments;
	  private List<Fares> fares;
		private List<Brands> brands;
	  private String brandName;
	  private String cabinClass;
	  private String fareType;
	  private boolean isFareChanged;
	  
	  

}
