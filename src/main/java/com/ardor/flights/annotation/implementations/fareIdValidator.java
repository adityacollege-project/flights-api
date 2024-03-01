package com.ardor.flights.annotation.implementations;

import com.ardor.flights.annotation.fareIdValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Map;

public class fareIdValidator implements ConstraintValidator<fareIdValidation, Map<String,String>> {

  @Override
  public boolean isValid(Map<String, String> value, ConstraintValidatorContext context) {
    return value.entrySet().stream().allMatch(obj -> isValid(obj.getKey()) && isValid(obj.getValue()));
  }
  private boolean isValid(String data)
  {
    return data!=null && !data.isBlank() && !data.isEmpty();
  }

  private void constructconstraintValidation(String msg, String code, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(code+":"+msg).addConstraintViolation();
  }
}
