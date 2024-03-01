package com.ardor.flights.annotation;

import com.ardor.flights.annotation.implementations.DOBValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DOBValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
public @interface DOBValidation {

    public String message() default "Date of Birth should be in the past";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};

}
