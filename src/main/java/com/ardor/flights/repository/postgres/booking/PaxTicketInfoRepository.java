/**
 * 
 */
package com.ardor.flights.repository.postgres.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ardor.flights.entity.booking.AirPaxTicketInfo;

/**
 * 
 */
@Repository
public interface PaxTicketInfoRepository extends JpaRepository<AirPaxTicketInfo, Long>  {

}
