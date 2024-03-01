package com.ardor.flights.mapper;

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

import com.ardor.flights.model.booking.FlightBookingRequest.BillingAddress;
import com.ardor.flights.model.booking.FlightBookingRequest.ContactInfo;
import com.ardor.flights.model.booking.FlightBookingRequest.CreditCardDetails;
import com.ardor.flights.model.booking.FlightBookingRequest.PassengerDetails;
import com.ardor.flights.model.booking.FlightBookingResponse;
import com.ardor.flights.model.booking.FlightBookingResponse.BookingResponse;
import com.ardor.flights.supplier.mondee.model.booking.MondeeBookingRequest.BookItineraryBillingAddress;
import com.ardor.flights.supplier.mondee.model.booking.MondeeBookingRequest.BookItineraryCCDetails;
import com.ardor.flights.supplier.mondee.model.booking.MondeeBookingRequest.BookItineraryPaxContactInfo;
import com.ardor.flights.supplier.mondee.model.booking.MondeeBookingRequest.BookItineraryPaxDetail;
import com.ardor.flights.supplier.mondee.model.booking.MondeeBookingResponse;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface FlightBookingMapper {

  @Mappings({
      @Mapping(source = "paxType", target = "PaxType"),
      @Mapping(source = "gender", target = "Gender"),
      @Mapping(source = "userTitle", target = "UserTitle"),
      @Mapping(source = "firstName", target = "FirstName"),
      @Mapping(source = "middleName", target = "MiddleName"),
      @Mapping(source = "lastName", target = "LastName"),
      @Mapping(source = "dateOfBirth", target = "DateOfBirth"),
      @Mapping(source = "passportNumber", target = "PassportNumber"),
      @Mapping(source = "countryOfIssue", target = "CountryOfIssue"),
      @Mapping(source = "nationality", target = "Nationality"),
      @Mapping(source = "passportIssueDate", target = "PassportIssueDate"),
      @Mapping(source = "passportExpiryDate", target = "PassportExpiryDate")
  })
  BookItineraryPaxDetail mapPaxDetailsArdorToMondee(PassengerDetails passengerDetails);

  @Mappings({
      @Mapping(source = "phoneNumber", target = "PhoneNumber"),
      @Mapping(source = "alternatePhoneNumber", target = "AlternatePhoneNumber"),
      @Mapping(source = "email", target = "Email"),
  })
  BookItineraryPaxContactInfo mapContactInfoArdorToMondee(ContactInfo contactInfo);

  @Mappings({
      @Mapping(source = "name", target = "Name"),
      @Mapping(source = "address1", target = "Address1"),
      @Mapping(source = "address2", target = "Address2"),
      @Mapping(source = "city", target = "City"),
      @Mapping(source = "state", target = "State"),
      @Mapping(source = "country", target = "Country"),
      @Mapping(source = "zipCode", target = "ZipCode")
  })
  BookItineraryBillingAddress mapBillingAddresArdorToMondee(BillingAddress billingAddress);

  @Mappings({
      @Mapping(source = "cardType", target = "CardType"),
      @Mapping(source = "cardNumber", target = "CardNumber"),
      @Mapping(source = "cvv", target = "CVV"),
      @Mapping(source = "expiryDate", target = "ExpiryDate"),
      @Mapping(source = "bankPhoneNumber", target = "BankPhoneNum"),
      @Mapping(source = "billingPhoneNumber", target = "BillingPhoneNum"),
  })
  BookItineraryCCDetails mapCCDetailsArdorToMondee(CreditCardDetails ccDetails);


  default FlightBookingResponse mapResponseMondeeToArdor(
      MondeeBookingResponse mondeeBookingResponse) {
    return FlightBookingResponse.builder().bookingResponse(
        mondeeBookingResponse.getBookResponse().stream().map(this::mapBookingResponseMondeeToArdor)
            .collect(Collectors.toList())).build();
  }

  @Mappings({
      @Mapping(source = "PNR", target = "PNR"),
      @Mapping(source = "referenceNumber", target = "referenceNumber"),
      @Mapping(source = "bookingStatus", target = "status")
  })
  BookingResponse mapBookingResponseMondeeToArdor(
      MondeeBookingResponse.BookingResponse bookingResponse);
}
