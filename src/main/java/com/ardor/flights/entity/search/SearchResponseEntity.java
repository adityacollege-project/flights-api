package com.ardor.flights.entity.search;

import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "flight-search-response")
public class SearchResponseEntity {

  @Id
  private String id;
  private String responseKey;
  private String searchKey;
  private List<FlightsResponse> flights;
  private Map<String, List<FlightsResponse>> brandFlights;
  @CreatedDate
  private Date created;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class FlightsResponse {

    private String jId;
    private String supplierItineraryId;
    private String bookingClasses;
    private String airlineCode;
    private String cabinClass;
    private String fareBasisCodes;
    private double totalBaseFare;
    private double totalTaxes;
    private List<Map<String, Object>> brands;
  }

}
