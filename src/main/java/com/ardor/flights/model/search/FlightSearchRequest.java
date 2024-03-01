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

import com.ardor.flights.common.Constants;
import com.ardor.flights.annotation.DateValidation;
import com.ardor.flights.annotation.EnumValidation;
import com.ardor.flights.annotation.OriginDestinationValidation;
import com.ardor.flights.annotation.PaxValidation;
import com.ardor.flights.common.ErrorCodeConstants;
import jakarta.validation.GroupSequence;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FlightSearchRequest is a model class representing a flight search request. It contains
 * information about passenger details and a list of origin-destination pairs. The model includes
 * validation annotations to ensure the integrity of the provided data.
 *
 * @author mkkumar
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@GroupSequence({FlightSearchRequest.class, NotNull.class, Size.class,PaxValidation.class, OriginDestinationValidation.class})
public class FlightSearchRequest {

  private String currencyCode;
  private boolean includeNearByAirports;
  private List<String> allowedCarrierCodes;
  private boolean brandedfares;
  private boolean incremental;

  @Valid
  @NotNull(message = "FSW0020:Passenger details can not be blank.", groups = NotNull.class)
  @PaxValidation(message = "FSW0021:Invalid pax details", groups = PaxValidation.class)
  private PaxDetails paxDetails;

  @Valid
  @Size(max = 5,message = ErrorCodeConstants.FSE_0024,groups = Size.class)
  @NotNull(message = "FSW0018:Origin and Destination cannot be blank.", groups = NotNull.class)
  @OriginDestinationValidation(message = "FSW0019:Invalid origin and destination", groups = OriginDestinationValidation.class)
  private List<OriginDestination> originDestinations;

  /**
   * Represents passenger details within the flight search request.
   */
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @GroupSequence({PaxDetails.class, Min.class, Max.class})
  public static class PaxDetails {

    @Min(value = 1, message = "FSW0011:The adult count must be at least 1", groups = Min.class)
    @Max(value = 9, message = "FSW0012:The adult count must be a maximum of 9", groups = Max.class)
    private int adultCount;

    @Min(value = 0, message = "FSW0013:The child count should not be less than 0", groups = Min.class)
    @Max(value = 8, message = "FSW0014:The child count must be a maximum of 8", groups = Max.class)
    private int childCount;

    @Min(value = 0, message = "FSW0015:The infant count should not be less than 0", groups = Min.class)
    @Max(value = 8, message = "FSW0016:The infant count must be a maximum of 8", groups = Max.class)
    private int infantCount;
  }

  /**
   * Represents an origin-destination pair within the flight search request.
   */
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @GroupSequence({OriginDestination.class, NotNull.class, NotBlank.class, Pattern.class, Size.class,
      DateValidation.class, EnumValidation.class})
  public static class OriginDestination {

    @NotNull(message = "FSW0002:Origin cannot be blank.", groups = NotNull.class)
    @NotBlank(message = "FSW0002:Origin cannot be blank.", groups = NotBlank.class)
    @Pattern(regexp = Constants.ALPHABET_PATTERN_REGEX, message = "FSW0022:Only Alphabets allowed. Numbers, Spaces & special characters not allowed.", groups = Pattern.class)
    @Size(min = 3, max = 3, message = "FSW0004:Origin must be exactly 3 characters long.", groups = Size.class)
    private String from;

    @NotNull(message = "FSW0003:Destination cannot be blank.", groups = NotNull.class)
    @NotBlank(message = "FSW0003:Destination cannot be blank.", groups = NotBlank.class)
    @Pattern(regexp = Constants.ALPHABET_PATTERN_REGEX, message = "FSW0022:Only Alphabets allowed. Numbers, Spaces & special characters not allowed.", groups = Pattern.class)
    @Size(min = 3, max = 3, message = "FSW0005:Destination must be exactly 3 characters long.", groups = Size.class)
    private String to;

    @NotNull(message = "FSW0007:Onward date cannot be blank.", groups = NotNull.class)
    @NotBlank(message = "FSW0007:Onward date cannot be blank.", groups = NotBlank.class)
    @Pattern(regexp = Constants.DATE_PATTERN_REGEX, message = "FSW0008:Onward date must be in yyyy/MM/dd format", groups = Pattern.class)
    @DateValidation(message = "FSW0009:Invalid Onward date.", groups = DateValidation.class)
    private String onwardDate;

    private String returnDate;

    @NotNull(message = "FSW0010:Cabin class cannot be blank.", groups = NotNull.class)
    @NotBlank(message = "FSW0010:Cabin class cannot be blank.", groups = NotBlank.class)
    @EnumValidation(message = "FSW0023:Invalid Cabin class.", enumClass = CabinClassType.class, groups = EnumValidation.class)
    private String cabinClass;
  }

  public enum CabinClassType {
    E("E"),
    P("P"),
    B("B"),
    F("F");

    private final String cabinClassName;

    CabinClassType(String cabinClassName) {
      this.cabinClassName = cabinClassName;
    }

    public String getCabinClassName() {
      return this.cabinClassName;
    }
  }

}
