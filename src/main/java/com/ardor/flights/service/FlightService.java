/**
 *
 */
package com.ardor.flights.service;

import com.ardor.flights.common.ErrorCodeConstants;
import com.ardor.flights.entity.booking.AirPaxBookingInfo;
import com.ardor.flights.entity.booking.AirTransactionDetail;
import com.ardor.flights.entity.booking.AirTransactionHeader;
import com.ardor.flights.exception.FlightCancelException;
import com.ardor.flights.exception.FlightOrderTicketException;
import com.ardor.flights.exception.FlightRepriceException;
import com.ardor.flights.helper.FlightCancelHelper;
import com.ardor.flights.helper.FlightOrderTicketHelper;
import com.ardor.flights.helper.FlightReadPnrHelper;
import com.ardor.flights.model.cancel.FlightCancelRequest;
import com.ardor.flights.model.cancel.FlightCancelResponse;
import com.ardor.flights.model.readpnr.FlightReadPnrRequest;
import com.ardor.flights.model.readpnr.FlightReadPnrResponse;
import com.ardor.flights.model.ticket.FlightOrderTicketRequest;
import com.ardor.flights.model.ticket.FlightOrderTicketResponse;
import com.ardor.flights.repository.postgres.booking.AirPaxBookingInfoRepository;
import com.ardor.flights.repository.postgres.booking.TransactionDetailRepository;
import com.ardor.flights.repository.postgres.booking.TransactionHeaderRepository;
import jakarta.validation.Valid;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightService {

  private final FlightCancelHelper flightCancelHelper;
  private final FlightReadPnrHelper flightReadPnrHelper;
  private final FlightOrderTicketHelper flightOrderTicketHelper;
  private final TransactionDetailRepository transactionDetailRepository;
  private final TransactionHeaderRepository transactionHeaderRepository;
  private final AirPaxBookingInfoRepository airPaxBookingInfoRepository;

  public FlightCancelResponse cancelPnr(@Valid FlightCancelRequest cancelRequest) {
    try {
      Optional<AirTransactionDetail> airTransactionDetail = getSupplierReferenceNumber(cancelRequest.getReferenceNumber(), cancelRequest.getPNR());
      if("CAN".equalsIgnoreCase(airTransactionDetail.get().getTransactionStatus())) {
        return FlightCancelResponse.builder()
            .cancelled(true)
            .message("PNR cancelled successfully")
            .build();
      }
      if(airTransactionDetail.get().getSupplierRef() != null) {
        cancelRequest.setSupplierReference(airTransactionDetail.get().getSupplierRef());
      } else {
        throw new FlightCancelException(ErrorCodeConstants.FCE_0004);
      }
    } catch (Exception e) {
      throw new FlightCancelException(ErrorCodeConstants.FCE_0004);
    }
    FlightCancelResponse resp = flightCancelHelper.cancelPnr(cancelRequest);
    if(resp.isCancelled()) {
      updateFlightStatus(cancelRequest.getReferenceNumber(), "CAN",cancelRequest.getPNR());
    }
    return resp;
  }

  public FlightReadPnrResponse readPnr(@Valid FlightReadPnrRequest readPnrRequest) {
    try {
      Optional<AirTransactionDetail> airTransactionDetail = getSupplierReferenceNumber(readPnrRequest.getReferenceNumber(), readPnrRequest.getPNR());
      if(airTransactionDetail.get().getSupplierRef() != null) {
        readPnrRequest.setSupplierReference(airTransactionDetail.get().getSupplierRef());
      } else {
        throw new FlightRepriceException(ErrorCodeConstants.FRE_0005);
      }
    } catch (Exception e) {
      throw new FlightRepriceException(ErrorCodeConstants.FRE_0005);
    }
    return flightReadPnrHelper.readPnr(readPnrRequest);
  }

  public FlightOrderTicketResponse orderTicket(
      @Valid FlightOrderTicketRequest flightOrderTicketRequest) {
    try {
      Optional<AirTransactionDetail> airTransactionDetail = getSupplierReferenceNumber(flightOrderTicketRequest.getReferenceNumber(), flightOrderTicketRequest.getPNR());
      if(airTransactionDetail.get().getSupplierRef() != null) {
        flightOrderTicketRequest.setSupplierReference(airTransactionDetail.get().getSupplierRef());
      } else {
        throw new FlightOrderTicketException(ErrorCodeConstants.OTE_0005);
      }
    } catch (Exception e) {
      throw new FlightOrderTicketException(ErrorCodeConstants.OTE_0005);
    }
    FlightOrderTicketResponse resp = flightOrderTicketHelper.orderTicket(flightOrderTicketRequest);
    if(resp.isTicketOrdered()) {
      updateFlightStatus(flightOrderTicketRequest.getReferenceNumber(), "TKO",flightOrderTicketRequest.getPNR());
      readPnr(FlightReadPnrRequest.builder().PNR(flightOrderTicketRequest.getPNR()).referenceNumber(flightOrderTicketRequest.getReferenceNumber()).build());
    }
    return resp;
  }

  private Optional<AirTransactionDetail> getSupplierReferenceNumber(String referenceNumber, String pnr) {
    List<AirTransactionDetail> airTransactionDetails = transactionDetailRepository.findByAirTransactionId(referenceNumber);
    Optional<AirTransactionDetail> airTransactionDetail = airTransactionDetails.stream().filter(detail -> pnr.equalsIgnoreCase(detail.getPnr())).findFirst();
    return airTransactionDetail;
  }

  private void updateFlightStatus(String referenceNumber, String status, String pnr) {
    AirTransactionHeader transaction = transactionHeaderRepository.findByAirTransactionId(
        referenceNumber);
    List<AirTransactionDetail> transactionDetail = transactionDetailRepository.findByAirTransactionId(
        referenceNumber);
    for(AirTransactionDetail detail : transactionDetail) {
      if(pnr.equalsIgnoreCase(detail.getPnr())) {
        detail.setTransactionStatus(status);
        detail.setModified(new Date(System.currentTimeMillis()));
      }
    }
    transactionDetailRepository.saveAll(transactionDetail);
    if(!status.equalsIgnoreCase(transaction.getTransactionStatus())) {
      String trStatus = transactionDetail.stream().filter(detail -> status.equalsIgnoreCase(detail.getTransactionStatus())).toList().size() == transactionDetail.size() ? status : "PCD";
      transaction.setTransactionStatus(trStatus);
      transaction.setModified(new Date(System.currentTimeMillis()));
      transactionHeaderRepository.save(transaction);
    }
    List<AirPaxBookingInfo> airPaxBookingInfo = airPaxBookingInfoRepository.findByAirTransactionId(referenceNumber);
    for(AirPaxBookingInfo bookingInfo : airPaxBookingInfo) {
        if(pnr.equalsIgnoreCase(bookingInfo.getPnr())){
          bookingInfo.setTransStatus(status);
          bookingInfo.setModified(new Date(System.currentTimeMillis()));
        }
    }
    airPaxBookingInfoRepository.saveAll(airPaxBookingInfo);
  }
}
