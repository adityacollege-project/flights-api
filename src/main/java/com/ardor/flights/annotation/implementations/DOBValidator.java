package com.ardor.flights.annotation.implementations;

import com.ardor.flights.annotation.DOBValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DOBValidator implements ConstraintValidator<DOBValidation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate inputDate = LocalDate.parse(value, formatter);
            LocalDate currentDate = LocalDate.now();
            return inputDate.isBefore(currentDate);
    }
}
