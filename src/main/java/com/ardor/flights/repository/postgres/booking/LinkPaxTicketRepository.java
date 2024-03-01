/**
 * 
 */
package com.ardor.flights.repository.postgres.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ardor.flights.entity.booking.LinkAirInfoPaxTicket;

/**
 * 
 */
@Repository
public interface LinkPaxTicketRepository extends JpaRepository<LinkAirInfoPaxTicket, Long> {
	
}
