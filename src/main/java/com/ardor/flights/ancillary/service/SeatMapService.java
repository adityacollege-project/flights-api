/**
 * 
 */
package com.ardor.flights.ancillary.service;

import com.ardor.flights.exception.SeatMapException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import com.ardor.flights.model.ancillary.SeatMapRequest;
import com.ardor.flights.model.ancillary.SeatResponse;
import com.ardor.flights.common.ErrorCodeConstants;
import com.ardor.flights.entity.search.SearchResponseEntity;
import com.ardor.flights.entity.search.SearchResponseEntity.FlightsResponse;
import com.ardor.flights.repository.mongo.search.SearchResponseRepository;
import com.ardor.flights.search.supplier.mondee.MondeeSupplier;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */

@Service
@Slf4j
public class SeatMapService {
	

	  private final MondeeSupplier mondeeSupplier;
	  private final SearchResponseRepository searchResponseRepository;
	  
	  public SeatMapService(MondeeSupplier mondeeSupplier, SearchResponseRepository searchResponseRepository) {
		  this.mondeeSupplier = mondeeSupplier;
		  this.searchResponseRepository = searchResponseRepository;
	  }

	public SeatResponse seatMap(SeatMapRequest request) {
		request = fetchItineraryInfo(request);
		return mondeeSupplier.seatMap(request);
	}

	private SeatMapRequest fetchItineraryInfo(SeatMapRequest searchRequest) {
		List<FlightsResponse> flightResp = null;
		try {
			String key = new String(Base64.decodeBase64(searchRequest.getJIds().get(0)));
			SearchResponseEntity searchResponse = searchResponseRepository.findByResponseKey(key.split("-")[0]);
			flightResp = Optional.ofNullable(searchResponse.getFlights()).map(f -> f.stream().filter(flight -> searchRequest.getJIds().contains(flight.getJId())).collect(
					Collectors.toList())).orElse(Collections.emptyList());
			if(flightResp.isEmpty() && !searchResponse.getBrandFlights().isEmpty()) {
				//get flight s from brand map;
				flightResp = Optional.ofNullable(searchResponse.getBrandFlights()).map(f -> f.values().stream().flatMap(List::stream).filter(flight -> searchRequest.getJIds().contains(flight.getJId())).collect(
						Collectors.toList())).orElse(Collections.emptyList());
			}
		} catch (Exception sme) {
			throw new SeatMapException(ErrorCodeConstants.SME_0002);
		}
	    if(flightResp == null || flightResp.isEmpty()) {
	      throw new SeatMapException(ErrorCodeConstants.SME_0002);
	    }
		try {
			Map<String, String> faresMap = new HashMap<String, String>();
			for(int i = 0; i < searchRequest.getJIds().size(); i++) {
				if(null != searchRequest.getBrandedFareIds() && !searchRequest.getBrandedFareIds().isEmpty() &&
						searchRequest.getBrandedFareIds().containsKey(searchRequest.getJIds().get(i))) {
					faresMap.put(flightResp.stream().collect(Collectors.toMap(FlightsResponse::getJId, FlightsResponse::getSupplierItineraryId)).get(searchRequest.getJIds().get(i)), searchRequest.getBrandedFareIds().get(searchRequest.getJIds().get(i)));
				}
			}
			searchRequest.setFareIds(faresMap);
		} catch (Exception sme) {
			throw new SeatMapException(ErrorCodeConstants.SME_0005);
		}
		searchRequest.setItineraryIds(flightResp.stream().map(FlightsResponse::getSupplierItineraryId).collect(Collectors.toList()));
		return searchRequest;
	}

}
