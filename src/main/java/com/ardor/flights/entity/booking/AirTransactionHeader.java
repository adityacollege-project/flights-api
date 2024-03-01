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
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.annotation.CreatedDate;

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
@Table(name = "AIR_TRANS_HDR", schema = "ardor")
public class AirTransactionHeader {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(name = "AIR_TRANS_ID", unique = true)
  private String airTransactionId;
  @Column(name = "USER_CODE")
  private String userCode;
  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATED", updatable = false)
  private Date created;
  @Column(name = "MODIFIED")
  private Date modified;
  @Column(name = "MODIFIED_BY")
  private String modifiedBy;
  @Column(name = "FIRSTNAME")
  private String firstName;
  @Column(name = "LASTNAME")
  private String lastName;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "PHONE")
  private String phone;
  @Column(name = "ADDRESS")
  private String address;
  @Column(name = "TRANS_STATUS")
  private String transactionStatus;
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
  @Column(name = "PAX")
  private String pax;
  @Column(name = "TRANS_INFO")
  private String transactionInfo;
  @Column(name = "REMARKS")
  private String remarks;
  @Column(name = "TRANS_IP")
  private String transIP;
  @Column(name = "TRANS_COUNTRY_CODE")
  private String transCountryCode;
}
