package com.ardor.flights.annotation;

import com.ardor.flights.annotation.implementations.fareIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = fareIdValidator.class)
public @interface fareIdValidation
{
  public String message();
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
