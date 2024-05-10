package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import app.util.Caps;
import app.util.Validation;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UserController
{

    public static void addRoutes(Javalin app, ConnectionPool connectionPool)
    {
        app.post("login", ctx -> login(ctx, connectionPool));
        app.get("logout", ctx -> logout(ctx));
        app.post("createuser", ctx -> createUser(ctx, connectionPool));
        app.get("backtoindex", ctx -> ctx.render("index.html"));
        app.get("loginpage", ctx -> ctx.render("loginpage.html"));
        app.get("createuserpage", ctx -> ctx.render("createuserpage.html"));
        app.get("backtologin", ctx -> ctx.render("loginpage.html"));
        app.get("adminoverview", ctx -> ctx.render("adminoverview.html"));
        app.get("customeroverview", ctx -> ctx.render("customeroverview.html"));

    }

    public static void login(Context ctx, ConnectionPool connectionPool)
    {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        try
        {
            User user = UserMapper.login(email, password, connectionPool);
            if (user != null)
            {
                ctx.sessionAttribute("currentUser", user);
                ctx.sessionAttribute("userEmail", email);
                ctx.sessionAttribute("userRole", user.getRole());

                ctx.render("index.html");
            } else
            {
                ctx.attribute("message", "Noget er gået galt");
                ctx.render("loginpage.html");
            }
        } catch (DatabaseException e)
        {
            // Handle database error
            ctx.attribute("message", "forkert email eller password");
            ctx.render("loginpage.html");
        }
    }

    public static void logout(Context ctx)
    {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    private static void createUser(Context ctx, ConnectionPool connectionPool)
    {
        Map<String, String> errorMessages = new HashMap<>();

        String firstName = null;
        String lastName = null;
        String address = null;
        int zipCode = 0;
        int phoneNumber = 0;
        String email = null;
        String password1 = null;
        String password2 = null;

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

            {
                //CRITERIA TO FIRSTNAME
                if (!Validation.validateLetterOnly(firstName))
                {
                    errorMessages.put("firstnamemsg", "Dit fornavn må ikke indholde tal eller symboler, udover '-'");
                }
                //CRITERIA TO LASTNAME
                if (!Validation.validateLetterOnly(lastName))
                {
                    errorMessages.put("lastnamemsg", "Dit efternavn må ikke indholde tal eller symboler, udover '-'");
                }
                //CRITERIA TO ADDRESS
                if (!Validation.validateLetterAndSelectSymbolsOnly(address))
                {
                    errorMessages.put("addressmsg", "Din addresse må ikke indholde symboler, udover '. -'");
                }
                //CRITERIA TO ZIP CODE
                if (!Validation.validateFourNumbersOnly(zipCode))
                {
                    errorMessages.put("zipcodemsg", "Dit postnummer må kun indeholde 4 tal");
                }
                //CRITERIA FOR PHONE NUMBERS
                if (!Validation.validateEightNumbersOnly(phoneNumber))
                {
                    errorMessages.put("phonenumbermsg", "Dit telefon nummer må kun indeholde 8 tal");
                }
                // email must have a @
                if (!email.contains("@"))
                {
                   errorMessages.put("emailmsg", "Din email skal indeholde '@'! Prøv igen");
                }

               if(UserMapper.emailExists(email, connectionPool))
               {
                   errorMessages.put("emailmsg", "Email er i brug");
               }
               // both passwords must match
                if (!password1.equals(password2))
                {
                    errorMessages.put("passwordmsg", "Dine to passwords matcher ikke! Prøv igen");
                }
                // password must consist standard letter
                if (!Pattern.matches(".*[\\p{Lu}\\p{N}æøåÆØÅ].*", password1))
                {
                    errorMessages.put("passwordmsg", "Password skal bestå af standard bogstaver");
                }
                //password must be 4 letters long
                if (!(password1.length() >= 4))
                {
                    errorMessages.put("passwordmsg", "Password skal mindst være 4 bogstaver langt");
                }
                if(!errorMessages.isEmpty())
                {
                    ctx.attribute("errormessages", errorMessages);
                    ctx.render("createuserpage.html");
                    return;
                }
            }

        } catch (NumberFormatException n)
        {
            User guest = new User(firstName, lastName, address, zipCode, phoneNumber, email);
            ctx.sessionAttribute("currentCreateUser", guest);
            ctx.attribute("message", "Alle felter skal være udført");
            ctx.render("createuserpage.html");

        } catch (Exception e)
        {
            ctx.attribute("message", "noget gik galt prøve igen");
            ctx.render("createuserpage.html");
        }

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
                ctx.attribute("message", "Alle felter skal være udfyldt");
                ctx.render("createuserpage.html");
            }
        }
    }
}

