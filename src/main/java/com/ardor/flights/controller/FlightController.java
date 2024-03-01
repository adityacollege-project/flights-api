package com.ardor.flights.controller;

import com.ardor.flights.ancillary.service.SeatMapService;
import com.ardor.flights.booking.service.BookingService;
import com.ardor.flights.exception.InvalidRequestException;
import com.ardor.flights.model.ancillary.SeatMapRequest;
import com.ardor.flights.model.ancillary.SeatResponse;
import com.ardor.flights.model.booking.FlightBookingRequest;
import com.ardor.flights.model.booking.FlightBookingResponse;
import com.ardor.flights.model.cancel.FlightCancelRequest;
import com.ardor.flights.model.cancel.FlightCancelResponse;
import com.ardor.flights.model.readpnr.FlightReadPnrRequest;
import com.ardor.flights.model.readpnr.FlightReadPnrResponse;
import com.ardor.flights.model.reprice.FlightRepriceResponse;
import com.ardor.flights.model.reprice.RepriceRequest;
import com.ardor.flights.model.reprice.RepriceResponse;
import com.ardor.flights.model.search.FlightSearchRequest;
import com.ardor.flights.model.search.FlightSearchResponse;
import com.ardor.flights.model.ticket.FlightOrderTicketRequest;
import com.ardor.flights.model.ticket.FlightOrderTicketResponse;
import com.ardor.flights.reprice.service.RepriceService;
import com.ardor.flights.search.service.FlightSearchService;
import com.ardor.flights.service.FlightService;
import com.ardor.flights.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Flight", description = "Explore Multiple endpoints like Search,Pricecheck,Seatmap,Bookingservice")
public class FlightController {

  private final FlightSearchService flightSearchService;
  private final RepriceService repriceService;
  private final SeatMapService seatMapService;
  private final BookingService bookingService;
  private final FlightService flightService;
  private final UserService userService;

