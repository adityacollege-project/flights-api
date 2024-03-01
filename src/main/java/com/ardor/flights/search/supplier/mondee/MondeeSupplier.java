package com.ardor.flights.search.supplier.mondee;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.ardor.flights.common.Constants;
import com.ardor.flights.common.ErrorCodeConstants;
import com.ardor.flights.entity.reprice.RepriceRequestResponseEntity;
import com.ardor.flights.exception.FlightBookingException;
import com.ardor.flights.exception.FlightCancelException;
import com.ardor.flights.exception.FlightOrderTicketException;
import com.ardor.flights.exception.FlightReadPnrException;
import com.ardor.flights.exception.FlightRepriceException;
import com.ardor.flights.exception.FlightSearchException;
import com.ardor.flights.exception.SeatMapException;
import com.ardor.flights.mapper.FlightBookingMapper;
import com.ardor.flights.mapper.RepriceResponseMapper;
import com.ardor.flights.mapper.SearchResponseMapper;
import com.ardor.flights.model.ancillary.SeatMapRequest;
import com.ardor.flights.model.ancillary.SeatResponse;
import com.ardor.flights.model.booking.FlightBookingRequest;
import com.ardor.flights.model.booking.FlightBookingResponse;
import com.ardor.flights.model.cancel.FlightCancelRequest;
import com.ardor.flights.model.cancel.FlightCancelResponse;
import com.ardor.flights.model.readpnr.FlightReadPnrRequest;
import com.ardor.flights.model.readpnr.FlightReadPnrResponse;
import com.ardor.flights.model.reprice.RepriceResponse;
import com.ardor.flights.model.reprice.SupplierRepriceRequest;
import com.ardor.flights.model.search.FlightSearchRequest;
import com.ardor.flights.model.search.FlightSearchRequest.OriginDestination;
import com.ardor.flights.model.search.FlightSearchRequest.PaxDetails;
import com.ardor.flights.model.search.FlightSearchResponse;
import com.ardor.flights.model.ticket.FlightOrderTicketRequest;
import com.ardor.flights.model.ticket.FlightOrderTicketResponse;
import com.ardor.flights.search.supplier.mondee.helper.MondeeHelper;
import com.ardor.flights.search.supplier.mondee.helper.ReadPnrResponseMapper;
import com.ardor.flights.supplier.mondee.model.booking.MondeeBookingRequest;
import com.ardor.flights.supplier.mondee.model.booking.MondeeBookingResponse;
import com.ardor.flights.supplier.mondee.model.cancel.MondeeCancelResponse;
import com.ardor.flights.supplier.mondee.model.readpnr.MondeeReadPnrResponse;
import com.ardor.flights.supplier.mondee.model.reprice.MondeeRepriceResponse;
import com.ardor.flights.supplier.mondee.model.search.MondeeFlightItinerary;
import com.ardor.flights.supplier.mondee.model.search.MondeeSearchResponse;
import com.ardor.flights.supplier.mondee.model.ticket.MondeeOrderTickerResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MondeeSupplier {

  @Value("${flights.mondee.search.endPoint}")
  private String searchEndPoint;
  @Value("${flights.mondee.accessToken}")
  private String accessToken;
  @Value("${flights.mondee.reprice.endPoint}")
  private String repriceEndPoint;
  @Value("${flights.mondee.seatMap.endPoint}")
  private String seatMapEndPoint;
  @Value("${flights.mondee.booking.endPoint}")
  private String bookingEndPoint;
  @Value("${flights.mondee.cancel.endPoint}")
  private String cancelEndPoint;
  @Value("${flights.mondee.readPnr.endPoint}")
  private String readPnrEndPoint;
  @Value("${flights.mondee.orderticket.endPoint}")
  private String orderticketEndPoint;

  @Autowired
  private CircuitBreakerRegistry circuitBreakerRegistry;
  private final MondeeHelper mondeeHelper;
  private final ObjectMapper mapper = new ObjectMapper();
  private final RestTemplate restTemplate;
  private final SearchResponseMapper searchResponseMapper;
  private final RepriceResponseMapper repriceResponseMapper;
  private final RestTemplate repriceRestTemplate;
  private final FlightBookingMapper bookingMapper;
  private final Gson gson;
  private final RestTemplate cancelRestTemplate;
  private final ReadPnrResponseMapper readPnrResponseMapper;

  public FlightSearchResponse flightSearch(FlightSearchRequest searchRequest) {
    ResponseEntity<String> responseEntity = callMondeeSearchAPI(searchRequest);
    log.debug("search response from Mondee Supplier {}", responseEntity);
    if(responseEntity.getBody() != null) {
      String responseBody = responseEntity.getBody();
      if(responseBody.contains("Invalid Location Code")) {
        log.error("Invalid Location Code provided");
        throw new FlightSearchException(ErrorCodeConstants.FSE_0025);
      }
    }
    return processResponse(responseEntity, searchRequest).get();
  }

  //Method to prepare the requst to call Mondee API
  private ResponseEntity<String> callMondeeSearchAPI(FlightSearchRequest searchRequest) {
    String request = prepareSearchRequest(searchRequest);
    log.info("search request for Mondee Supplier {}", request);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("SearchAccessToken", accessToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(request, headers);
    CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(
        Constants.SEARCH_CB_INSTANCE_NAME, Constants.SEARCH_CB_INSTANCE_NAME);
    return callMondeeAPI(requestEntity, circuitBreaker);
  }

  //Method to call Mondee API
  private ResponseEntity<String> callMondeeAPI(HttpEntity<String> requestEntity,
      CircuitBreaker circuitBreaker) {
    return CircuitBreaker.decorateSupplier(circuitBreaker, () ->
        restTemplate.exchange(searchEndPoint, HttpMethod.POST,
            requestEntity, String.class)
    ).get();
  }

  //Method to process and insert the requests in cache
  private Optional<FlightSearchResponse> processResponse(ResponseEntity<String> responseEntity,
      FlightSearchRequest searchRequest) {
    FlightSearchResponse flightSearchResponse = null;
    if (responseEntity.getStatusCode() == HttpStatus.OK) {
      try {
        String responseKey = mondeeHelper.generateUniqueKey(16);
        MondeeSearchResponse mondeeSearchResponse = null;
        if (searchRequest.isBrandedfares()) {
          Map<String, List<MondeeFlightItinerary>> searchResponse = mapper.readValue(
              responseEntity.getBody(),
              new TypeReference<Map<String, List<MondeeFlightItinerary>>>() {
              });
          mondeeSearchResponse = MondeeSearchResponse.builder().brandedFlights(searchResponse)
              .build();
          mondeeSearchResponse.getBrandedFlights().values().stream()
              .forEach(obj -> obj.stream().forEach(itin -> itin.setJId(
                  Base64.encodeBase64String(
                      (responseKey + "-" + mondeeHelper.generateUniqueKey(16)).getBytes()))));
          log.info("search response count from Mondee Supplier {}",
              mondeeSearchResponse.getBrandedFlights().size());
        } else {
          mondeeSearchResponse = mapper.readValue(responseEntity.getBody(),
              MondeeSearchResponse.class);
          mondeeSearchResponse.getFlights().stream().forEach(itin -> itin.setJId(
              Base64.encodeBase64String(
                  (responseKey + "-" + mondeeHelper.generateUniqueKey(16)).getBytes())));
          log.info("search response count from Mondee Supplier {}",
              mondeeSearchResponse.getFlights().size());
        }
        if (CollectionUtils.isEmpty(mondeeSearchResponse.getFlights()) && CollectionUtils.isEmpty(
            mondeeSearchResponse.getBrandedFlights())) {
          log.info("Mondee Supplier failed to give flights.");
          throw new FlightSearchException("FSE0022:No Flights Available for the Search");
        }
        flightSearchResponse = searchResponseMapper.mapResponsefromMondeeToArdor(
            mondeeSearchResponse);
        flightSearchResponse.setResponseKey(responseKey);
      } catch (JsonProcessingException jpe) {
        throw new FlightSearchException("FSE0001:Failed to parse the search response from API",
            jpe);
      }
    }
    return Optional.ofNullable(flightSearchResponse);
  }

  private String prepareSearchRequest(FlightSearchRequest searchRequest) {
    JsonObject request = new JsonObject();

    JsonObject currencyInfo = new JsonObject();
    currencyInfo.addProperty("CurrencyCode", "USD");
    request.add("CurrencyInfo", currencyInfo);

    JsonObject paxDetails = preparePaxDetails(searchRequest.getPaxDetails());
    request.add("PaxDetails", paxDetails);

    JsonArray odList = searchRequest.getOriginDestinations().stream()
        .map(this::prepareOriginDestination)
        .collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
    request.add("OriginDestination", odList);

    request.addProperty("Incremental", searchRequest.isIncremental());
    request.addProperty("BrandedFares", searchRequest.isBrandedfares());
    request.addProperty("DTK", searchRequest.isBrandedfares());
    return request.toString();
  }

  private JsonObject preparePaxDetails(PaxDetails paxDetail) {
    JsonObject paxDetails = new JsonObject();
    JsonObject noOfAdults = new JsonObject();
    noOfAdults.addProperty("count", paxDetail.getAdultCount());
    paxDetails.add("NoOfAdults", noOfAdults);

    JsonObject noOfChildern = new JsonObject();
    noOfChildern.addProperty("count", paxDetail.getChildCount());
    paxDetails.add("NoOfChildren", noOfChildern);

    JsonObject noOfInfants = new JsonObject();
    noOfInfants.addProperty("count", paxDetail.getInfantCount());
    paxDetails.add("NoOfInfants", noOfInfants);

    return paxDetails;
  }

  private JsonObject prepareOriginDestination(OriginDestination originDestination) {
    JsonObject odObj = new JsonObject();
    odObj.addProperty("DepartureLocationCode", originDestination.getFrom());
    odObj.addProperty("ArrivalLocationCode", originDestination.getTo());
    String[] onwardDateArr = originDestination.getOnwardDate().split("/");
    String onwardDate = onwardDateArr[2] + "/" + onwardDateArr[1] + "/" + onwardDateArr[0];
    odObj.addProperty("DepartureTime", onwardDate);
    odObj.addProperty("CabinClass", originDestination.getCabinClass());
    return odObj;
  }

  public RepriceResponse repriceFlight(SupplierRepriceRequest request,
      RepriceRequestResponseEntity.RepriceRequestResponseEntityBuilder entity) {
    RepriceResponse response = null;
    String requestBody = mondeeHelper.prepareRepriceRequest(request);
    entity.supplierRequest(requestBody);
    ResponseEntity<String> responseEntity = callMondeeRepriceAPI(requestBody);
    log.info("reprice response from Mondee Supplier {}", responseEntity);
    entity.supplierResponse(responseEntity.getBody());
    response = processRepriceResponse(responseEntity, request).get();
    return response;
  }

  private ResponseEntity<String> callMondeeRepriceAPI(String requestBody) {
    log.info("reprice request for Mondee Supplier {}", requestBody);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("AccessToken", accessToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
    CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(
        Constants.SEARCH_CB_INSTANCE_NAME, Constants.SEARCH_CB_INSTANCE_NAME);
    return callMondeeRepriceAPI(requestEntity, circuitBreaker);
  }

  private ResponseEntity<String> callMondeeRepriceAPI(HttpEntity<String> requestEntity,
      CircuitBreaker circuitBreaker) {
    return CircuitBreaker.decorateSupplier(circuitBreaker, () ->
        repriceRestTemplate.exchange(repriceEndPoint, HttpMethod.POST,
            requestEntity, String.class)
    ).get();
  }

  private Optional<RepriceResponse> processRepriceResponse(ResponseEntity<String> responseEntity,
      SupplierRepriceRequest request) {
    RepriceResponse repriceResponse = null;
    if (responseEntity.getStatusCode() == HttpStatus.OK) {
      MondeeRepriceResponse mondeeRepriceResponse = null;
      try {
        mondeeRepriceResponse = mapper.readValue(responseEntity.getBody(),
            MondeeRepriceResponse.class);
        log.info("reprice response count from Mondee Supplier {}",
            mondeeRepriceResponse.getRepriceResponses().size());
      } catch (JsonProcessingException jpe) {
        log.error("Unable to parse flight reprice response ", jpe);
        throw new FlightRepriceException(ErrorCodeConstants.RPE_00012);
      }
      mondeeRepriceResponse = mondeeHelper.checkAndValidateResponse(mondeeRepriceResponse);
      if (null == mondeeRepriceResponse) {
        throw new FlightRepriceException(ErrorCodeConstants.RPE_00013);
      }
      try {
        repriceResponse = repriceResponseMapper.mapResponsefromMondeeToArdor(
            mondeeRepriceResponse);
        mondeeHelper.setJid(repriceResponse, mondeeRepriceResponse, request);
      } catch (Exception jpe) {
        log.error("Unable to parse flight reprice response ", jpe);
        throw new FlightRepriceException(ErrorCodeConstants.RPE_00014);
      }
    } else {
      throw new FlightRepriceException(ErrorCodeConstants.RPE_00015);
    }
    return Optional.ofNullable(repriceResponse);
  }

  public SeatResponse seatMap(SeatMapRequest request) {
    SeatResponse response = null;
    String requestBody = mondeeHelper.prepareSeatMapRequest(request);
    ResponseEntity<String> responseEntity = callMondeeSeatMapAPI(requestBody);
    log.info("seatmap response from Mondee Supplier {}", responseEntity);
    return processSeatMapResponse(responseEntity, request).get();
  }

  private ResponseEntity<String> callMondeeSeatMapAPI(String requestBody) {
    log.info("seat map request for Mondee Supplier {}", requestBody);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("AccessToken", accessToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
    CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(
        Constants.SEARCH_CB_INSTANCE_NAME, Constants.SEARCH_CB_INSTANCE_NAME);
    return callMondeeSeatMapAPI(requestEntity, circuitBreaker);
  }

  private ResponseEntity<String> callMondeeSeatMapAPI(HttpEntity<String> requestEntity,
      CircuitBreaker circuitBreaker) {
    return CircuitBreaker.decorateSupplier(circuitBreaker, () ->
        repriceRestTemplate.exchange(seatMapEndPoint, HttpMethod.POST,
            requestEntity, String.class)
    ).get();
  }

  private Optional<SeatResponse> processSeatMapResponse(ResponseEntity<String> responseEntity,
      SeatMapRequest request) {
    SeatResponse seatResponse = null;
    if (responseEntity.getStatusCode() == HttpStatus.OK) {
      try {
        seatResponse = mapper.readValue(responseEntity.getBody(),
            SeatResponse.class);
      } catch (JsonProcessingException jpe) {
        log.error("Unable to parse seat map response ", jpe);
        throw new SeatMapException(ErrorCodeConstants.SME_0003);
      }
      if (null == seatResponse.getSeatMap() || seatResponse.getSeatMap().isEmpty() ||
          seatResponse.getSeatMap().stream()
              .filter(seatMap -> null != seatMap.getCabins() && !seatMap.getCabins().isEmpty())
              .collect(
                  Collectors.toList()).isEmpty()) {
        throw new SeatMapException(ErrorCodeConstants.SME_0003);
      }
    } else {
      throw new SeatMapException(ErrorCodeConstants.SME_0004);
    }
    return Optional.ofNullable(seatResponse);
  }

  public FlightBookingResponse bookFlight(FlightBookingRequest bookingRequest) {
    MondeeBookingRequest mondeeBookingRequest = mondeeHelper.prepareMondeeBookingRequest(
        bookingRequest);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("AccessToken", accessToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(this.gson.toJson(mondeeBookingRequest),
        headers);
    log.info("Booking request to Mondee Supplier {}", requestEntity);
    // Calling the Mondee API for Booking
    ResponseEntity<String> responseEntity = repriceRestTemplate.exchange(bookingEndPoint,
        HttpMethod.POST, requestEntity, String.class);
    log.info("Booking response from Mondee Supplier {}", responseEntity);
    if (responseEntity.getStatusCode() == HttpStatus.OK) {
      try {
        MondeeBookingResponse mondeeBookingResponse = mapper.readValue(responseEntity.getBody(),
            MondeeBookingResponse.class);
        return bookingMapper.mapResponseMondeeToArdor(mondeeBookingResponse);
      } catch (JsonProcessingException jpe) {
        throw new FlightBookingException(ErrorCodeConstants.FBE_0004, jpe);
      }
    } else {
      throw new FlightBookingException(ErrorCodeConstants.FBE_0005);
    }
  }

public FlightCancelResponse cancelPnr(@Valid FlightCancelRequest cancelRequest) {
	String requestBody = mondeeHelper.prepareCancelPnrRequest(cancelRequest);
	log.info("flight cancel request for Mondee Supplier {}", requestBody);
	HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("AccessToken", accessToken);
    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
  ResponseEntity<String> responseEntity = null;
  String errorMsg = null;
  try {
      responseEntity = repriceRestTemplate.exchange(cancelEndPoint, HttpMethod.POST,
          requestEntity, String.class);
    } catch (Exception e) {
      errorMsg = getErrorMessage(e.getMessage());
      if(null == errorMsg) {
        throw new FlightCancelException(ErrorCodeConstants.FCE_0003, e);
      }
    }
    if(null != errorMsg) {
      return FlightCancelResponse.builder()
          .cancelled(errorMsg.contains("Itinerary already has been cancelled"))
          .message(errorMsg.contains("Itinerary already has been cancelled") ? "Itinerary already has been cancelled": "Unable to cancel PNR")
          .build();
    }
    log.info("Cancel PNR response from Mondee Supplier {}", responseEntity);
    if (responseEntity.getStatusCode() == HttpStatus.OK) {
	    try {
	      MondeeCancelResponse mondeeBookingResponse = mapper.readValue(responseEntity.getBody(),
	    		  MondeeCancelResponse.class);
	      return FlightCancelResponse.builder()
            .cancelled(!mondeeBookingResponse.isError())
            .message(mondeeBookingResponse.isError() ? mondeeBookingResponse.getErrorMessage() : mondeeBookingResponse.getMessage())
            .build();
	    } catch (JsonProcessingException jpe) {
	      throw new FlightCancelException(ErrorCodeConstants.FCE_0002, jpe);
	    }
	  } else {
	    throw new FlightCancelException(ErrorCodeConstants.FCE_0003);
	  }
}

private String getErrorMessage(String errorMsg) {
    try {
      if(errorMsg.contains("errorMsg")) {
          return errorMsg;
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return null;
}
	
	public FlightReadPnrResponse readPnr(@Valid FlightReadPnrRequest readPnrRequest) {
		String requestBody = mondeeHelper.prepareReadPnrRequest(readPnrRequest);
		log.info("flight read pnr request for Mondee Supplier {}", requestBody);
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.add("AccessToken", accessToken);
	    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
	  ResponseEntity<String> responseEntity = null;
	  try {
	      responseEntity = repriceRestTemplate.exchange(readPnrEndPoint, HttpMethod.POST,
	          requestEntity, String.class);
	    } catch (Exception e) {
	      throw new FlightReadPnrException(ErrorCodeConstants.FRE_0003, e);
	    }
	    log.info("Read PNR response from Mondee Supplier {}", responseEntity);
	    if (responseEntity.getStatusCode() == HttpStatus.OK) {
		    try {
		      MondeeReadPnrResponse mondeeReadPnrResponse = mapper.readValue(responseEntity.getBody(),
		    		  MondeeReadPnrResponse.class);
          if(mondeeReadPnrResponse.isError()) {
            throw new FlightReadPnrException(ErrorCodeConstants.FRE_0004 + mondeeReadPnrResponse.getErrorMsg());
          }
		      return readPnrResponseMapper.mapResponseMondeeToArdor(mondeeReadPnrResponse);
		    } catch (JsonProcessingException jpe) {
		      throw new FlightReadPnrException(ErrorCodeConstants.FRE_0002, jpe);
		    }
		  } else {
		    throw new FlightReadPnrException(ErrorCodeConstants.FRE_0003);
		  }
	}

	public FlightOrderTicketResponse orderTicket(@Valid FlightOrderTicketRequest flightOrderTicketRequest) {
		String requestBody = mondeeHelper.prepareOrderTicketRequest(flightOrderTicketRequest);
		log.info("flight order ticket request for Mondee Supplier {}", requestBody);
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.add("AccessToken", accessToken);
	    HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
	  ResponseEntity<String> responseEntity = null;
	  try {
	      responseEntity = repriceRestTemplate.exchange(orderticketEndPoint, HttpMethod.POST,
	          requestEntity, String.class);
	    } catch (Exception e) {
	      throw new FlightOrderTicketException(ErrorCodeConstants.OTE_0003, e);
	    }
	    log.info("Read PNR response from Mondee Supplier {}", responseEntity);
	    if (responseEntity.getStatusCode() == HttpStatus.OK) {
		    try {
		      MondeeOrderTickerResponse mondeeOrderTickerResponse = mapper.readValue(responseEntity.getBody(),
		    		  MondeeOrderTickerResponse.class);
          if(mondeeOrderTickerResponse.isError()) {
            throw new FlightOrderTicketException(ErrorCodeConstants.OTE_0004 + mondeeOrderTickerResponse.getErrorMessage());
          }
          return FlightOrderTicketResponse.builder()
              .ticketOrdered(!mondeeOrderTickerResponse.isError())
              .message(mondeeOrderTickerResponse.isError() ? mondeeOrderTickerResponse.getErrorMessage() : mondeeOrderTickerResponse.getMessage())
              .build();
		    } catch (JsonProcessingException jpe) {
		      throw new FlightOrderTicketException(ErrorCodeConstants.OTE_0002, jpe);
		    }
		  } else {
		    throw new FlightOrderTicketException(ErrorCodeConstants.OTE_0003);
		  }
	}
}
