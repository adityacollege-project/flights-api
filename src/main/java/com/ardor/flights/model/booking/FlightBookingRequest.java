package com.ardor.flights.model.booking;

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

import com.ardor.flights.annotation.*;
import com.ardor.flights.common.ErrorCodeConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.GroupSequence;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ardor.flights.common.Constants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@GroupSequence({FlightBookingRequest.class, NotNull.class, NotEmpty.class, JidValidation.class,
    fareIdValidation.class})
public class FlightBookingRequest {

  @Valid
  @NotEmpty(message = ErrorCodeConstants.FBE_0001,groups = NotEmpty.class)
  @JidValidation(message = ErrorCodeConstants.FBE_0001,groups = JidValidation.class)
  private List<String> jIds;

  @Valid
  @NotEmpty(message = ErrorCodeConstants.FBE_0002, groups = NotEmpty.class)
  @fareIdValidation(message = ErrorCodeConstants.FBE_0006,groups = fareIdValidation.class)
  private Map<String, String> fareIds;

  @Valid
  @NotEmpty(message = ErrorCodeConstants.FBE_0048, groups = NotEmpty.class)
  private List<PassengerDetails> passengerDetails;

  @Valid
  @NotNull(message = ErrorCodeConstants.FBE_0057, groups = NotNull.class)
  private ContactInfo contactInfo;

  @Valid
  @NotNull(message = ErrorCodeConstants.FBE_0059, groups = NotNull.class)
  private PaymentDetails paymentDetails;


  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @GroupSequence({PassengerDetails.class, NotNull.class, NotBlank.class, Pattern.class, DOBValidation.class})
  public static class PassengerDetails {

    @NotNull(message = ErrorCodeConstants.FBE_0007, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0008, groups = NotBlank.class)
    @Pattern(regexp = "ADT|CHD|INF", message = ErrorCodeConstants.FBE_0049, groups = Pattern.class)
    private String paxType;

    @NotNull(message = ErrorCodeConstants.FBE_0009, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0010, groups = NotBlank.class)
    @Pattern(regexp = "[MFO]", message = ErrorCodeConstants.FBE_0050, groups = Pattern.class)
    private String gender;

    @NotNull(message = ErrorCodeConstants.FBE_0011, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0012, groups = NotBlank.class)
    @Pattern(regexp = "Mr|Mrs", message = ErrorCodeConstants.FBE_0051, groups = Pattern.class)
    private String userTitle;

    @NotNull(message = ErrorCodeConstants.FBE_0013, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0014, groups = NotBlank.class)
    @Pattern(regexp = Constants.ALPHABET_PATTERN_REGEX, message = ErrorCodeConstants.FBE_0052, groups = Pattern.class)
    private String firstName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Pattern(regexp = Constants.ALPHABET_PATTERN_REGEX, message = ErrorCodeConstants.FBE_0053, groups = Pattern.class)
    private String middleName;

    @NotNull(message = ErrorCodeConstants.FBE_0015, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0016, groups = NotBlank.class)
    @Pattern(regexp = Constants.ALPHABET_PATTERN_REGEX, message = ErrorCodeConstants.FBE_0054, groups = Pattern.class)
    private String lastName;

    @NotNull(message = ErrorCodeConstants.FBE_0017, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0018, groups = NotBlank.class)
    @Pattern(regexp = Constants.DOB_PATTERN, message = ErrorCodeConstants.FBE_0055, groups = Pattern.class)
    @DOBValidation(message = ErrorCodeConstants.FBE_0056, groups = DOBValidation.class)
    private String dateOfBirth;
    private String passportNumber;
    private String countryOfIssue;
    private String nationality;
    private String passportIssueDate;
    private String passportExpiryDate;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @GroupSequence({ContactInfo.class, NotNull.class, NotBlank.class, Pattern.class})
  public static class ContactInfo {

    @NotNull(message = ErrorCodeConstants.FBE_0019, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0020, groups = NotBlank.class)
    @Pattern(regexp = "^\\d{0,11}$",message = ErrorCodeConstants.FBE_0021,groups = Pattern.class)
    private String phoneNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Pattern(regexp = "^\\d{0,11}$",message = ErrorCodeConstants.FBE_0021,groups = Pattern.class)
    private String alternatePhoneNumber;

    @NotNull(message = ErrorCodeConstants.FBE_0022, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0023, groups = NotBlank.class)
    @Pattern(regexp = "^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$", message = ErrorCodeConstants.FBE_0058,groups = Pattern.class)
    private String email;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @GroupSequence({PaymentDetails.class, NotNull.class, NotBlank.class})
  public static class PaymentDetails {

