package app.util;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
class ValidationTest {

    @Test
    void validateLetterOnly()
    {
        assertTrue(Validation.validateLetterOnly("Jørgen"));
        assertFalse(Validation.validateLetterOnly("Hans5"));


    }

    @Test
    void validateLetterAndSelectSymbolsOnly()
    {
        assertTrue(Validation.validateLetterAndSelectSymbolsOnly("lyngby-vej 50"));
        assertFalse(Validation.validateLetterAndSelectSymbolsOnly("Virumgade 25%"));
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
    void validateEmailContains()
    {
        assertTrue(Validation.validateEmailContainsAtSymbol("@"));
        assertFalse(Validation.validateEmailContainsAtSymbol("mas"));
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
    void validateEmailContainsAtLetters()
    {
        assertTrue(Validation.validateEmailContainsAtLetters("hans"));
        assertFalse(Validation.validateEmailContainsAtLetters("33"));
    }
}