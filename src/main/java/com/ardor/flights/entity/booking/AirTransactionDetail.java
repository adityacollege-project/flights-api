package com.ardor.flights.entity.booking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "AIR_TRANS_DTL", schema = "ardor")
public class AirTransactionDetail {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(name = "AIR_TRANS_ID")
  private String airTransactionId;
  @Column(name = "AIR_TRANS_DTL_ID")
  private String airTransactionDetailId;
  @Column(name = "BOOKING_TYPE")
  private String bookingType;
  @Column(name = "TRANS_TYPE")
  private String transactionType;
  @Column(name = "TRANS_STATUS")
  private String transactionStatus;
  @Column(name = "USER_CODE")
  private String userCode;
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATED", updatable = false)
  private Date created;
  @Column(name = "MODIFIED")
  private Date modified;
  @Column(name = "MODIFIED_BY")
  private String modifiedBy;
  @Column(name = "PNR")
  private String pnr;
  @Column(name = "AIRLINE_CODE")
  private String airlineCode;
  @Column(name = "SUPPLIER")
  private String supplier;
  @Column(name = "SUPPLIER_REF")
  private String supplierRef;
  @Column(name = "QUOTED")
  private double quoted;
  @Column(name = "QUOTED_COST")
  private double quotedCost;
  @Column(name = "QUOTED_ADDITIONAL")
  private double quotedAdditional;
  @Column(name = "SUP_COST")
  private double supplierCost;
  @Column(name = "NET_COST")
  private double netCost;
  @Column(name = "NET_REMIT")
  private double netRemit;
  @Column(name = "CHARGED")
  private double charged;
  @Column(name = "REVENUE")
  private double revenue;
  @Column(name = "UNDER_CHARGE")
  private double underCharge;
  @Column(name = "OVER_CHARGE")
  private double overCharge;
  @Column(name = "MARKUP")
  private double markup;
  @Column(name = "PROMOTION")
  private double promotion;
  @Column(name = "ADJUSTMENT")
  private double adjustment;
  @Column(name = "CCFEE")
  private double ccFee;
  @Column(name = "CUSTOMER_FOP")
  private double customerFop;
  @Column(name = "SUPPLIER_FOP")
  private double supplierFop;
  @Column(name = "TRANS_INFO")
  private String transactionInfo;
  @Column(name = "REMARKS")
  private String remarks;
  @Column(name = "PAX")
  private int pax;
  @Column(name = "ADULT")
  private int adult;
  @Column(name = "CHILD")
  private int child;
  @Column(name = "INFANT")
  private int infant;
  @Column(name = "JOURNEY_TYPE")
  private String journeyType;
  @Column(name = "ORIGIN")
  private String origin;
  @Column(name = "DESTINATION")
  private String destination;
  @Column(name = "ONWARD_DATE")
  private Date onwardDate;
  @Column(name = "RETURN_DATE")
  private Date returnDate;
  @Column(name = "RETURN_DATE1")
  private Date returnDate1;
  @Column(name = "RETURN_DATE2")
  private Date returnDate2;
  @Column(name = "RETURN_DATE3")
  private Date returnDate3;
  @Column(name = "ITINERARY")
  private String itinerary;
  @Column(name = "MULTICITY_ITINERARY")
  private String multiCityItinerary;
  @Column(name = "CABIN")
  private String cabin;
  @Column(name = "BKG_CLASS")
  private String bookingClass;
  @Column(name = "ANCILLARY_TYPE")
  private String ancillaryType;
}
