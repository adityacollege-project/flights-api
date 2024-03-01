/**
 *
 */
package com.ardor.flights.search.supplier.mondee.helper;

import com.ardor.flights.common.Constants;
import com.ardor.flights.common.ErrorCodeConstants;
import com.ardor.flights.entity.booking.AirFopCollectedInfo;
import com.ardor.flights.entity.booking.AirInfo;
import com.ardor.flights.entity.booking.AirPaxBookingInfo;
import com.ardor.flights.entity.booking.AirPaxInfo;
import com.ardor.flights.entity.booking.AirTransactionDetail;
import com.ardor.flights.entity.booking.AirTransactionHeader;
import com.ardor.flights.entity.reprice.RepriceRequestResponseEntity;
import com.ardor.flights.entity.search.SearchRequestEntity;
import com.ardor.flights.entity.search.SearchResponseEntity;
import com.ardor.flights.entity.search.SearchResponseEntity.FlightsResponse;
import com.ardor.flights.exception.FlightBookingException;
import com.ardor.flights.exception.FlightRepriceException;
import com.ardor.flights.mapper.FlightBookingMapper;
import com.ardor.flights.model.ancillary.SeatMapRequest;
import com.ardor.flights.model.booking.FlightBookingRequest;
import com.ardor.flights.model.booking.FlightBookingRequest.BillingAddress;
import com.ardor.flights.model.booking.FlightBookingRequest.CreditCardDetails;
import com.ardor.flights.model.booking.FlightBookingRequest.PassengerDetails;
import com.ardor.flights.model.booking.FlightBookingResponse.BookingResponse;
import com.ardor.flights.model.cancel.FlightCancelRequest;
import com.ardor.flights.model.readpnr.FlightReadPnrRequest;
import com.ardor.flights.model.reprice.RepriceFlightItinerary;
import com.ardor.flights.model.reprice.RepriceResponse;
import com.ardor.flights.model.reprice.SupplierRepriceRequest;
import com.ardor.flights.model.search.Brands;
import com.ardor.flights.model.search.CityPairs;
import com.ardor.flights.model.search.Fares;
import com.ardor.flights.model.search.FlightSearchRequest.OriginDestination;
import com.ardor.flights.model.search.FlightSegment;
import com.ardor.flights.model.ticket.FlightOrderTicketRequest;
import com.ardor.flights.repository.mongo.search.SearchResponseRepository;
import com.ardor.flights.supplier.mondee.model.booking.MondeeBookingRequest;
import com.ardor.flights.supplier.mondee.model.booking.MondeeBookingRequest.BookItineraryPaymentDetail;
import com.ardor.flights.supplier.mondee.model.reprice.MondeeRepriceResponse;
import com.ardor.flights.supplier.mondee.model.search.MondeeFlightItinerary;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.validation.Valid;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MondeeHelper {

  private final FlightBookingMapper bookingMapper;
  private final SearchResponseRepository searchResponseRepository;
  private final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(
      Constants.DATE_TIME_PATTERN);
  private final SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.DATE_PATTERN_D_M_Y);
  private final SimpleDateFormat ardorDateFormatter = new SimpleDateFormat(Constants.DATE_PATTERN);


  public String prepareRepriceRequest(SupplierRepriceRequest request) {
    JsonObject requestJO = new JsonObject();
    JsonObject fareIdJO = new JsonObject();
    try {
      if (null != request.getFareIds() && !request.getFareIds().isEmpty()) {
        request.getFareIds().entrySet().stream().forEach(entry -> {
          fareIdJO.addProperty(entry.getKey(), entry.getValue());
        });
      }
      requestJO.add("ItineraryIds", request.getItineraryIds().stream()
          .collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
      requestJO.add("FareIds", fareIdJO);
      requestJO.addProperty("AdultPaxCount", request.getPaxDetails().getAdultCount());
      requestJO.addProperty("ChildPaxCount", request.getPaxDetails().getChildCount());
      requestJO.addProperty("InfantPaxCount", request.getPaxDetails().getInfantCount());
      requestJO.addProperty("tripType", request.getTripType());
    } catch (Exception e) {
      throw new FlightRepriceException(ErrorCodeConstants.RPE_00011);
    }
    return requestJO.toString();
  }

  public MondeeRepriceResponse checkAndValidateResponse(
      MondeeRepriceResponse mondeeRepriceResponse) {
    List<MondeeFlightItinerary> list = null;
    try {
      list = mondeeRepriceResponse.getRepriceResponses().stream()
          .filter(itin -> null != itin.getSegments() && !itin.getSegments().isEmpty())
          .collect(Collectors.toList());
      if (list.isEmpty()) {
        return null;
      }
      return MondeeRepriceResponse.builder().repriceResponses(list).build();
    } catch (Exception e) {

    }
    return null;
  }

  public void setJid(RepriceResponse repriceResponse, MondeeRepriceResponse mondeeRepriceResponse,
      SupplierRepriceRequest searchRequest) {
    repriceResponse.getFlights().forEach(sResponse -> {
      if (null != sResponse.getItineraryId() && searchRequest.getJIdSupplierMap()
          .containsKey(sResponse.getItineraryId())) {
        sResponse.setJId(searchRequest.getJIdSupplierMap().get(sResponse.getItineraryId()));
      }
    });
  }

  public String prepareSeatMapRequest(SeatMapRequest request) {
    JsonObject requestJO = new JsonObject();
    JsonObject fareIdJO = new JsonObject();
    try {
      if (null != request.getFareIds() && !request.getFareIds().isEmpty()) {
        request.getFareIds().entrySet().stream().forEach(entry -> {
          fareIdJO.addProperty(entry.getKey(), entry.getValue());
        });
      }
      requestJO.add("ItineraryIds", request.getItineraryIds().stream()
          .collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
      requestJO.add("FareIds", fareIdJO);
    } catch (Exception e) {
      throw new FlightRepriceException(ErrorCodeConstants.RPE_00011);
    }
    return requestJO.toString();
  }

  public List<FlightsResponse> findFlightsByJids(List<String> jIds) {
    String responseKey = new String(Base64.decodeBase64(jIds.get(0))).split("-")[0];
    SearchResponseEntity searchResponse = searchResponseRepository.findByResponseKey(responseKey);
    List<FlightsResponse> flightResp = Optional.ofNullable(searchResponse.getFlights()).map(
        f -> f.stream().filter(flight -> jIds.contains(flight.getJId()))
            .collect(Collectors.toList())).orElse(Collections.emptyList());
    if (flightResp.isEmpty() && !searchResponse.getBrandFlights().isEmpty()) {
      flightResp = Optional.of(searchResponse.getBrandFlights()).map(
              f -> f.values().stream().flatMap(List::stream)
                  .filter(flight -> jIds.contains(flight.getJId())).collect(Collectors.toList()))
          .orElse(Collections.emptyList());
    }
    return Optional.of(flightResp)
        .orElseThrow(() -> new FlightBookingException(ErrorCodeConstants.FBE_0003));
  }

  public MondeeBookingRequest prepareMondeeBookingRequest(FlightBookingRequest bookingRequest) {
    List<FlightsResponse> flightsResponseList = findFlightsByJids(bookingRequest.getJIds());
    List<String> itinsList = flightsResponseList.stream()
        .map(FlightsResponse::getSupplierItineraryId)
        .collect(Collectors.toList());

    Map<String, String> fareIdsMap = flightsResponseList.stream().collect(
        Collectors.toMap(FlightsResponse::getSupplierItineraryId,
            response -> response.getBrands().stream().flatMap(m -> m.entrySet().stream())
                .filter(f -> bookingRequest.getFareIds().values().contains(f.getValue()))
                .findFirst().map(brand -> brand.getValue().toString()).orElse(null)));

    return MondeeBookingRequest.builder()
        .ItineraryIds(itinsList)
        .FareIds(fareIdsMap)
        .BookItineraryPaxContactInfo(
            bookingMapper.mapContactInfoArdorToMondee(bookingRequest.getContactInfo()))
        .BookItineraryPaxDetail(bookingRequest.getPassengerDetails().stream()
            .map(bookingMapper::mapPaxDetailsArdorToMondee).collect(Collectors.toList()))
        .BookItineraryPaymentDetail(BookItineraryPaymentDetail.builder()
            .PaymentType(bookingRequest.getPaymentDetails().getPaymentType())
            .BookItineraryCCDetails(bookingMapper.mapCCDetailsArdorToMondee(
                bookingRequest.getPaymentDetails().getCreditCardDetails()))
            .BookItineraryBillingAddress(bookingMapper.mapBillingAddresArdorToMondee(
                bookingRequest.getPaymentDetails().getBillingAddress())).build())
        .build();
  }

  public String generateUniqueKey(int length) {
    String timestamp = String.valueOf(Instant.now().toEpochMilli()).substring(7);
    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
    return ("F" + (timestamp + uuid).substring(0, length - 1)).toUpperCase();
  }

  public AirTransactionHeader prepareBookingTransactionHeader(FlightBookingRequest bookingRequest,
      RepriceRequestResponseEntity repriceResponse) {
    ;
    BillingAddress billingAddress = bookingRequest.getPaymentDetails().getBillingAddress();
    List<String> addressList = new ArrayList<>();
    addressList.add(billingAddress.getAddress1());

    if (billingAddress.getAddress2() != null) {
      addressList.add( billingAddress.getAddress2());
    }
    if(null != billingAddress.getCity()) {
      addressList.add(billingAddress.getCity());
    }
    if(null != billingAddress.getState()) {
      addressList.add(billingAddress.getState());
    }
    if(null != billingAddress.getCountry()) {
      addressList.add(billingAddress.getCountry());
    }
    if(null != billingAddress.getZipCode()) {
      addressList.add(billingAddress.getZipCode());
    }

    List<Brands> brandsList = repriceResponse.getRawResponse().getFlights().stream()
        .filter(f -> bookingRequest.getJIds().contains(f.getJId()))
        .flatMap(fm -> fm.getBrands().stream())
        .filter(b -> bookingRequest.getFareIds().values().contains(b.getBrandId()))
        .collect(Collectors.toList());

    double totalBaseFare = brandsList.stream()
        .mapToDouble(brand -> brand.getFares().stream()
            .mapToDouble(fare -> calculateTotalBaseFareByPax(fare, bookingRequest))
            .sum())
        .sum();

    double totalTaxes = brandsList.stream()
        .mapToDouble(brand -> brand.getFares().stream()
            .mapToDouble(fare -> caculateTotalTaxesByPax(fare, bookingRequest))
            .sum())
        .sum();

    return AirTransactionHeader.builder()
        .airTransactionId(generateUniqueKey(12))
        .userCode("USER")
        .created(new Date(System.currentTimeMillis()))
        .modified(new Date(System.currentTimeMillis()))
        .modifiedBy("USER")
        .firstName(bookingRequest.getPassengerDetails().get(0).getFirstName().toUpperCase())
        .lastName(bookingRequest.getPassengerDetails().get(0).getLastName().toUpperCase())
        .email(bookingRequest.getContactInfo().getEmail())
        .phone(bookingRequest.getContactInfo().getPhoneNumber())
        .address(addressList.stream().collect(
            Collectors.joining(",")))
        .transactionStatus("BKD")
        .quoted((totalBaseFare + totalTaxes))
        .quotedCost((totalBaseFare + totalTaxes))
        .pax(String.valueOf(bookingRequest.getPassengerDetails().size()))
        .build();
  }

  public AirTransactionDetail prepareBookingTransactionDetail(FlightBookingRequest bookingRequest,
      BookingResponse bookingResponse, RepriceFlightItinerary flight,
      SearchRequestEntity searchRequest, OriginDestination originDestination) {



    Brands brand = flight.getBrands().stream()
        .filter(b -> b.getBrandId().equals(bookingRequest.getFareIds().get(flight.getJId())))
        .findFirst().orElseGet(null);
    Fares fares = brand.getFares().stream().filter(f -> f.getPaxType().equalsIgnoreCase("ADT"))
        .findFirst().orElse(null);
    double totalBaseFare = brand.getFares().stream()
        .mapToDouble(fare -> calculateTotalBaseFareByPax(fare, bookingRequest))
        .reduce(0.0, Double::sum);
    double totalTaxes = brand.getFares().stream()
        .mapToDouble(fare -> caculateTotalTaxesByPax(fare, bookingRequest))
        .reduce(0.0, Double::sum);

    try {
      return AirTransactionDetail.builder()
          .airTransactionDetailId(generateUniqueKey(20))
          .bookingType("AIR")
          .transactionType("SAL")
          .transactionStatus("Success".equalsIgnoreCase(bookingResponse.getStatus()) ? "BKD" : "FLD")
          .userCode("USER") // persist from user info data by accesstoken
          .created(new Timestamp(System.currentTimeMillis()))
          .modified(new Timestamp(System.currentTimeMillis()))
          .modifiedBy("USER")
          .pnr(StringUtils.isNotEmpty(bookingResponse.getPNR()) ? bookingResponse.getPNR().toUpperCase() : bookingResponse.getStatus().toUpperCase())
          .supplier("MND")
          .quoted((totalBaseFare + totalTaxes))
          .quotedCost((totalBaseFare + totalTaxes))
          .pax(bookingRequest.getPassengerDetails().size())
          .adult(getpaxCountFromBookingRequest(bookingRequest, "ADT"))
          .child(getpaxCountFromBookingRequest(bookingRequest, "CHD"))
          .infant(getpaxCountFromBookingRequest(bookingRequest, "INF"))
          .origin(originDestination.getFrom().toUpperCase())
          .destination(originDestination.getTo().toUpperCase())
          .onwardDate(new Timestamp(dateTimeFormatter.parse(flight.getSegments().get(0).getFlightSegments().get(0).getDeparture()).getTime()))
          .returnDate(new Timestamp(dateTimeFormatter.parse(flight.getSegments().get(flight.getSegments().size() - 1).getFlightSegments().get(flight.getSegments().get(0).getFlightSegments().size() - 1).getArrival()).getTime()))
          .cabin(originDestination.getCabinClass())
          .bookingClass(Optional.of(fares).map(Fares::getBookingClasses).orElse(null))
          .supplierRef(StringUtils.isNotEmpty(bookingResponse.getReferenceNumber()) ? bookingResponse.getReferenceNumber() : bookingResponse.getStatus().toUpperCase())
          .journeyType(getTripType(searchRequest))
          .itinerary(flight.getSegments().get(0).getFlightSegments().stream().map(ft -> ft.getFromAirportCode() + "-" + ft.getToAirportCode()).collect(
              Collectors.joining(",")))
          .airlineCode(flight.getValidatingCarrierCode())
          .build();
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public List<AirInfo> prepareBookingAirInfo(RepriceFlightItinerary flight, FlightBookingRequest bookingRequest,
      BookingResponse bookingResponse) {

    Brands brand = flight.getBrands().stream()
        .filter(b -> b.getBrandId().equals(bookingRequest.getFareIds().get(flight.getJId())))
        .findFirst().orElseGet(null);
    Fares fares = brand.getFares().stream().filter(f -> f.getPaxType().equalsIgnoreCase("ADT"))
        .findFirst().orElse(null);
    double totalBaseFare = brand.getFares().stream()
        .mapToDouble(fare -> calculateTotalBaseFareByPax(fare, bookingRequest))
        .reduce(0.0, Double::sum);
    double totalTaxes = brand.getFares().stream()
        .mapToDouble(fare -> caculateTotalTaxesByPax(fare, bookingRequest))
        .reduce(0.0, Double::sum);
    List<AirInfo> airInfos = new ArrayList<>();
    for(CityPairs cityPair : flight.getSegments()) {
      for(int i=0;i<cityPair.getFlightSegments().size();i++) {
        try {
          FlightSegment segment = cityPair.getFlightSegments().get(i);
          airInfos.add(AirInfo.builder()
              .airId(generateUniqueKey(20))
              .origin(segment.getFromAirportCode())
              .destination(segment.getToAirportCode())
              .airlineCode(segment.getOperatingAirline())
              .departDateTime(
                  new Timestamp(dateTimeFormatter.parse(segment.getDeparture()).getTime()))
              .arrivalDateTime(
                  new Timestamp(dateTimeFormatter.parse(segment.getArrival()).getTime()))
              .cabin(segment.getCabinClass().substring(0, 1).replace("B", "E"))
              .bookingClass(segment.getBookingCode())
              .flightNo(String.valueOf(segment.getFlightNumber()))
              .fare((totalBaseFare/cityPair.getFlightSegments().size()))
              .taxes((totalTaxes/cityPair.getFlightSegments().size()))
              .yr(0.0)
              .pnr(StringUtils.isNotEmpty(bookingResponse.getPNR()) ? bookingResponse.getPNR() : bookingResponse.getStatus().toUpperCase())
              .legNo(i+1)
              .operatingAirline(segment.getOperatingAirline())
              .fareType(flight.getFareType())
              .fareBasisCode(segment.getFareBasisCode())
              //          .tourCode()
              .fareInfo(null)//Need to persist fare ladder but not getting as of now.
              //          .endorsement()
              .created(new Timestamp(System.currentTimeMillis()))
              .modified(new Timestamp(System.currentTimeMillis()))
              .build());
        } catch (ParseException e) {
          throw new RuntimeException(e);
        }
      }
    }
    return airInfos;
  }

  public AirPaxInfo prepareBookingAirPaxInfo(PassengerDetails paxInfo) {
    try {
      return AirPaxInfo.builder()
          .paxId(generateUniqueKey(20))
          .firstName(StringUtils.isNotEmpty(paxInfo.getFirstName()) ? paxInfo.getFirstName().toUpperCase() : null)
          .lastName(StringUtils.isNotEmpty(paxInfo.getLastName())?paxInfo.getLastName().toUpperCase() : null)
          .middleName(StringUtils.isNotEmpty(paxInfo.getMiddleName())?paxInfo.getMiddleName().toUpperCase() : null)
          .gender(paxInfo.getGender().toUpperCase())
          .dob(new Date(dateFormatter.parse(paxInfo.getDateOfBirth()).getTime()))
          .passport(StringUtils.isNotEmpty(paxInfo.getPassportNumber())?paxInfo.getPassportNumber() : null)
          .nationality(StringUtils.isNotEmpty(paxInfo.getNationality())? paxInfo.getNationality().toUpperCase() : null)
          .paxType(StringUtils.isNotEmpty(paxInfo.getPaxType())?paxInfo.getPaxType().toUpperCase():null)
          .build();
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  private double calculateTotalBaseFareByPax(Fares fares, FlightBookingRequest bookingRequest) {
    double totalPaxBaseFare = 0.0;
    int paxCount = bookingRequest.getPassengerDetails().stream()
        .filter(f -> f.getPaxType().equals(fares.getPaxType())).toList().size();
    if (paxCount > 0) {
      totalPaxBaseFare += fares.getBaseFare() * paxCount;
    }
    return totalPaxBaseFare;
  }

  private double caculateTotalTaxesByPax(Fares fares, FlightBookingRequest bookingRequest) {
    double totalPaxTaxes = 0.0;
    int paxCount = bookingRequest.getPassengerDetails().stream()
        .filter(f -> f.getPaxType().equals(fares.getPaxType())).toList().size();
    if (paxCount > 0) {
      totalPaxTaxes += fares.getTaxes() * paxCount;
    }
    return totalPaxTaxes;
  }

  public String prepareCancelPnrRequest(@Valid FlightCancelRequest cancelRequest) {
    JsonObject requestJO = new JsonObject();
    requestJO.addProperty("pnr", cancelRequest.getPNR());
    requestJO.addProperty("productId", cancelRequest.getSupplierReference());
    requestJO.addProperty("reason", cancelRequest.getReason());
    return requestJO.toString();
  }

  public String prepareReadPnrRequest(@Valid FlightReadPnrRequest readPnrRequest) {
    JsonObject requestJO = new JsonObject();
    requestJO.addProperty("pnr", readPnrRequest.getPNR());
    requestJO.addProperty("productId", readPnrRequest.getSupplierReference());
    return requestJO.toString();
  }

  public String prepareOrderTicketRequest(@Valid FlightOrderTicketRequest readPnrRequest) {
    JsonObject requestJO = new JsonObject();
    requestJO.addProperty("pnr", readPnrRequest.getPNR());
    requestJO.addProperty("productId", readPnrRequest.getSupplierReference());
    return requestJO.toString();
  }

  public String getTripType(SearchRequestEntity searchRequest) {
    try {
      if(searchRequest.getOriginDestinations().size() > 1) {
        if(searchRequest.getOriginDestinations().size() == 2 &&
            searchRequest.getOriginDestinations().get(0).getFrom().equals(searchRequest.getOriginDestinations().get(1).getTo()) &&
            searchRequest.getOriginDestinations().get(1).getFrom().equals(searchRequest.getOriginDestinations().get(0).getTo())) {
          return "RT";
        } else {
          return "MC";
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "OW";
  }

  public AirPaxBookingInfo prepareAirPaxBkgInfo(RepriceFlightItinerary flight, AirPaxInfo pax,BookingResponse bookingResponse,OriginDestination originDestination,FlightBookingRequest bookingRequest) {
    Brands brand = flight.getBrands().stream()
        .filter(b -> b.getBrandId().equals(bookingRequest.getFareIds().get(flight.getJId())))
        .findFirst().orElseGet(null);
        Fares fares = brand.getFares().stream().filter(f -> f.getPaxType().equalsIgnoreCase(pax.getPaxType()))
        .findFirst().orElse(Fares.builder().build());
    double totalBaseFare = brand.getFares().stream()
        .mapToDouble(fare -> calculateTotalBaseFareByPax(fare, bookingRequest))
        .reduce(0.0, Double::sum);
    double totalTaxes = brand.getFares().stream()
        .mapToDouble(fare -> caculateTotalTaxesByPax(fare, bookingRequest))
        .reduce(0.0, Double::sum);

    return AirPaxBookingInfo.builder()
        .paxBkgId(generateUniqueKey(20))
        .paxId(pax.getPaxId())
        .pnr(StringUtils.isNotEmpty(bookingResponse.getPNR()) ? bookingResponse.getPNR() : bookingResponse.getStatus().toUpperCase())
        .airline(flight.getValidatingCarrierCode())
        .fareType(flight.getFareType())
        .fareInfo(null)
        .itinerary(flight.getSegments().get(0).getFlightSegments().stream().map(ft -> ft.getFromAirportCode() + "-" + ft.getToAirportCode()).collect(
        Collectors.joining(",")))
        .orgDest(originDestination.getFrom().toUpperCase()+originDestination.getTo().toUpperCase())
        .fop("HOLD".equalsIgnoreCase(bookingRequest.getPaymentDetails().getPaymentType()) ? "CK" : bookingRequest.getPaymentDetails().getPaymentType().toUpperCase())
        .transStatus("Success".equalsIgnoreCase(bookingResponse.getStatus()) ? "BKD" : "FLD")
        .baseFare(fares.getBaseFare())
        .tax(fares.getTaxes())
        .totFare(fares.getBaseFare()+fares.getTaxes())
        .transType("SAL")
        .created(new Date(System.currentTimeMillis()))
        .modified(new Date(System.currentTimeMillis()))
        .build();
  }

  public String getPaxBkgId(AirInfo ft, AirPaxInfo pax, List<AirPaxBookingInfo> paxFlights) {
    return paxFlights.stream().filter(flight -> flight.getPaxId().equals(pax.getPaxId())).findFirst().get().getPaxBkgId();
  }

  public AirFopCollectedInfo getFopCollectedInfo(FlightBookingRequest bookingRequest) {
    if("CC".equalsIgnoreCase(bookingRequest.getPaymentDetails().getPaymentType())) {
      CreditCardDetails creditCardDetails = bookingRequest.getPaymentDetails().getCreditCardDetails();
      return AirFopCollectedInfo.builder()
          .type(bookingRequest.getPaymentDetails().getPaymentType())
          .firstEightDigits(creditCardDetails.getCardNumber().substring(0, 7))
          .lastFourDigits(creditCardDetails.getCardNumber().substring(creditCardDetails.getCardNumber().length() - 5, creditCardDetails.getCardNumber().length() -1))
          .ccInfo(creditCardDetails.getCardType())
          .ccNum(creditCardDetails.getCardNumber())
          .ccCode(creditCardDetails.getCardType())
          .ccExp(getExpiryForSave(creditCardDetails.getExpiryDate()))
          .cardHolderName(bookingRequest.getPaymentDetails().getBillingAddress().getName())
          .fcId(generateUniqueKey(20))
          .build();
    } else if("CK".equalsIgnoreCase(bookingRequest.getPaymentDetails().getPaymentType())) {
      return AirFopCollectedInfo.builder()
          .type(bookingRequest.getPaymentDetails().getPaymentType())
          .created(new Date(System.currentTimeMillis()))
          .modified(new Date(System.currentTimeMillis()))
          .fcId(generateUniqueKey(20))
          .build();
    } else {
      return AirFopCollectedInfo.builder()
          .type("CK")
          .created(new Date(System.currentTimeMillis()))
          .modified(new Date(System.currentTimeMillis()))
          .fcId(generateUniqueKey(20))
          .build();
    }

  }

  private int getpaxCountFromBookingRequest(FlightBookingRequest bookingRequest, String paxType) {
    try {
      return bookingRequest.getPassengerDetails().stream().filter(pax -> paxType.equalsIgnoreCase(pax.getPaxType())).toList().size();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  private String getExpiryForSave(String expDate) {
    return expDate.split("/")[0] + expDate.split("/")[1].substring(2, 4);
  }

}
