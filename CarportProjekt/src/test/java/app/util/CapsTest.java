package app.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapsTest
{

    @Test
    void firstLetterToUppercase()
    {
        String expected = "Jimmy";
        String actual = "jimmy";
        String result = Caps.firstLetterToUppercase(actual);

        assertEquals(expected, result);
    }
}