package com.ardor.flights.model.search;

/**
 * Copyright (c) 2023, Ardor Technologies All rights reserved.
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

import com.ardor.flights.model.search.BaggageAllowance;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightSegment {

  private String fromAirportCode;
  private String fromAirportDisplayName;
  private String fromAirportName;
  private String toAirportCode;
  private String toAirportDisplayName;
  private String toAirportName;
  private String departure;
  private String arrival;
  private int flightNumber;
  private String flightLogoName;
  private String operatingAirline;
  private String operatingAirlineName;
  private String duration;
  private int durationInMinutes;
  private String layoverTime;
  private String airEquipmentType;
  private String bookingCode;
  private String cabinClass;
  private String brandName;
  private String brandId;
  private String brandTier;
  private String fareBasisCode;
  private List<BaggageAllowance> baggageAllowance;

}
