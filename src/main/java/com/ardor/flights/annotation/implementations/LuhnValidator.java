package com.ardor.flights.annotation.implementations;

import com.ardor.flights.annotation.LuhnValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LuhnValidator implements ConstraintValidator<LuhnValid, String> {

    @Override
    public boolean isValid(String cardNumber, ConstraintValidatorContext context) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return false; // Empty or null strings are considered invalid
        }

        // Remove all non-digit characters
        cardNumber = cardNumber.replaceAll("[^0-9]", "");

        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = digit % 10 + 1;
                }
            }
            sum += digit;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }
}