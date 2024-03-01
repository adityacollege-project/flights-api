package com.ardor.flights.annotation.implementations;

import com.ardor.flights.annotation.ValidExpiryDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class ExpiryDateValidator implements ConstraintValidator<ValidExpiryDate, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yyyy");
        simpleDateFormat.setLenient(false);
        Date expiry = null;
        try {
            expiry = simpleDateFormat.parse(value);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return expiry.after(new Date());

    }
}
