package app.util;

public class Caps
{
    public static String firstLetterToUppercase(String text){

        String firstLetter = text.substring(0, 1).toUpperCase();
        String restOfText = text.substring(1);
        text = firstLetter + restOfText;

        return text;


    }
}
