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
@Table(name = "AIR_PAX_INFO", schema = "ardor")
public class AirPaxInfo {

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
  @Column(name = "FIRSTNAME")
  private String firstName;
  @Column(name = "MIDDLENAME")
  private String middleName;
  @Column(name = "LASTNAME")
  private String lastName;
  @Column(name = "GENDER")
  private String gender;
  @Column(name = "DOB")
  private Date dob;
  @Column(name = "PASSPORT")
  private String passport;
  @Column(name = "NATIONALITY")
  private String nationality;
  @Column(name = "NAMENO")
  private String nameNo;
  @Column(name = "PAXTYPE")
  private String paxType;
  @Column(name = "TRAVELPREFERENCENO")
  private String travelReferenceNo;
}
