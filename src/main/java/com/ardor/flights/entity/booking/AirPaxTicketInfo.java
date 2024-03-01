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
import java.util.Date;
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
@Table(name = "AIR_PAX_TKT_INFO", schema = "ardor")
public class AirPaxTicketInfo {

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
  @Column(name = "PAX_TKT_ID", unique = true)
  private String paxTktId;
  @Column(name = "PNR")
  private String pnr;
  @Column(name = "AIRLINE_CODE")
  private String airline;
  @Column(name = "TICKETNO")
  private String ticket;
  @Column(name = "GDS")
  private String gds;
  @Column(name = "FARETYPE")
  private String fareType;
  @Column(name = "FAREINFO")
  private String fareInfo;
  @Column(name = "ENDORSEMENT")
  private String endorsement;
  @Column(name = "TOURCODE")
  private String tourCode;
  @Column(name = "FAREBASIS")
  private String farebasis;
  @Column(name = "ITINERARY")
  private String itinerary;
  @Column(name = "ORGDEST")
  private String orgDest;
  @Column(name = "MC_ORGDEST")
  private String mcOrgDest;
  @Column(name = "TICKETFARE")
  private Double tktFare;
  @Column(name = "BASEFARE")
  private Double baseFare;
  @Column(name = "TAX")
  private Double tax;
  @Column(name = "TAXBREAKUP")
  private Double taxBreakUp;
  @Column(name = "COMM")
  private Double comm;
  @Column(name = "NETIATAAMT")
  private Double netIataAmount;
  @Column(name = "FOPCODE")
  private String fop;
  @Column(name = "TRANS_TYPE")
  private String transType;
  @Column(name = "TRANS_STATUS")
  private String transStatus;
  @Column(name = "PENALITY")
  private Double penalty;
  @Column(name = "COMM_ON_PENALITY")
  private Double comOnPenalty;
  @Column(name = "ORIGTICKETNO")
  private String orgTkt;
  @Column(name = "ORIGBASE_FARE")
  private double orgBasefare;
  @Column(name = "ORIGTAX")
  private double orgTax;
  @Column(name = "ORIGCOMM")
  private double orgComm;
  @Column(name = "ORIGFOPCODE")
  private String orgFopl;
  @Column(name = "TOTALFARE")
  private double totFare;
  @Column(name = "NETFARE")
  private double netFare;
  @Column(name = "MARKUP")
  private double markup;
  @Column(name = "PROMOTION")
  private double promotion;
  @Column(name = "ADJUSTMENT")
  private double adjustment;
  @Column(name = "CCFEE")
  private double ccFee;
  @Column(name = "MISC")
  private double misc;
  @Column(name = "EXFEE")
  private double exFee;
  @Column(name = "RFFEE")
  private double reFee;
  @Column(name = "RECALLFEE")
  private double recallFee;
  @Column(name = "REMARKS")
  private String remarks;
  @Column(name = "CREATED", updatable = false)
  private Date created;
  @Column(name = "MODIFIED")
  private Date modified;
}