    @NotNull(message = ErrorCodeConstants.FBE_0024,groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0025,groups = NotBlank.class)
    @Pattern(regexp = "CC|CK|HOLD", message = ErrorCodeConstants.FBE_0060,groups = Pattern.class)
    private String paymentType;

    @Valid
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CreditCardDetails creditCardDetails;

    @Valid
    @NotNull(message = ErrorCodeConstants.FBE_0038, groups = NotNull.class)
    private BillingAddress billingAddress;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @GroupSequence({CreditCardDetails.class, NotNull.class, NotBlank.class, /*Size.class,*/ Pattern.class, LuhnValid.class, ValidExpiryDate.class})
  public static class CreditCardDetails {

    @NotNull(message = ErrorCodeConstants.FBE_0026, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0027, groups = NotBlank.class)
    @Pattern(regexp = "VI|CA", message = ErrorCodeConstants.FBE_0063,groups = Pattern.class)
    private String cardType;

    @NotNull(message = ErrorCodeConstants.FBE_0028, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0029, groups = NotBlank.class)
    //@Size(min = 16, max = 16, message = ErrorCodeConstants.FBE_0064, groups = Size.class)
    @Pattern(regexp = "^\\d{4}-\\d{4}-\\d{4}-\\d{4}$", message = ErrorCodeConstants.FBE_0062,groups = Pattern.class)
    @LuhnValid(message = ErrorCodeConstants.FBE_0062, groups = LuhnValid.class)
    private String cardNumber;

    @NotNull(message = ErrorCodeConstants.FBE_0030, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0031, groups = NotBlank.class)
    @Pattern(regexp = "^\\d{3}$", message = ErrorCodeConstants.FBE_0065, groups = Pattern.class)
    private String cvv;

    @NotNull(message = ErrorCodeConstants.FBE_0032, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0033, groups = NotBlank.class)
    @Pattern(regexp = Constants.EXPIRY_DATE_PATTERN, message = ErrorCodeConstants.FBE_0066, groups = Pattern.class)
    @ValidExpiryDate(message = ErrorCodeConstants.FBE_0067, groups = ValidExpiryDate.class)
    private String expiryDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Pattern(regexp = "^\\d{0,11}$", message = ErrorCodeConstants.FBE_0021,groups = Pattern.class)
    private String bankPhoneNumber;

    @NotNull(message = ErrorCodeConstants.FBE_0034, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0035, groups = NotBlank.class)
    @Pattern(regexp = "^\\d{0,11}$", message = ErrorCodeConstants.FBE_0021,groups = Pattern.class)
    private String billingPhoneNumber;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @GroupSequence({BillingAddress.class,NotNull.class, NotBlank.class, Pattern.class})
  public static class BillingAddress {

    @NotNull(message = ErrorCodeConstants.FBE_0036, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0037, groups = NotBlank.class)
    @Pattern(regexp = Constants.ALPHABET_PATTERN_REGEX, message = ErrorCodeConstants.FBE_0072, groups = Pattern.class)
    private String name;

    @NotNull(message = ErrorCodeConstants.FBE_0038, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0039, groups = NotBlank.class)
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = ErrorCodeConstants.FBE_0073, groups = Pattern.class)
    private String address1;

    private String address2;

    @NotNull(message = ErrorCodeConstants.FBE_0040, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0041, groups = NotBlank.class)
    @Pattern(regexp = Constants.ALPHABET_PATTERN_REGEX, message = ErrorCodeConstants.FBE_0068, groups = Pattern.class)
    private String city;

    @NotNull(message = ErrorCodeConstants.FBE_0042, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0043, groups = NotBlank.class)
    @Pattern(regexp = Constants.ALPHABET_PATTERN_REGEX, message = ErrorCodeConstants.FBE_0069, groups = Pattern.class)
    private String state;

    @NotNull(message = ErrorCodeConstants.FBE_0044, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0045, groups = NotBlank.class)
    @Pattern(regexp = Constants.ALPHABET_PATTERN_REGEX, message = ErrorCodeConstants.FBE_0070, groups = Pattern.class)
    private String country;

    @NotNull(message = ErrorCodeConstants.FBE_0046, groups = NotNull.class)
    @NotBlank(message = ErrorCodeConstants.FBE_0047, groups = NotBlank.class)
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = ErrorCodeConstants.FBE_0071, groups = Pattern.class)
    private String zipCode;
  }

}
