package app.util;

public class Validation
{
    public static boolean validateLetterOnly(String text){

        return text.matches("^[a-zA-ZæøåÆØÅ]+$");

    }

    public static boolean validateLetterAndSelectSymbolsOnly(String text){

        return text.matches("^[\\p{N}a-zA-ZæøåÆØÅ.\\s-]+$");

    }

    public static boolean validateFourNumbersOnly(int number){

        String numberString = String.valueOf(number);

        if(numberString.length() != 4)
        {
            return false;
        }

        return numberString.matches("^[\\p{N}]+$");

    }

    public static boolean validateEightNumbersOnly(int number){

        String numberString = String.valueOf(number);

        if(numberString.length() != 8)
        {
            return false;
        }

        return numberString.matches("^[\\p{N}]+$");

    }

}
