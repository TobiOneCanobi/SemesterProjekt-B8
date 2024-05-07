package app.util;

public class Validation
{
    public static boolean validateLetterOnly(String text){

        return text.matches("^[a-zA-ZæøåÆØÅ]+$");

    }
}
