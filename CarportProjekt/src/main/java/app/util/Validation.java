package app.util;

public class Validation
{
    public static boolean validateLetterOnly(String text)
    {
        return text.matches("^[a-zA-ZæøåÆØÅ]+$");
    }
    public static boolean validateLetterAndSelectSymbolsOnly(String text)
    {
        return text.matches("^[\\p{N}a-zA-ZæøåÆØÅ.\\s-]+$");
    }
    public static boolean validateFourNumbersOnly(int number)
    {
        String numberString = String.valueOf(number);
        return numberString.length() == 4 && numberString.matches("^[0-9]+$");
    }

    public static boolean validateEightNumbersOnly(int number)
    {
        String numberString = String.valueOf(number);
        return  numberString.length() == 8 && numberString.matches("^[0-9]+$");
    }

    public static boolean validateEmailContainsAtSymbol(String text)
    {
        return text.contains("@");
    }

    public static boolean validateEmailContainsAtLetters(String text)
    {
        return text.matches(".*[a-zA-Z]+.*");
    }
    public static boolean validateEqualPasswords(String text1, String text2)
    {
        return text1.equals(text2);
    }

    public static boolean validateOneUppercaseLetterPassword(String text)
    {
        return text.matches(".*[A-Z].*");
    }

    public static boolean validateLengthOfPassword(String text)
    {
        return text.length() >=4;
    }

    public static boolean validatePasswordContainsNumber(String text)
    {
        return text.matches(".*\\d.*");
    }

    public static boolean validatePasswordContainsSign(String text)
    {
        return text.matches(".*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?`~].*") ;
    }
}
