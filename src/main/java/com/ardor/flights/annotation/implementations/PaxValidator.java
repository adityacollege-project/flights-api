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

import com.ardor.flights.annotation.PaxValidation;
import com.ardor.flights.model.search.FlightSearchRequest.PaxDetails;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/**
 * PaxValidator is an implementation of the ConstraintValidator interface for the PaxValidation
 * annotation. It performs custom validation logic on PaxDetails objects used in
 * FlightSearchRequest. This validator checks if the total number of passengers is within the
 * allowed limit and if the number of adults is greater than or equal to the number of infants.
 *
 * @author mkkumar
 */
@Slf4j
public class PaxValidator implements ConstraintValidator<PaxValidation, PaxDetails> {

  /**
   * Validates the provided PaxDetails object based on custom business logic.
   *
   * @param paxDetails The PaxDetails object to be validated.
   * @param ctxt       The context in which the constraint is evaluated.
   * @return true if the PaxDetails object is valid; otherwise, false.
   */
  @Override
  public boolean isValid(PaxDetails paxDetails, ConstraintValidatorContext ctxt) {
    log.debug("Applying the Validations to pax {}", paxDetails);
    Integer totalPassengers =
        paxDetails.getAdultCount() + paxDetails.getChildCount() + paxDetails.getInfantCount();
    return (totalPassengers <= 9 && paxDetails.getAdultCount() >= paxDetails.getInfantCount());
  }
}
