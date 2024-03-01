package com.ardor.flights.supplier.mondee.model.booking;

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
public class MondeeBookingRequest {

  private List<String> ItineraryIds;
  private Map<String, String> FareIds;
  private List<BookItineraryPaxDetail> BookItineraryPaxDetail;
  private BookItineraryPaxContactInfo BookItineraryPaxContactInfo;
  private BookItineraryPaymentDetail BookItineraryPaymentDetail;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class BookItineraryPaxDetail {

    private String PaxType;
    private String Gender;
    private String UserTitle;
    private String FirstName;
    private String MiddleName;
    private String LastName;
    private String DateOfBirth;
    private String PassportNumber;
    private String CountryOfIssue;
    private String Nationality;
    private String PassportIssueDate;
    private String PassportExpiryDate;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class BookItineraryPaxContactInfo {

    private String PhoneNumber;
    private String AlternatePhoneNumber;
    private String Email;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class BookItineraryPaymentDetail {

    private String PaymentType;
    private BookItineraryCCDetails BookItineraryCCDetails;
    private BookItineraryBillingAddress BookItineraryBillingAddress;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class BookItineraryCCDetails {

    private String CardType;
    private String CardNumber;
    private String CVV;
    private String ExpiryDate;
    private String BankPhoneNum;
    private String BillingPhoneNum;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class BookItineraryBillingAddress {

    private String Name;
    private String Address1;
    private String Address2;
    private String City;
    private String State;
    private String Country;
    private String ZipCode;
  }

}
