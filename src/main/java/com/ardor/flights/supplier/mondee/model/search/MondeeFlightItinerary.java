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

import com.ardor.flights.supplier.mondee.model.search.MondeeBrandFares;
import com.ardor.flights.supplier.mondee.model.search.MondeeCityPairs;
import com.ardor.flights.supplier.mondee.model.search.MondeeFares;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MondeeFlightItinerary {

  private String jId;
  @JsonProperty("ItineraryId")
  private String itineraryId;
  @JsonProperty("ValidatingCarrierCode")
  private String validatingCarrierCode;
  @JsonProperty("ValidatingCarrierName")
  private String validatingCarrierName;
  @JsonProperty("Citypairs")
  private List<MondeeCityPairs> segments;
  @JsonProperty("Fares")
  private List<MondeeFares> fares;
  @JsonProperty("brandFares")
  private List<MondeeBrandFares> brands;
  @JsonProperty("BrandName")
  private String brandName;
  @JsonProperty("CabinClass")
  private String cabinClass;
  @JsonProperty("FareType")
  private String fareType;
  @JsonProperty("IsFareChanged")
  private boolean isFareChanged;

}
