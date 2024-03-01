package com.ardor.flights.supplier.mondee.model.search;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MondeeFlightSegment {

  @JsonProperty("DepartureLocationCode")
  private String fromAirportCode;
  @JsonProperty("DepartureDisplayName")
  private String fromAirportDisplayName;
  @JsonProperty("OriginAirportName")
  private String fromAirportName;
  @JsonProperty("ArrivalLocationCode")
  private String toAirportCode;
  @JsonProperty("DisplayName")
  private String toAirportDisplayName;
  @JsonProperty("DestinationAirportName")
  private String toAirportName;
  @JsonProperty("DepartureDateTime")
  private String departure;
  @JsonProperty("ArrivalDateTime")
  private String arrival;
  @JsonProperty("FlightNumber")
  private int flightNumber;
  @JsonProperty("FlightLogoName")
  private String flightLogoName;
  @JsonProperty("OperatingAirline")
  private String operatingAirline;
  @JsonProperty("OperatingAirlineName")
  private String operatingAirlineName;
  @JsonProperty("Duration")
  private String duration;
  @JsonProperty("DurationInMinutes")
  private int durationInMinutes;
  @JsonProperty("LayoverTime")
  private String layoverTime;
  @JsonProperty("AirEquipmentType")
  private String airEquipmentType;
  @JsonProperty("BookingClass")
  private String bookingCode;
  @JsonProperty("CabinClass")
  private String cabinClass;
  @JsonProperty("BrandName")
  private String brandName;
  @JsonProperty("BrandId")
  private String brandId;
  @JsonProperty("BrandTier")
  private String brandTier;
  @JsonProperty("FareBasisCode")
  private String fareBasisCode;

}
