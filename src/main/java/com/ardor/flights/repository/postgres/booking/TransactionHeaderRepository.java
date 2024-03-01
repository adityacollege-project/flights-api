package com.ardor.flights.repository.postgres.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ardor.flights.entity.booking.AirTransactionHeader;

@Repository
public interface TransactionHeaderRepository extends JpaRepository<AirTransactionHeader, Long> {

	AirTransactionHeader findByAirTransactionId(String airTransactionId);

}
