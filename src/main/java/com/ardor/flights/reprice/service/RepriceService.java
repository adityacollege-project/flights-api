package com.ardor.flights.reprice.service;

import com.ardor.flights.common.ErrorCodeConstants;
import com.ardor.flights.entity.reprice.RepriceMetricksEntity;
import com.ardor.flights.entity.reprice.RepriceRequestResponseEntity;
import com.ardor.flights.exception.FlightRepriceException;
import com.ardor.flights.model.reprice.FlightRepriceResponse;
import com.ardor.flights.model.reprice.RepriceRequest;
import com.ardor.flights.model.reprice.RepriceResponse;
import com.ardor.flights.model.reprice.SupplierRepriceRequest;
import com.ardor.flights.repository.mongo.reprice.RepriceMetricksRepository;
import com.ardor.flights.repository.mongo.reprice.RepriceRequestResponseRepository;
import com.ardor.flights.entity.search.SearchResponseEntity;
import com.ardor.flights.entity.search.SearchResponseEntity.FlightsResponse;
import com.ardor.flights.model.search.Brands;
import com.ardor.flights.model.search.Fares;
import com.ardor.flights.repository.mongo.search.SearchResponseRepository;
import com.ardor.flights.search.supplier.mondee.MondeeSupplier;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RepriceService {

  private static final String REPRICE = "reprice";
  private static final String MONDEE = "mondee";
  private final MondeeSupplier mondeeSupplier;
  private final RepriceRequestResponseRepository repriceRequestResponseRepository;
  private final RepriceMetricksRepository repriceMetricksRepository;
  private final SearchResponseRepository searchResponseRepository;

  public RepriceService(MondeeSupplier mondeeSupplier,
      RepriceRequestResponseRepository repriceRequestResponseRepository,
      RepriceMetricksRepository repriceMetricksRepository,
      SearchResponseRepository searchResponseRepository) {
    this.mondeeSupplier = mondeeSupplier;
    this.repriceRequestResponseRepository = repriceRequestResponseRepository;
    this.repriceMetricksRepository = repriceMetricksRepository;
    this.searchResponseRepository = searchResponseRepository;
  }

  public FlightRepriceResponse reprice(RepriceRequest searchRequest) {
    long startTime = System.currentTimeMillis();
    RepriceResponse repriceResponse = null;
    FlightRepriceResponse response = null;
    RepriceRequestResponseEntity.RepriceRequestResponseEntityBuilder entity = RepriceRequestResponseEntity.builder();
    entity.rawRequest(searchRequest);
    SupplierRepriceRequest request = prepareRepriceRequest(searchRequest);
    List<FlightsResponse> flightResp = fetchFlightsByJids(request);
    entity.supplier(request.getSupplier());
    try {
      if (MONDEE.equalsIgnoreCase(request.getSupplier())) {
        repriceResponse = mondeeSupplier.repriceFlight(request, entity);
      }
    } catch (FlightRepriceException exe) {
      throw new FlightRepriceException(exe.getMessage());
    }
    entity.jId(searchRequest.getJIds().get(0));
    response = prepareFlightRepriceResponse(repriceResponse, request);
    entity.searchKey(request.getSearchKey());
    entity.validPrice(response.isValidPrice());
    if (null != repriceResponse && null != repriceResponse.getFlights()) {
      entity.rawResponse(FlightRepriceResponse.builder().flights(repriceResponse.getFlights()).build());
    }
    repriceRequestResponseRepository.save(entity.build());
    request.setResponseTime(System.currentTimeMillis() - startTime);
    prepareAndSaveMetricks(request, flightResp, response);
    return response;
  }

  private void prepareAndSaveMetricks(SupplierRepriceRequest request,
      List<FlightsResponse> flightResp,
      FlightRepriceResponse response) {
    double baseFare = 0.0, taxes = 0.0;
    double changedBase = 0.0, changedTax = 0.0;
    String paxCount = "", bookingCode = "", changedBookingCode = "";
    String metricks = "price not changed";
    String fareCode = "", changedFareCode = "";
    try {
      if(null != request.getFareIds() && !request.getFareIds().isEmpty()) {
        List<Map<String, Object>> brands = flightResp.stream().flatMap(flight -> flight.getBrands().stream()).filter(b -> request.getFareIds().containsValue(b.get("brandId"))).toList();
        baseFare = brands.stream().mapToDouble(map -> (Double) map.get("totalBaseFare")).sum();
        taxes = brands.stream().mapToDouble(map -> (Double) map.get("totalTaxes")).sum();
        bookingCode = brands.stream().map(b -> (String) b.get("bookingClasses")).collect(Collectors.joining(","));
        fareCode = brands.stream().map(b -> (String) b.get("fareBasisCodes")).collect(Collectors.joining(","));
        List<Brands> repriceBrands = response.getFlights().stream().flatMap(flight -> flight.getBrands().stream()).filter(b ->  request.getFareIds().containsValue(b.getBrandId())).toList();
        changedBase = repriceBrands.stream().mapToDouble(b -> b.getFares().stream().mapToDouble(fare -> getPaxwiseFare(fare, fare.getBaseFare(), request)).sum()).sum();
        changedTax = repriceBrands.stream().mapToDouble(b -> b.getFares().stream().mapToDouble(fare -> getPaxwiseFare(fare, fare.getTaxes(), request)).sum()).sum();
        changedBookingCode = repriceBrands.stream().map(br -> br.getFares().get(0).getBookingClasses()).collect(Collectors.joining(","));
        changedFareCode = repriceBrands.stream().map(br -> br.getFares().get(0).getFareBasisCodes()).collect(Collectors.joining(","));
      } else {
        baseFare = flightResp.stream().mapToDouble(flight -> flight.getTotalBaseFare()).sum();
        taxes = flightResp.stream().mapToDouble(flight -> flight.getTotalTaxes()).sum();
        changedBase = response.getFlights().stream().mapToDouble(repItin -> {
          return repItin.getFares().stream().mapToDouble(item -> {
            return getPaxwiseFare(item, item.getBaseFare(), request);
          }).sum();
        }).sum();
        changedTax = response.getFlights().stream().mapToDouble(repItin -> {
          return repItin.getFares().stream().mapToDouble(item -> {
            return getPaxwiseFare(item, item.getTaxes(), request);
          }).sum();
        }).sum();
        bookingCode = flightResp.stream().map(flight -> flight.getBookingClasses()).collect(
            Collectors.joining(","));
        fareCode = flightResp.stream().map(flight -> flight.getFareBasisCodes()).collect(
            Collectors.joining(","));
        changedBookingCode = response.getFlights().stream()
            .map(flight -> flight.getFares().get(0).getBookingClasses()).collect(
                Collectors.joining(","));
        changedFareCode = response.getFlights().stream()
            .map(flight -> flight.getFares().get(0).getFareBasisCodes()).collect(
                Collectors.joining(","));
      }
      try {
        if (!response.getFlights().stream().filter(itin -> itin.isFareChanged())
            .collect(Collectors.toList()).isEmpty()) {
          double fareChange = (changedBase + changedTax) - (baseFare + taxes);
          metricks = "price difference :: " + fareChange;
        }
        if (null != request.getSearchKey()) {
          String[] keyArr = request.getSearchKey().split("-");
          paxCount = keyArr[keyArr.length - 3] + "," + keyArr[keyArr.length - 2] + "," + keyArr[
              keyArr.length - 1];
        }
      } catch (Exception e) {
        log.error(e.getMessage());
      }

      repriceMetricksRepository.save(RepriceMetricksEntity.builder()
              .jIds(request.getJIds())
              .brandIds(request.getFareIds())
              .brandedfares(null != request.getFareIds() && !request.getFareIds().isEmpty())
          .jId(request.getJIds().get(0))
          .phase(REPRICE)
          .supplier(MONDEE)
          .searchKey(request.getSearchKey())
          .baseFare(baseFare)
          .taxes(taxes)
          .cabinClass(flightResp.stream().map(flight -> flight.getCabinClass()).collect(
              Collectors.joining(",")))
          .bookingCode(bookingCode)
          .fareBasisCode(fareCode)
          .paxCount(paxCount)
          .changedPaxCount(request.getPaxDetails().getAdultCount() + "," + request.getPaxDetails()
              .getChildCount() + "," +
              request.getPaxDetails().getInfantCount())
          .changedTaxes(changedTax)
          .changedBaseFare(changedBase)
          .metricks(metricks)
          .airline(response.getFlights().get(0).getValidatingCarrierCode())
          .changedCabinClass(response.getFlights().stream()
              .map(flight -> filterCabinClassValue(flight.getCabinClass())).collect(
                  Collectors.joining(",")))
          .changedBookingCode(changedBookingCode)
          .responseTime(request.getResponseTime() / 1000)
          .changedFareBasisCode(changedFareCode).build()
      );
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  private String filterCabinClassValue(String cabin) {
    if (null != cabin) {
      cabin = cabin.toLowerCase()
          .replaceAll("basic economy", "BE")
          .replaceAll("economy", "E")
          .replaceAll("business", "B")
          .replaceAll("premium economy", "P")
          .replaceAll("First", "F");
      cabin = cabin.substring(0, 1).toUpperCase();
    }
    return cabin;
  }

  private double getPaxwiseFare(Fares item, double fare, SupplierRepriceRequest request) {
    if (item.getPaxType().equals("ADT")) {
      return fare * request.getPaxDetails().getAdultCount();
    } else if (request.getPaxDetails().getChildCount() > 0 && item.getPaxType()
        .equals("CHD")) {
      return fare * request.getPaxDetails().getChildCount();
    } else if (request.getPaxDetails().getInfantCount() > 0 && item.getPaxType()
        .equals("INF")) {
      return fare * request.getPaxDetails().getInfantCount();
    }
    return 0.0;
  }

  private FlightRepriceResponse prepareFlightRepriceResponse(RepriceResponse repriceResponse,
      SupplierRepriceRequest request) {
    FlightRepriceResponse.FlightRepriceResponseBuilder response = FlightRepriceResponse.builder();
    boolean isValidPrice = false;
    if (null != repriceResponse && null != repriceResponse.getFlights()) {
      if (repriceResponse.getFlights().size() >= request.getItineraryIds().size()) {
        isValidPrice = repriceResponse.getFlights().stream()
            .filter(itin -> itin.isFareChanged()).collect(Collectors.toList()).isEmpty();
        response.flights(repriceResponse.getFlights());
        if (isValidPrice && !request.isFlightInfoRequired()) {
          response.flights(Collections.emptyList());
        }
      }
    }
    response.validPrice(isValidPrice);
    return response.build();
  }

  private List<FlightsResponse> fetchFlightsByJids(SupplierRepriceRequest searchRequest) {
    List<FlightsResponse> flightResp = null;
    try {
      String key = new String(Base64.decodeBase64(searchRequest.getJIds().get(0)));
      SearchResponseEntity searchResponse = searchResponseRepository.findByResponseKey(
          key.split("-")[0]);
      flightResp = Optional.ofNullable(searchResponse.getFlights()).map(f -> f.stream().filter(flight -> searchRequest.getJIds().contains(flight.getJId())).collect(
          Collectors.toList())).orElse(Collections.emptyList());
      if(flightResp.isEmpty() && !searchResponse.getBrandFlights().isEmpty()) {
        //get flight s from brand map;
        flightResp = Optional.ofNullable(searchResponse.getBrandFlights()).map(f -> f.values().stream().flatMap(List::stream).filter(flight -> searchRequest.getJIds().contains(flight.getJId())).collect(
            Collectors.toList())).orElse(Collections.emptyList());
      }
      searchRequest.setSearchKey(searchResponse.getSearchKey());
    } catch (Exception sme) {
      throw new FlightRepriceException(ErrorCodeConstants.RPE_00010);
    }
    if (flightResp == null || flightResp.isEmpty()) {
      throw new FlightRepriceException(ErrorCodeConstants.RPE_00010);
    }
    searchRequest.setItineraryIds(
        flightResp.stream().map(FlightsResponse::getSupplierItineraryId).collect(Collectors.toList()));
    searchRequest.setJIdSupplierMap(flightResp.stream()
        .collect(Collectors.toMap(FlightsResponse::getSupplierItineraryId, FlightsResponse::getJId)));

    try {
      Map<String, String> faresMap = new HashMap<String, String>();
      for (int i = 0; i < searchRequest.getJIds().size(); i++) {
        if (null != searchRequest.getFareIds() && !searchRequest.getFareIds().isEmpty() &&
            searchRequest.getFareIds().containsKey(searchRequest.getJIds().get(i))) {
          faresMap.put(flightResp.stream()
                  .collect(Collectors.toMap(FlightsResponse::getJId, FlightsResponse::getSupplierItineraryId))
                  .get(searchRequest.getJIds().get(i)),
              searchRequest.getFareIds().get(searchRequest.getJIds().get(i)));
        }
      }
      searchRequest.setFareIds(faresMap);
    } catch (Exception sme) {
      throw new FlightRepriceException(ErrorCodeConstants.RPE_00016);
    }
    return flightResp;
  }

  private SupplierRepriceRequest prepareRepriceRequest(RepriceRequest searchRequest) {
    return SupplierRepriceRequest.builder()
        .supplier(MONDEE)
        .paxDetails(searchRequest.getPaxDetails())
        .jIds(searchRequest.getJIds())
        .fareIds(searchRequest.getBrandedFareIds())
        .flightInfoRequired(searchRequest.isFlightInfoRequired())
        .build();
  }

}
