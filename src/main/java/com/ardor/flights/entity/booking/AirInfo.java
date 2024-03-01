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
@Table(name = "AIR_INFO", schema = "ardor")
public class AirInfo {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(name = "AIR_TRANS_ID")
  private String airTransactionId;
  @Column(name = "AIR_TRANS_DTL_ID")
  private String airTransactionDetailId;
  @Column(name = "AIR_ID")
  private String airId;
  @Column(name = "ORIGIN")
  private String origin;
  @Column(name = "DESTINATION")
  private String destination;
  @Column(name = "AIRLINE_CODE")
  private String airlineCode;
  @Column(name = "DEPART_DATETIME")
  private Date departDateTime;
  @Column(name = "ARRIVAL_DATETIME")
  private Date arrivalDateTime;
  @Column(name = "CABIN")
  private String cabin;
  @Column(name = "BKG_CLASS")
  private String bookingClass;
  @Column(name = "FLIGHTNO")
  private String flightNo;
  @Column(name = "SEATNO")
  private String seatNo;
  @Column(name = "FARE")
  private double fare;
  @Column(name = "YQ")
  private double taxes;
  @Column(name = "YR")
  private double yr;
  @Column(name = "AIRLINE_PNR")
  private String pnr;
//  @Column(name = "DEPART_FLAG")
//  private boolean departFlag;
  @Column(name = "LEG_NO")
  private int legNo;
  @Column(name = "OP_AIRLINE")
  private String operatingAirline;
  @Column(name = "MK_AIRLINE")
  private String marketingAirline;
  @Column(name = "FARE_TYPE")
  private String fareType;
  @Column(name = "FAREBASISCODE")
  private String fareBasisCode;
  @Column(name = "TOURCODE")
  private String tourCode;
  @Column(name = "FARE_INFO")
  private String fareInfo;
  @Column(name = "ENDORSEMENT")
  private String endorsement;
  @Column(name = "ADDL_BAGGAGE")
  private String additionalBaggage;
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATED", updatable = false)
  private Date created;
  @Column(name = "MODIFIED")
  private Date modified;
}
