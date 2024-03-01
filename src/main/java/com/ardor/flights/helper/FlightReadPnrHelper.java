/**
 *
 */
package com.ardor.flights.helper;

import com.ardor.flights.entity.booking.AirInfo;
import com.ardor.flights.entity.booking.AirPaxInfo;
import com.ardor.flights.entity.booking.AirPaxTicketInfo;
import com.ardor.flights.entity.booking.AirTransactionDetail;
import com.ardor.flights.entity.booking.AirTransactionHeader;
import com.ardor.flights.entity.booking.LinkAirInfoPaxTicket;
import com.ardor.flights.model.readpnr.Ticket;
import com.ardor.flights.repository.postgres.booking.AirInfoRepository;
import com.ardor.flights.repository.postgres.booking.AirPaxInfoRepository;
import com.ardor.flights.repository.postgres.booking.LinkPaxTicketRepository;
import com.ardor.flights.repository.postgres.booking.PaxTicketInfoRepository;
import com.ardor.flights.repository.postgres.booking.TransactionDetailRepository;
import com.ardor.flights.repository.postgres.booking.TransactionHeaderRepository;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.ardor.flights.model.cancel.FlightCancelResponse;
import com.ardor.flights.model.readpnr.FlightReadPnrRequest;
import com.ardor.flights.model.readpnr.FlightReadPnrResponse;
import com.ardor.flights.search.supplier.mondee.MondeeSupplier;

import jakarta.validation.Valid;

/**
 *
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class FlightReadPnrHelper {

	private static final String MONDEE = "mondee";
	private final MondeeSupplier mondeeSupplier;
	private final AirInfoRepository airInfoRepository;
	private final AirPaxInfoRepository paxInfoRepository;
	private final TransactionDetailRepository transactionDetailRepository;
	private final TransactionHeaderRepository transactionHeaderRepository;
	private final PaxTicketInfoRepository paxTicketInfoRepository;
	private final LinkPaxTicketRepository linkPaxTicketRepository;

	public FlightReadPnrResponse readPnr(@Valid FlightReadPnrRequest readPnrRequest) {
		FlightReadPnrResponse response = mondeeSupplier.readPnr(readPnrRequest);
		updateTicketNumbers(readPnrRequest, response);
		return response;
	}

	public void updateTicketNumbers(FlightReadPnrRequest readPnrRequest, FlightReadPnrResponse readResp) {
		try {
			AirTransactionHeader transaction = transactionHeaderRepository.findByAirTransactionId(readPnrRequest.getReferenceNumber());
			if(!readResp.getTickets().isEmpty() && !"TKT".equalsIgnoreCase(transaction.getTransactionStatus())) {
				List<AirTransactionDetail> airTransactionDetails = transactionDetailRepository.findByAirTransactionId(readPnrRequest.getReferenceNumber());
				Optional<AirTransactionDetail> airTransactionDetail = airTransactionDetails.stream().filter(detail -> readPnrRequest.getPNR().equalsIgnoreCase(detail.getPnr())).findFirst();
				AirTransactionDetail detail = airTransactionDetail.get();
				List<AirPaxInfo> paxList = paxInfoRepository.findByAirTransactionId(readPnrRequest.getReferenceNumber());
				List<AirInfo> flights = airInfoRepository.findByAirTransactionId(readPnrRequest.getReferenceNumber());
				List<AirPaxTicketInfo> tickets = new ArrayList<>();
				for (int i = 0; i < readResp.getTickets().size(); i++) {
					Ticket ticket = readResp.getTickets().get(i);
					AirPaxInfo pax = getMatchedPaxInfo(ticket, paxList);
					tickets.add(AirPaxTicketInfo.builder()
							.ticket(ticket.getTicketCode() + ticket.getTicketNumber())
							.airline(ticket.getAirline())
							.ccFee(0.0)
							.baseFare(0.0)
							.tax(0.0)
							.itinerary(ticket.getSectors() + "")
							.orgDest(ticket.getSectors().get(0))
							.airTransactionId(transaction.getAirTransactionId())
							.airTransactionDetailId(detail.getAirTransactionDetailId())
							.paxId(pax.getPaxId())
							.pnr(detail.getPnr())
							.fareType(readResp.getFlight().getFareType())
							.created(new Date(System.currentTimeMillis()))
							.modified(new Date(System.currentTimeMillis()))
							.build()
					);
				}
				paxTicketInfoRepository.saveAll(tickets);
				List<LinkAirInfoPaxTicket> links = new ArrayList<>();
				flights.forEach(flight -> {
					tickets.forEach(tkt -> {
						if(tkt.getItinerary().contains(flight.getOrigin() + "" + flight.getDestination())) {
							links.add(LinkAirInfoPaxTicket.builder()
									.airTransactionId(transaction.getAirTransactionId())
									.airTransactionDetailId(detail.getAirTransactionDetailId())
									.airId(flight.getId())
									.paxTktId(tkt.getPaxTktId())
									.paxId(tkt.getPaxId())
									.build()
							);
						}
					});
				});
				linkPaxTicketRepository.saveAll(links);
				updateTktStatus(transaction);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private void updateTktStatus(AirTransactionHeader transaction) {
		transaction.setTransactionStatus("TKT");
		transaction.setModified(new Timestamp(System.currentTimeMillis()));
		transactionHeaderRepository.save(transaction);
	}

	private AirPaxInfo getMatchedPaxInfo(Ticket ticket, List<AirPaxInfo> paxList) {
		return paxList.stream().filter(pax -> pax.getFirstName().equalsIgnoreCase(ticket.getFirstName()) && pax.getLastName().equalsIgnoreCase(ticket.getLastName()))
				.findFirst().get();
	}

}
