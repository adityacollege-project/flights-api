package com.ardor.flights.annotation;

import com.ardor.flights.annotation.implementations.LuhnValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LuhnValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface LuhnValid {

    String message() default "Invalid credit card number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
