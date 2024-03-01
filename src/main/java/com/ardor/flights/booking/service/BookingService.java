package com.ardor.flights.booking.service;

/**
 * Copyright (c) 2024, Ardor Technologies All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of Ardor Technologies. You shall
 * not disclose such Confidential Information and shall use it only in accordance with the terms of
 * the license agreement you entered into with Ardor Technologies.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import com.ardor.flights.common.ErrorCodeConstants;
import com.ardor.flights.entity.booking.AirFopCollectedInfo;
import com.ardor.flights.entity.booking.AirInfo;
import com.ardor.flights.entity.booking.AirPaxBookingInfo;
import com.ardor.flights.entity.booking.AirPaxInfo;
import com.ardor.flights.entity.booking.AirTransactionDetail;
import com.ardor.flights.entity.booking.AirTransactionHeader;
import com.ardor.flights.entity.booking.LinkAirInfoPaxBooking;
import com.ardor.flights.entity.reprice.RepriceRequestResponseEntity;
import com.ardor.flights.entity.search.SearchRequestEntity;
import com.ardor.flights.exception.FlightBookingException;
import com.ardor.flights.exception.InvalidRequestException;
import com.ardor.flights.exception.UserNotFoundExcpetion;
import com.ardor.flights.model.booking.FlightBookingRequest;
import com.ardor.flights.model.booking.FlightBookingResponse;
import com.ardor.flights.model.booking.FlightBookingResponse.BookingResponse;
import com.ardor.flights.model.reprice.RepriceFlightItinerary;
import com.ardor.flights.model.search.FlightSearchRequest.OriginDestination;
import com.ardor.flights.model.user.UserInfo;
import com.ardor.flights.repository.mongo.reprice.RepriceRequestResponseRepository;
import com.ardor.flights.repository.mongo.search.SearchRequestRepository;
import com.ardor.flights.repository.postgres.booking.AirFopCollectedInfoRepository;
import com.ardor.flights.repository.postgres.booking.AirInfoRepository;
import com.ardor.flights.repository.postgres.booking.AirPaxBookingInfoRepository;
import com.ardor.flights.repository.postgres.booking.AirPaxInfoRepository;
import com.ardor.flights.repository.postgres.booking.LinkAirInfoPaxBookingRepository;
import com.ardor.flights.repository.postgres.booking.TransactionDetailRepository;
import com.ardor.flights.repository.postgres.booking.TransactionHeaderRepository;
import com.ardor.flights.search.supplier.mondee.MondeeSupplier;
import com.ardor.flights.search.supplier.mondee.helper.MondeeHelper;
import com.ardor.flights.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

  private final MondeeSupplier mondeeSupplier;
  private final MondeeHelper mondeeHelper;
  private final RepriceRequestResponseRepository repriceRequestResponseRepository;
  private final SearchRequestRepository searchRequestRepository;
  private final AirPaxInfoRepository paxInfoRepository;
  private final AirInfoRepository airInfoRepository;
  private final AirFopCollectedInfoRepository airFopCollectedInfoRepository;
  private final AirPaxBookingInfoRepository airPaxBookingInfoRepository;
  private final TransactionHeaderRepository transactionHeaderRepository;
  private final TransactionDetailRepository transactionDetailRepository;
  private final LinkAirInfoPaxBookingRepository linkAirInfoPaxBookingRepository;
  private final UserService userService;

  public FlightBookingResponse bookFlight(FlightBookingRequest bookingRequest, HttpServletRequest httpServletRequest) throws InvalidRequestException {
    UserInfo userInfo = userService.getUserInfo(httpServletRequest);
    if (userInfo != null) {
      RepriceRequestResponseEntity repriceResponse = this.repriceRequestResponseRepository.findLatestByRawRequestJIds(
          bookingRequest.getJIds());
      if (repriceResponse == null || !repriceResponse.isValidPrice()) {
        throw new FlightBookingException(ErrorCodeConstants.FBE_0076);
      }
      if (isValidRequest(bookingRequest)) {
        log.info("Valid request");
      }
      FlightBookingResponse bookingResponse = mondeeSupplier.bookFlight(bookingRequest);
      String id = saveBookingDetails(bookingRequest, bookingResponse, repriceResponse,userInfo);
      bookingResponse.getBookingResponse().stream().forEach(ft -> ft.setReferenceNumber(id));
      return bookingResponse;
    } else {
      throw new UserNotFoundExcpetion(ErrorCodeConstants.UIE_0001);
    }
  }

  private boolean isValidRequest(FlightBookingRequest bookingRequest) throws InvalidRequestException {
    if(Objects.equals(bookingRequest.getPaymentDetails().getPaymentType(), "CC")) {
      if(bookingRequest.getPaymentDetails().getCreditCardDetails() == null) {
        throw new InvalidRequestException(ErrorCodeConstants.FBE_0061);
      }
    }
    return true;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public String saveBookingDetails(FlightBookingRequest bookingRequest,
      FlightBookingResponse flightBookingResponse, RepriceRequestResponseEntity repriceResponse,
      UserInfo userInfo) {
    SearchRequestEntity searchRequest = this.searchRequestRepository.findLatestBySearchKey(
        repriceResponse.getSearchKey());

    // Transaction Header persistence
    AirTransactionHeader airTransactionHeader = mondeeHelper.prepareBookingTransactionHeader(
        bookingRequest, repriceResponse);
    airTransactionHeader.setUserCode(userInfo.getUserCode());
    airTransactionHeader.setModifiedBy(userInfo.getUserCode());
    if(flightBookingResponse.getBookingResponse().stream()
        .anyMatch(obj -> "Failed".equalsIgnoreCase(obj.getStatus()))) {
      airTransactionHeader.setTransactionStatus("FLD");
    }
    transactionHeaderRepository.save(airTransactionHeader);

    String id = airTransactionHeader.getAirTransactionId();

    for (int i = 0; i < bookingRequest.getJIds().size(); i++) {
      int j = i;
      RepriceFlightItinerary flight = repriceResponse.getRawResponse().getFlights().stream()
          .filter(f -> f.getJId().equals(bookingRequest.getJIds().get(j))).findFirst()
          .orElseGet(null);
      BookingResponse bookingResponse = null;
      if(flightBookingResponse.getBookingResponse().size() > j) {
        bookingResponse = flightBookingResponse.getBookingResponse().get(j);
      } else if(j > 0 && flightBookingResponse.getBookingResponse().get(0).isRoundTripPnr()) {
        bookingResponse = flightBookingResponse.getBookingResponse().get(0);
      }
      OriginDestination originDestination = searchRequest.getOriginDestinations().get(j);

      // Transaction Detail Persistence
      AirTransactionDetail airTransactionDetail = mondeeHelper.prepareBookingTransactionDetail(
          bookingRequest, bookingResponse, flight, searchRequest, originDestination);
      airTransactionDetail.setAirTransactionId(airTransactionHeader.getAirTransactionId());
      airTransactionDetail.setUserCode(userInfo.getUserCode());
      airTransactionDetail.setModifiedBy(userInfo.getUserCode());
      transactionDetailRepository.save(airTransactionDetail);

      // Air Pax Info Persistence
      List<AirPaxInfo> paxInfoList = bookingRequest.getPassengerDetails().stream()
          .map(mondeeHelper::prepareBookingAirPaxInfo).toList();
      AtomicInteger count = new AtomicInteger(0);
      paxInfoList.forEach(item -> {
        item.setNameNo(count.incrementAndGet() + "");
        item.setAirTransactionId(airTransactionHeader.getAirTransactionId());
        item.setAirTransactionDetailId(airTransactionDetail.getAirTransactionDetailId());
      });
      paxInfoRepository.saveAll(paxInfoList);

      // Air Info Persistence
      List<AirInfo> airInfoList = mondeeHelper.prepareBookingAirInfo(flight, bookingRequest, bookingResponse);
      airInfoList.forEach(item -> {
        item.setAirTransactionId(airTransactionHeader.getAirTransactionId());
        item.setAirTransactionDetailId(airTransactionDetail.getAirTransactionDetailId());
      });
      this.airInfoRepository.saveAll(airInfoList);

      //AIR_PAX_BKG_INFO
      List<AirPaxBookingInfo> paxFlights = new ArrayList<>();
      for(AirPaxInfo pax : paxInfoList) {
          AirPaxBookingInfo info = mondeeHelper.prepareAirPaxBkgInfo(flight, pax,bookingResponse,originDestination,bookingRequest);
          info.setAirTransactionId(airTransactionHeader.getAirTransactionId());
          info.setAirTransactionDetailId(airTransactionDetail.getAirTransactionDetailId());
          paxFlights.add(info);
      }
      airPaxBookingInfoRepository.saveAll(paxFlights);

      //LINK_AIR_INFO_PAX_BKG
      List<LinkAirInfoPaxBooking> links = new ArrayList<>();
      airInfoList.forEach(ft -> {
        paxInfoList.forEach(pax -> {
          links.add(LinkAirInfoPaxBooking.builder()
              .airTransactionId(airTransactionHeader.getAirTransactionId())
              .airTransactionDetailId(airTransactionDetail.getAirTransactionDetailId())
              .paxId(pax.getPaxId())
              .airId(ft.getAirId())
              .paxBkgId(mondeeHelper.getPaxBkgId(ft, pax, paxFlights))
              .build()
          );
        });
      });
      linkAirInfoPaxBookingRepository.saveAll(links);

      AirFopCollectedInfo fop = mondeeHelper.getFopCollectedInfo(bookingRequest);
      fop.setAirTransactionId(airTransactionHeader.getAirTransactionId());
      fop.setUserCode(userInfo.getUserCode());
      fop.setModifiedBy(userInfo.getUserCode());
      fop.setStatus("BKG");
      airFopCollectedInfoRepository.save(fop);
    }
    return id;
  }
}
