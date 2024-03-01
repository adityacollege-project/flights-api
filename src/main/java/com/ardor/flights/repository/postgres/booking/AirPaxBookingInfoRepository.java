/**
 * 
 */
package com.ardor.flights.repository.postgres.booking;

import com.ardor.flights.entity.booking.AirPaxBookingInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 */
public interface AirPaxBookingInfoRepository extends JpaRepository<AirPaxBookingInfo, Long>{
  List<AirPaxBookingInfo> findByAirTransactionId(String airTransactionId);

}
