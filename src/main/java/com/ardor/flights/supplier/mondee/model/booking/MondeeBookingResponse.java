package com.ardor.flights.supplier.mondee.model.booking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class MondeeBookingResponse {

  @JsonProperty("bookResponse")
  private List<BookingResponse> bookResponse;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class BookingResponse {

    @JsonProperty("bookingStatus")
    private String bookingStatus;
    @JsonProperty("PNR")
    private String PNR;
    @JsonProperty("ReferenceNumber")
    private String referenceNumber;
    @JsonProperty("errorsList")
    private ErrorsList errorsList;
    @JsonProperty("multiItinInSingleBooking")
    private boolean roundTripPnr;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ErrorsList {

    @JsonProperty("empty")
    private boolean empty;
    @JsonProperty("tperror")
    private List<TypeError> tperror;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class TypeError {

    @JsonProperty("errorCode")
    private String errorCode;
    @JsonProperty("errorType")
    private String errorType;
    @JsonProperty("errorText")
    private String errorText;
    @JsonProperty("errorDetail")
    private ErrorDetail errorDetail;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class ErrorDetail {

    @JsonProperty("severity")
    private String severity;
  }

}
