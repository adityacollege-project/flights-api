package com.ardor.flights.annotation;

import com.ardor.flights.annotation.implementations.JidValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Constraint(validatedBy = JidValidator.class)
public @interface JidValidation {
  String message() default "";

  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
