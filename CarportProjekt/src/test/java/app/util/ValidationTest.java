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
        assertTrue(Validation.validateFourNumbersOnly(3436));
        assertFalse(Validation.validateFourNumbersOnly(444));

    }
}