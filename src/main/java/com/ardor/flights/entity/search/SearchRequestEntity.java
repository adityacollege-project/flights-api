package com.ardor.flights.entity.search;

import com.ardor.flights.model.search.FlightSearchRequest.OriginDestination;
import com.ardor.flights.model.search.FlightSearchRequest.PaxDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "flight-search-request")
public class SearchRequestEntity {

  @Id
  private String id;
  private String currencyCode;
  private boolean includeNearByAirports;
  private List<String> allowedCarrierCodes;
  private boolean brandedfares;
  private PaxDetails paxDetails;
  private List<OriginDestination> originDestinations;
  private String searchKey;
  private long noOfResults;
  private Map<String, Integer> brandNoOfResults;
  private List<Map<String, String>> lowestFareByAirline;
  private Map<String, List<Map<String, String>>> lowestFareAirlineByBrand;
  @CreatedDate
  private Date created;

  @JsonIgnore
  private String _class;

  public String getSearchKey() {
    if (StringUtils.isEmpty(searchKey)) {
      generateAndsetSearchKey();
    }
    return searchKey;
  }

  public void generateAndsetSearchKey() {
    StringBuilder odKey = new StringBuilder();
    for (int i = 0; i < this.getOriginDestinations().size(); i++) {
      OriginDestination originDestination = this.getOriginDestinations().get(i);
      if (i == 1 && StringUtils.isNotBlank(
          this.getOriginDestinations().get(i - 1).getReturnDate())) {
        continue;
      }
      odKey.append("-").append(this.prepareOriginDestinationKey(originDestination));
    }
    odKey = new StringBuilder(odKey.substring(1));
    String cabinClassKey = this.getOriginDestinations().get(0).getCabinClass();
    String paxDetailsKey = preparePaxDetailsKey(this.getPaxDetails());
    this.searchKey = String.join("-", odKey.toString(), cabinClassKey, paxDetailsKey,
        String.valueOf(this.isBrandedfares()));
  }

  private String prepareOriginDestinationKey(OriginDestination originDestination) {
    String key = originDestination.getFrom() + "-" + originDestination.getTo()
        + "-" + originDestination.getOnwardDate();
    if (StringUtils.isNotBlank(originDestination.getReturnDate())) {
      key += "-" + originDestination.getReturnDate();
    }
    return key;
  }

  private String preparePaxDetailsKey(PaxDetails paxDetails) {
    return (paxDetails.getAdultCount() + "-" + paxDetails.getChildCount() + "-"
        + paxDetails.getInfantCount());
  }

}
