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

import com.ardor.flights.annotation.DateValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateValidator implements ConstraintValidator<DateValidation, String> {

  private int days;
  private String pattern;

  @Override
  public void initialize(DateValidation constraintAnnotation) {
    this.days = constraintAnnotation.days();
    this.pattern = constraintAnnotation.pattern();
  }

  @Override
  public boolean isValid(String strDate, ConstraintValidatorContext constraintValidatorContext) {
    log.debug("Applying the validation to onward date {}", strDate);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(this.pattern);
    LocalDate inputDate = LocalDate.parse(strDate, formatter);
    LocalDate currentDate = LocalDate.now();
    return inputDate.isAfter(currentDate) && inputDate.isBefore(currentDate.plusDays(this.days));
  }
}
