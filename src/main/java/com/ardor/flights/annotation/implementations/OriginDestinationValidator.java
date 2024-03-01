package com.ardor.flights.annotation.implementations;

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
import com.ardor.flights.annotation.OriginDestinationValidation;
import com.ardor.flights.model.search.FlightSearchRequest.OriginDestination;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Validates the list of origin and destination pairs in a flight search request. This class
 * implements the ConstraintValidator interface for OriginDestinationValidation. It logs debug
 * information when applying the validations to the provided list of OriginDestination objects.
 *
 * @author mkkumar
 */
@Slf4j
public class OriginDestinationValidator implements
    ConstraintValidator<OriginDestinationValidation, List<OriginDestination>> {

  /**
   * Validates the list of origin and destination pairs. This method is invoked to check the
   * validity of the provided list of OriginDestination objects.
   *
   * @param originDestinations         The list of origin and destination pairs to be validated.
   * @param constraintValidatorContext The context in which the constraint is evaluated.
   * @return Returns true if the list is valid; otherwise, false.
   */
  @Override
  public boolean isValid(List<OriginDestination> originDestinations,
      ConstraintValidatorContext constraintValidatorContext) {
    log.debug("Applying the validations to origin & destination  {}", originDestinations);
    // Origin - Destinations are same Validation check
    boolean isODValid = originDestinations.parallelStream()
        .noneMatch(obj -> obj.getFrom().equalsIgnoreCase(obj.getTo()));
    if (!isODValid) {
      constructconstraintValidation("Origin & Destination cannot be same","FSW0001",
          constraintValidatorContext);
    }
    // Multi-city Onward Date Validation
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_PATTERN);
    boolean isOriginDateValid = IntStream.range(0, originDestinations.size() - 1).allMatch(i ->
    {
      try
      {
        LocalDate onwardDate = LocalDate.parse(originDestinations.get(i).getOnwardDate(), formatter);
        LocalDate nextOnwardDate = LocalDate.parse(originDestinations.get(i + 1).getOnwardDate(), formatter);
        return onwardDate.isBefore(nextOnwardDate);
      }catch (Exception e)
      {
        constructconstraintValidation("Onward date must be in yyyy/MM/dd format","FSW0008", constraintValidatorContext);
        return false;
      }
    });
    if (!isOriginDateValid) constructconstraintValidation("Invalid Onward Date", "FSW0009", constraintValidatorContext);

    // return date validation check
    String returnDateStr = originDestinations.get(0).getReturnDate();
    boolean isReturnValid = true;
    if (!StringUtils.isEmpty(returnDateStr)) {
      log.debug("Applying the validations to return date {}", returnDateStr);
      Pattern pattern = Pattern.compile(Constants.DATE_PATTERN_REGEX);
      Matcher matcher = pattern.matcher(returnDateStr);
      if (!matcher.matches()) {
        constructconstraintValidation("Return date must be in yyyy/MM/dd format", "FSW0017", constraintValidatorContext);
        return false;
      }
      boolean isOnwardValid = true;
      //DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_PATTERN);
      LocalDate returnDate = LocalDate.parse(returnDateStr, formatter);
      try {
        LocalDate.parse(originDestinations.get(0).getOnwardDate(), formatter);
      } catch (Exception e) {
        isOnwardValid = false;
        constructconstraintValidation("Onward date must be in yyyy/MM/dd format.","FSW0008", constraintValidatorContext);
      }
      if(!isOnwardValid) {
        return false;
      }
      LocalDate onwardDate = LocalDate.parse(originDestinations.get(0).getOnwardDate(), formatter);
      LocalDate currentDate = LocalDate.now();
      isReturnValid = (returnDate.isAfter(currentDate) && returnDate.isAfter(onwardDate)
          && returnDate.isBefore(currentDate.plusDays(Constants.SEARCH_MAX_ALLOWED_DAYS)));
      if (!isReturnValid) {
        constructconstraintValidation("Invalid return date.","FSW0006", constraintValidatorContext);
      }
    }
    //CabinClass Validation
    boolean isCabinvalid = true;
    if(originDestinations.size()>1)
    {
      String cabinClass = originDestinations.get(0).getCabinClass();
      log.debug("Applying Validation to CabinClass {}",cabinClass);
      isCabinvalid = originDestinations.parallelStream().allMatch(obj -> cabinClass.equalsIgnoreCase(obj.getCabinClass()));
      if(!isCabinvalid)
        constructconstraintValidation("Invalid Cabin class.","FSW0023",constraintValidatorContext);

    }
    return isODValid && isReturnValid && isCabinvalid && isOriginDateValid;
  }
  private void constructconstraintValidation(String msg, String code, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(code+":"+msg).addConstraintViolation();
  }
}
