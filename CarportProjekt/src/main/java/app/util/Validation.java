package app.util;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.http.Context;

import java.util.regex.Pattern;

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

    public static void createUserWithValidation(Context ctx, ConnectionPool connectionPool)
    {

        String firstName = null;
        String lastName = null;
        String address = null;
        int zipCode = 0;
        int phoneNumber = 0;
        String email = null;
        String password1 = null;
        String password2 = null;
        boolean inputWrong = false;
        try
        {
            firstName = ctx.formParam("firstname");
            lastName = ctx.formParam("lastname");
            address = ctx.formParam("address");
            String zipCodeString = ctx.formParam("zipcode");
            String phoneNumberString = ctx.formParam("phonenumber");
            email = ctx.formParam("email");
            password1 = ctx.formParam("password1");
            password2 = ctx.formParam("password2");

            zipCode = Integer.parseInt(zipCodeString);
            phoneNumber = Integer.parseInt(phoneNumberString);

            User guest = new User(firstName, lastName, address, zipCode, phoneNumber, email);
            ctx.sessionAttribute("currentCreateUser", guest);

            firstName = Caps.firstLetterToUppercase(firstName);
            lastName = Caps.firstLetterToUppercase(lastName);
            address = Caps.firstLetterToUppercase(address);


            if (!inputWrong)
            {
                //CRITERIA TO FIRSTNAME
                if (!Validation.validateLetterOnly(firstName))
                {
                    ctx.attribute("message", "Dit fornavn må ikke indholde tal eller symboler, udover '-'");
                    ctx.render("createuserpage.html");
                    return;
                }
                //CRITERIA TO LASTNAME
                if (!Validation.validateLetterOnly(lastName))
                {
                    ctx.attribute("message", "Dit efternavn må ikke indholde tal eller symboler, udover '-'");
                    ctx.render("createuserpage.html");
                    return;
                }
                //CRITERIA TO ADDRESS
                if (!Validation.validateLetterAndSelectSymbolsOnly(address))
                {
                    ctx.attribute("message", "Din addresse må ikke indholde symboler, udover '. -'");
                    ctx.render("createuserpage.html");
                    return;
                }
                //CRITERIA TO ZIP CODE

                if (!Validation.validateFourNumbersOnly(zipCode))
                {
                    ctx.attribute("message", "Dit postnummer må kun indeholde 4 tal");
                    ctx.render("createuserpage.html");
                    return;
                }

                //CRITERIA FOR PHONE NUMBERS
                if (!Validation.validateEightNumbersOnly(phoneNumber))
                {
                    ctx.attribute("message", "Dit telefon nummer må kun indeholde 8 tal");
                    ctx.render("createuserpage.html");
                    return;
                }

                // email must have a @
                if (!email.contains("@"))
                {
                    ctx.attribute("message", "Din email skal indeholde '@'! Prøv igen.");
                    ctx.render("createuserpage.html");
                    return;
                }

                // both passwords must match
                if (!password1.equals(password2))
                {
                    ctx.attribute("message", "Dine to passwords matcher ikke! Prøv igen");
                    ctx.render("createuserpage.html");
                    return;
                }

                // password must consist standard letter
                if (!Pattern.matches(".*[\\p{Lu}\\p{N}æøåÆØÅ].*", password1))
                {
                    ctx.attribute("message", " Password skal bestå af standard bogstaver");
                    ctx.render("createuserpage.html");
                    inputWrong = true;
                }

                //password must be 4 letters long
                if (!(password1.length() >= 4))
                {
                    ctx.attribute("message", " Password skal mindst være 4 bogstaver langt");
                    ctx.render("createuserpage.html");
                    inputWrong = true;
                }
            }

        } catch (NumberFormatException n)
        {
            User guest = new User(firstName, lastName, address, zipCode, phoneNumber, email);
            ctx.sessionAttribute("currentCreateUser", guest);
            ctx.attribute("message", "Alle felter skal være udfyldt");
            ctx.render("createuserpage.html");
            inputWrong = true;
        } catch (Exception e)
        {
            ctx.attribute("message", "noget gik galt prøve igen");
            ctx.render("createuserpage.html");
            inputWrong = true;
        }

        if (!inputWrong)
        {
            try
            {
                UserMapper.createUser(firstName, lastName, address, zipCode, phoneNumber, email, password1, "customer", connectionPool);
                ctx.attribute("message", "Du er hermed oprettet med email: " + email +
                        ". Nu kan du logge på.");
                ctx.req().getSession().invalidate();
                ctx.render("loginpage.html");
            } catch (DatabaseException e)
            {
                ctx.attribute("message", "Din email er allerede i brug. Prøv igen, eller log ind");
                ctx.render("createuserpage.html");
            }
        }
    }

    }


