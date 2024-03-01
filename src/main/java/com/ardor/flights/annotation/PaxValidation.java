package com.ardor.flights.annotation;

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

import com.ardor.flights.annotation.implementations.PaxValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * PaxValidation is a custom annotation used for validating passenger details in a flight search
 * request. It ensures that the provided passenger details meet certain criteria using the
 * PaxValidator class. The annotation can be applied to fields, parameters, or types (classes).
 *
 * @author mkkumar
 */
@Documented
@Constraint(validatedBy = PaxValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PaxValidation {

  /**
   * Defines the default error message that will be used when the validation fails.
   *
   * @return The default error message.
   */
  public String message() default "Invalid pax details.";

  /**
   * Groups to which this constraint belongs.
   *
   * @return An array of group classes.
   */
  public Class<?>[] groups() default {};

  /**
   * Payloads that can be attached to the constraint.
   *
   * @return An array of payload classes.
   */
  public Class<? extends Payload>[] payload() default {};
}
