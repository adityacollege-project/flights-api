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
@Table(name = "AIR_FOP_COLLECTED", schema = "ardor")
public class AirFopCollectedInfo {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(name = "AIR_TRANS_ID")
  private String airTransactionId;
  @Column(name = "AIR_FC_ID")
  private String fcId;
  @Column(name = "USER_CODE")
  private String userCode;
  @Column(name = "MODIFIED_BY")
  private String modifiedBy;
  @Column(name = "CREATED", updatable = false)
  private Date created;
  @Column(name = "MODIFIED")
  private Date modified;
  @Column(name = "FOP_TYPE")
  private String type;
  @Column(name = "FOP_STATUS")
  private String status;
  @Column(name = "AUTH_AMOUNT")
  private double authAmount;
  @Column(name = "SETTLED_AMOUNT")
  private double settledAmount;
  @Column(name = "CCFEE")
  private double ccFee;
  @Column(name = "FIRSTEIGHT")
  private String firstEightDigits;
  @Column(name = "LASTFOUR")
  private String lastFourDigits;
  @Column(name = "CCINFO")
  private String ccInfo;
  @Column(name = "CCNO")
  private String ccNum;
  @Column(name = "CCCODE")
  private String ccCode;
  @Column(name = "CCEXP")
  private String ccExp; //MMYY
  @Column(name = "CCHOLDER_NAME")
  private String cardHolderName;
  @Column(name = "CCBANK")
  private String ccBank;
//  @Column(name = "CONSIDER_AMT")
//  private byte considerAmount;
}
