package com.ardor.flights.annotation;

import com.ardor.flights.annotation.implementations.ExpiryDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExpiryDateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidExpiryDate {

    String message() default "Invalid expiry date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
