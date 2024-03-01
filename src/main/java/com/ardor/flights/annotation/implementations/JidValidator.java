package com.ardor.flights.annotation.implementations;

import com.ardor.flights.annotation.JidValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class JidValidator implements ConstraintValidator<JidValidation, List<String>> {

  @Override
  public boolean isValid(List<String> value, ConstraintValidatorContext context) {
    return value.stream().allMatch(s -> s != null && !s.isEmpty() && !s.isBlank());
  }

}