  @Operation(summary = "Search for flights", description =
      "Search for flights based on \"from\" and \"to\" locations with journey dates and type of cabin class and with pax details.\n"
          +
          "for body -  Search request should be created as a JSON as mentioned below.")
  @ApiResponses(value =
      {
          @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightSearchResponse.class))),
          @ApiResponse(responseCode = "400", ref = "BAD_REQUEST"),
          @ApiResponse(responseCode = "500", ref = "INTERNAL_SERVER_ERROR"),
          @ApiResponse(responseCode = "502", ref = "BAD_GATE_WAY"),
          @ApiResponse(responseCode = "503", ref = "SERVICE_UNAVAILABLE"),
          @ApiResponse(responseCode = "504", ref = "GATEWAY_TIMEOUT"),
      })
  @PostMapping("/search")
  public ResponseEntity<FlightSearchResponse> flightSearch(
      @Valid @RequestBody FlightSearchRequest searchRequest) {
    log.info("flight search request {}", searchRequest);
    FlightSearchResponse response = flightSearchService.flightSearch(searchRequest);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @Operation(summary = "Check Price", description = "Check the price of a response by providing necessary fields such as itinerary and passenger details. Returns repriced details.")
  @ApiResponses(value =
      {
          @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RepriceResponse.class))),
          @ApiResponse(responseCode = "400", ref = "BAD_REQUEST"),
          @ApiResponse(responseCode = "500", ref = "INTERNAL_SERVER_ERROR"),
          @ApiResponse(responseCode = "502", ref = "BAD_GATE_WAY"),
          @ApiResponse(responseCode = "503", ref = "SERVICE_UNAVAILABLE"),
          @ApiResponse(responseCode = "504", ref = "GATEWAY_TIMEOUT"),
      })
  @PostMapping("/priceCheck")
  public ResponseEntity<FlightRepriceResponse> priceCheck(
      @Valid @RequestBody RepriceRequest request) {
    log.info("flight reprice request {}", request);
    FlightRepriceResponse response = repriceService.reprice(request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @Operation(summary = "Fetch Seat Map",
      description = "Retrieve seat mapping for a given flight ID. Returns the seat mapping in JSON format.")
  @ApiResponses(value =
      {
          @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SeatResponse.class))),
          @ApiResponse(responseCode = "400", ref = "BAD_REQUEST"),
          @ApiResponse(responseCode = "500", ref = "INTERNAL_SERVER_ERROR"),
          @ApiResponse(responseCode = "502", ref = "BAD_GATE_WAY"),
          @ApiResponse(responseCode = "503", ref = "SERVICE_UNAVAILABLE"),
          @ApiResponse(responseCode = "504", ref = "GATEWAY_TIMEOUT"),
      })
  @PostMapping("/seatMap")
  public ResponseEntity<SeatResponse> seatMap(@Valid @RequestBody SeatMapRequest request) {
    log.info("flight reprice request {}", request);
    SeatResponse response = seatMapService.seatMap(request);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @Operation(summary = "Make Booking", description = "Confirms a booking with multiple input fields including pax details, passport details, payment, and address.")
  @ApiResponses(value =
      {
          @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightBookingResponse.class))),
          @ApiResponse(responseCode = "400", ref = "BAD_REQUEST"),
          @ApiResponse(responseCode = "500", ref = "INTERNAL_SERVER_ERROR"),
          @ApiResponse(responseCode = "502", ref = "BAD_GATE_WAY"),
          @ApiResponse(responseCode = "503", ref = "SERVICE_UNAVAILABLE"),
          @ApiResponse(responseCode = "504", ref = "GATEWAY_TIMEOUT"),
      })
  @PostMapping("/book")
  public ResponseEntity<FlightBookingResponse> flightBook(
      @Valid @RequestBody FlightBookingRequest bookingRequest,HttpServletRequest httpServletRequest) throws InvalidRequestException {
    log.info("Flight booking request {}", bookingRequest);
    FlightBookingResponse bookingResponse = bookingService.bookFlight(bookingRequest,httpServletRequest);
    return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
  }

  @Operation(summary = "Cancel PNR", description = "Cancel a booking with input field pnr, reason for cancellation")
  @ApiResponses(value =
      {
          @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightCancelResponse.class))),
          @ApiResponse(responseCode = "400", ref = "BAD_REQUEST"),
          @ApiResponse(responseCode = "500", ref = "INTERNAL_SERVER_ERROR"),
          @ApiResponse(responseCode = "502", ref = "BAD_GATE_WAY"),
          @ApiResponse(responseCode = "503", ref = "SERVICE_UNAVAILABLE"),
          @ApiResponse(responseCode = "504", ref = "GATEWAY_TIMEOUT"),
      })
  @PostMapping("/cancel")
  public ResponseEntity<FlightCancelResponse> cancel(@Valid @RequestBody FlightCancelRequest cancelRequest) {
    log.info("Flight cancel request {}", cancelRequest);
    FlightCancelResponse cancelResponse = flightService.cancelPnr(cancelRequest);
    return new ResponseEntity<>(cancelResponse, HttpStatus.OK);
  }

  @Operation(summary = "Read PNR", description = "Read pnr with input field pnr")
  @ApiResponses(value =
      {
          @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightReadPnrResponse.class))),
          @ApiResponse(responseCode = "400", ref = "BAD_REQUEST"),
          @ApiResponse(responseCode = "500", ref = "INTERNAL_SERVER_ERROR"),
          @ApiResponse(responseCode = "502", ref = "BAD_GATE_WAY"),
          @ApiResponse(responseCode = "503", ref = "SERVICE_UNAVAILABLE"),
          @ApiResponse(responseCode = "504", ref = "GATEWAY_TIMEOUT"),
      })
  @PostMapping("/read")
  public ResponseEntity<FlightReadPnrResponse> read(@Valid @RequestBody FlightReadPnrRequest readPnrRequest) {
    log.info("Flight read pnr request {}", readPnrRequest);
    FlightReadPnrResponse readPnrResponse = flightService.readPnr(readPnrRequest);
    return new ResponseEntity<>(readPnrResponse, HttpStatus.OK);
  }

  @Operation(summary = "Order Ticket", description = "Order the Ticket with pnr and reference")
  @ApiResponses(value =
      {
          @ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightOrderTicketResponse.class))),
          @ApiResponse(responseCode = "400", ref = "BAD_REQUEST"),
          @ApiResponse(responseCode = "500", ref = "INTERNAL_SERVER_ERROR"),
          @ApiResponse(responseCode = "502", ref = "BAD_GATE_WAY"),
          @ApiResponse(responseCode = "503", ref = "SERVICE_UNAVAILABLE"),
          @ApiResponse(responseCode = "504", ref = "GATEWAY_TIMEOUT"),
      })
  @PostMapping("/ticket")
  public ResponseEntity<FlightOrderTicketResponse> orderTicket(@Valid @RequestBody FlightOrderTicketRequest flightOrderTicketRequest) {
    log.info("Flight order ticket request {}", flightOrderTicketRequest);
    FlightOrderTicketResponse flightOrderTicketResponse = flightService.orderTicket(flightOrderTicketRequest);
    return new ResponseEntity<>(flightOrderTicketResponse, HttpStatus.OK);
  }

}
