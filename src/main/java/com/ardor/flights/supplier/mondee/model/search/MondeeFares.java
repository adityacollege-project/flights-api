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

import com.ardor.flights.supplier.mondee.model.search.MondeeBaggageAllowance;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MondeeFares {

  @JsonProperty("CurrencyCode")
  private String currencyCode;
  @JsonProperty("BaseFare")
  private double baseFare;
  @JsonProperty("Taxes")
  private double taxes;
  @JsonProperty("PaxType")
  private String paxType;
  @JsonProperty("fareBasisCodes")
  private String fareBasisCodes;
  @JsonProperty("bookingClasses")
  private String bookingClasses;
  @JsonProperty("FareTypeIndicator")
  private String fareTypeIndicator;
  @JsonProperty("baggageAllowance")
  private Map<String, List<MondeeBaggageAllowance>> baggageAllowance;
  @JsonProperty("ChangeFare")
  private double changeFare;

}
