/**
 * 
 */
package com.ardor.flights.mapper;

import com.ardor.flights.model.search.Brands;
import com.ardor.flights.supplier.mondee.model.search.MondeeBrandFares;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.ardor.flights.model.reprice.RepriceFlightItinerary;
import com.ardor.flights.model.reprice.RepriceResponse;
import com.ardor.flights.model.search.BaggageAllowance;
import com.ardor.flights.model.search.CityPairs;
import com.ardor.flights.model.search.Fares;
import com.ardor.flights.model.search.FlightSegment;
import com.ardor.flights.supplier.mondee.model.search.MondeeBaggageAllowance;
import com.ardor.flights.supplier.mondee.model.search.MondeeCityPairs;
import com.ardor.flights.supplier.mondee.model.search.MondeeFares;
import com.ardor.flights.supplier.mondee.model.search.MondeeFlightItinerary;
import com.ardor.flights.supplier.mondee.model.reprice.MondeeRepriceResponse;

/**
 * 
 */
@Mapper(componentModel = "spring")
public interface RepriceResponseMapper {
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	@Mappings({
		@Mapping(target = "flights", expression = "java(mapFlightItineraries(mondeeRepriceResponse.getRepriceResponses()))")
	})
	RepriceResponse mapResponsefromMondeeToArdor(MondeeRepriceResponse mondeeRepriceResponse);


	default List<RepriceFlightItinerary> mapFlightItineraries(List<MondeeFlightItinerary> itineraries) {
		return itineraries.stream().map(this::mapFlightItinerary).collect(Collectors.toList());
	}

	default RepriceFlightItinerary mapFlightItinerary(MondeeFlightItinerary itinerary) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String timestamp = sdf.format(new Date());
		String uniqueString = uuid.substring(0, 16) + timestamp + uuid.substring(16);
		return RepriceFlightItinerary.builder()
				.itineraryId(itinerary.getItineraryId())
				.jId(uniqueString)
				.validatingCarrierName(itinerary.getValidatingCarrierName())
				.validatingCarrierCode(itinerary.getValidatingCarrierCode())
				.brandName(itinerary.getBrandName())
				.cabinClass(itinerary.getCabinClass())
				.fareType(itinerary.getFareType())
				.fares(this.mapFares(itinerary.getFares()))
				.segments(this.mapSegements(itinerary))
				.isFareChanged(itinerary.isFareChanged())
				.brands(this.mapBrands(itinerary.getBrands()))
				.build();
	}

	default List<CityPairs> mapSegements(MondeeFlightItinerary itinerary) {
		List<CityPairs> citypairs = new ArrayList<>();
		for (MondeeCityPairs segments : itinerary.getSegments()) {
			CityPairs segment = mapSegment(segments);
			for (FlightSegment flightSegment : segment.getFlightSegments()) {
				List<BaggageAllowance> baggageAllowances = Optional.ofNullable(itinerary.getFares())
						.map(f -> f.stream()
								.filter(item -> item.getPaxType().equalsIgnoreCase("ADT")).flatMap(
										map -> map.getBaggageAllowance().entrySet().stream()
												.filter(entry -> entry.getKey()
														.equalsIgnoreCase(String.join("-", flightSegment.getFromAirportCode(),
																flightSegment.getToAirportCode())))
												.flatMap(entry -> entry.getValue().stream())).map(this::mapBaggageAllowance)
								.collect(Collectors.toList())).orElse(Collections.emptyList());
				flightSegment.setBaggageAllowance(baggageAllowances);
			}
			citypairs.add(segment);
		}
		return citypairs;
	}

	CityPairs mapSegment(MondeeCityPairs cityPairs);

	default List<Fares> mapFares(List<MondeeFares> fares) {
		return Optional.ofNullable(fares)
				.map(f -> f.stream().map(this::mapFare).collect(Collectors.toList()))
				.orElse(Collections.emptyList());
	}

	@Mapping(source = "currencyCode", target = "currencyCode")
	Fares mapFare(MondeeFares fare);

	BaggageAllowance mapBaggageAllowance(MondeeBaggageAllowance allowance);

	default List<Brands> mapBrands(List<MondeeBrandFares> brands) {
		return Optional.ofNullable(brands)
				.map(f -> f.stream().map(this::mapBrand).collect(Collectors.toList()))
				.orElse(Collections.emptyList());
	}

	default Brands mapBrand(MondeeBrandFares brands) {
		return Optional.ofNullable(brands).map(f -> Brands.builder()
				.brandId(f.getBrandId())
				.name(f.getName())
				.brandAttributeId(f.getBrandAttributeId())
				.fares(this.mapFares(f.getFares()))
				.baggageAllowance(Optional.ofNullable(f.getFares()).map(fareList -> {
					return fareList.get(0).getBaggageAllowance().entrySet().stream().collect(
							Collectors.toMap(Map.Entry::getKey,
									entry -> entry.getValue().stream().map(this::mapBaggageAllowance)
											.collect(Collectors.toList())));
				}).orElse(Collections.emptyMap())).build()).orElse(null);
	}

}
