package app.util;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class ValidationTest
{
    @Test
    void validateLetterOnly()
    {
        assertTrue(Validation.validateLetterOnly("Jørgen"));
        assertFalse(Validation.validateLetterOnly("Hans5"));
    }

    @Test
    void validateFourNumbersOnly()
    {
        assertTrue(Validation.validateFourNumbersOnly(3435));
        assertFalse(Validation.validateFourNumbersOnly(444));
    }

    @Test
    void validateEightNumbersOnly()
    {
        assertTrue(Validation.validateEightNumbersOnly(12345678));
        assertFalse(Validation.validateEightNumbersOnly(5555));
    }

    @Test
    void validateEqualPasswords()
    {
        assertTrue(Validation.validateEqualPasswords("mas12", "mas12"));
        assertFalse(Validation.validateEqualPasswords("mas2", "mas1"));
    }

    @Test
    void validateOneUppercaseLetterPassword()
    {
        assertTrue(Validation.validateOneUppercaseLetterPassword("hAns"));
        assertFalse(Validation.validateOneUppercaseLetterPassword("hans"));
    }

    @Test
    void validateLengthOfPassword()
    {
        assertTrue(Validation.validateLengthOfPassword("mk14"));
        assertFalse(Validation.validateLengthOfPassword("mk4"));
    }

    @Test
    void validatePasswordContainsNumber()
    {
        assertTrue(Validation.validatePasswordContainsNumber("mas1"));
        assertFalse(Validation.validatePasswordContainsNumber("mas"));
    }

    @Test
    void validatePasswordContainsSign()
    {
        assertTrue(Validation.validatePasswordContainsSign("mas!?%"));
        assertFalse(Validation.validatePasswordContainsSign("mas"));
    }

    @Test
    void validateTextContainsLetterAndNumber()
    {
        assertTrue(Validation.validateTextContainsLetterAndNumber("stenvej 20"));
        assertFalse(Validation.validateTextContainsLetterAndNumber("stenvej"));
    }

    @Test
    void validateTextContainsAtAndLetter()
    {
        assertTrue(Validation.validateTextContainsAtAndLetter("mas@"));
        assertFalse(Validation.validateTextContainsAtAndLetter("mas"));
    }

    @Test
    void validateTextContainsWhitespaceBetweenLetterAndNumber()
    {
        assertTrue(Validation.validateTextContainsWhitespaceBetweenLetterAndNumber("stenvej 20"));
        assertFalse(Validation.validateTextContainsWhitespaceBetweenLetterAndNumber("stenvej20"));
    }
}