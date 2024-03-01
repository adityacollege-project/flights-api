/**
 * 
 */
package com.ardor.flights.entity.booking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "LINK_AIR_INFO_PAX_BKG", schema = "ardor")
public class LinkAirInfoPaxBooking {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(name = "AIR_TRANS_ID")
  private String airTransactionId;
  @Column(name = "AIR_TRANS_DTL_ID")
  private String airTransactionDetailId;
  @Column(name = "PAX_ID")
  private String paxId;
  @Column(name = "PAX_BKG_ID")
  private String paxBkgId;
  @Column(name = "AIR_ID")
  private String airId;
}
