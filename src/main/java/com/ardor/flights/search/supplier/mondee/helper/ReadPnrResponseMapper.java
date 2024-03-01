/**
 * 
 */
package com.ardor.flights.search.supplier.mondee.helper;

import com.ardor.flights.model.readpnr.Passenger;
import com.ardor.flights.model.readpnr.Ticket;
import com.ardor.flights.model.search.FlightItinerary;
import com.ardor.flights.supplier.mondee.model.readpnr.MondeeReadPnrTicketInfos;
import com.ardor.flights.supplier.mondee.model.readpnr.MondeeReadPnrTraveller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.ardor.flights.model.readpnr.FlightReadPnrResponse;
import com.ardor.flights.model.search.BaggageAllowance;
import com.ardor.flights.model.search.Brands;
import com.ardor.flights.model.search.CityPairs;
import com.ardor.flights.model.search.Fares;
import com.ardor.flights.model.search.FlightSegment;
import com.ardor.flights.supplier.mondee.model.readpnr.MondeeReadPnrResponse;
import com.ardor.flights.supplier.mondee.model.search.MondeeBaggageAllowance;
import com.ardor.flights.supplier.mondee.model.search.MondeeBrandFares;
import com.ardor.flights.supplier.mondee.model.search.MondeeCityPairs;
import com.ardor.flights.supplier.mondee.model.search.MondeeFares;
import com.ardor.flights.supplier.mondee.model.search.MondeeFlightItinerary;

/**
 * 
 */
@Mapper(componentModel = "spring")
public interface ReadPnrResponseMapper {

	@Mappings({
			@Mapping(target = "flight", expression = "java(mapFlightItinerary(mondeeReadPnrResponse.getFlight()))"),
			@Mapping(target = "passengers", expression = "java(mapTravellers(mondeeReadPnrResponse.getTravellers()))"),
			@Mapping(target = "tickets", expression = "java(mapTickets(mondeeReadPnrResponse.getTicketInfos()))")
	})
	FlightReadPnrResponse mapResponseMondeeToArdor(MondeeReadPnrResponse mondeeReadPnrResponse);

	default  List<Ticket> mapTickets(List<MondeeReadPnrTicketInfos> ticketInfos) {
		return ticketInfos.stream().map(this::mapTicket).collect(Collectors.toList());
	}

	default Ticket mapTicket(MondeeReadPnrTicketInfos ticketInfo) {
		return Ticket.builder()
				.paxRef(ticketInfo.getNameNumber())
				.paxType(ticketInfo.getPaxType())
				.gender(ticketInfo.getGender())
				.userTitle(ticketInfo.getTitle())
				.firstName(ticketInfo.getFirstName())
				.middleName(ticketInfo.getMiddleName())
				.lastName(ticketInfo.getLastName())
				.dateOfBirth(ticketInfo.getDateOfBirth())
				.passengerName(ticketInfo.getPassengerName())
				.ticketNumber(ticketInfo.getEticketNumber())
				.ticketCode(ticketInfo.getTicketingCode())
				.ticketDate(ticketInfo.getTicketingDate())
				.sectors(ticketInfo.getSectors())
				.airline(ticketInfo.getAirline())
				.build();
	}

	default  List<Passenger> mapTravellers(List<MondeeReadPnrTraveller> travellers) {
		return travellers.stream().map(this::mapTraveller).collect(Collectors.toList());
	}

	default Passenger mapTraveller(MondeeReadPnrTraveller traveler) {
		return Passenger.builder()
				.paxRef(traveler.getNameNumber())
				.paxType(traveler.getPaxType())
				.gender(traveler.getGender())
				.firstName(traveler.getFirstName())
				.middleName(traveler.getMiddleName())
				.lastName(traveler.getLastName())
				.dateOfBirth(traveler.getDob())
				.primaryPassenger(traveler.isLeadPassenger())
				.passportNumber(traveler.getPassportInfo().getPassportNumber())
				.countryOfIssue(traveler.getPassportInfo().getCountryOfIssue())
				.nationality(traveler.getPassportInfo().getNationality())
				.passportExpiryDate(traveler.getPassportInfo().getDateOfExpiry())
				.tsaDocs(traveler.getTsaDocs())
				.build();
	}

	default FlightItinerary mapFlightItinerary(MondeeFlightItinerary itinerary) {
		return FlightItinerary.builder()
				.itineraryId(itinerary.getItineraryId())
				.validatingCarrierName(itinerary.getValidatingCarrierName())
				.validatingCarrierCode(itinerary.getValidatingCarrierCode())
				.brandName(itinerary.getBrandName())
				.cabinClass(itinerary.getCabinClass())
				.fareType(itinerary.getFareType())
				.fares(this.mapFares(itinerary.getFares()))
				.segments(this.mapSegements(itinerary))
				.build();
	}

	default List<CityPairs> mapSegements(MondeeFlightItinerary itinerary) {
		List<CityPairs> citypairs = new ArrayList<>();
		for (MondeeCityPairs segments : itinerary.getSegments()) {
			CityPairs segment = mapSegment(segments);
//			for (FlightSegment flightSegment : segment.getFlightSegments()) {
//				List<BaggageAllowance> baggageAllowances = Optional.ofNullable(itinerary.getFares())
//						.map(f -> f.stream()
//								.filter(item -> item.getPaxType().equalsIgnoreCase("ADT")).flatMap(
//										map -> map.getBaggageAllowance().entrySet().stream()
//												.filter(entry -> entry.getKey()
//														.equalsIgnoreCase(String.join("-", flightSegment.getFromAirportCode(),
//																flightSegment.getToAirportCode())))
//												.flatMap(entry -> entry.getValue().stream())).map(this::mapBaggageAllowance)
//								.collect(Collectors.toList())).orElse(Collections.emptyList());
//				flightSegment.setBaggageAllowance(baggageAllowances);
//			}
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

}
