package app.util;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
class ValidationTest {

    @Test
    void validateLetterOnly()
    {
        assertTrue(Validation.validateLetterOnly("adsfkjdasf"));
        assertFalse(Validation.validateLetterOnly("hansi5"));
        assertTrue(Validation.validateLetterOnly("adsfkjdasfÅ"));

    }
}