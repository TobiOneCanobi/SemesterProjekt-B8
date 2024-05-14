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
            {
                // firstname must have letters only
                if (!Validation.validateLetterOnly(firstName))
                {
                    errorMessages.put("firstnamemsg", "Dit fornavn må ikke indeholde tal eller symboler, udover [-])");
                }
                // lastname must have letters only
                if (!Validation.validateLetterOnly(lastName))
                {
                    errorMessages.put("lastnamemsg", "Dit efternavn må ikke indeholde tal eller symboler, udover [-]");
                }
                // address must have letters and numbers
                if (!Validation.validateTextContainsLetterAndNumber(address))
                {
                    if(errorMessages.containsKey("addressmsg"))
                    {
                        errorMessages.put("addressmsg", errorMessages.get("addressmsg") + "Din adresse skal indeholde bogstaver og tal. ");
                    }else
                    {
                        errorMessages.put("addressmsg", "Din adresse skal indeholde bogstaver og tal. ");
                    }

                }
                // address must have a whitespace between letters and numbers
                if(!Validation.validateTextContainsWhitespaceBetweenLetterAndNumber(address))
                {
                    if(errorMessages.containsKey("addressmsg"))
                    {
                        errorMessages.put("addressmsg", errorMessages.get("addressmsg") + "Angiv venligst mellemrum mellem vejnavn og husnummer ");
                    }else
                    {
                        errorMessages.put("addressmsg", "Angiv venligst mellemrum mellem vejnavn og husnummer ");
                    }

                }
                // zip code must only contain 4 numbers
                if (!Validation.validateFourNumbersOnly(zipCode))
                {
                    errorMessages.put("zipcodemsg", "Dit postnummer må kun indeholde 4 tal");
                }
                // phone number must only contain 8 numbers
                if (!Validation.validateEightNumbersOnly(phoneNumber))
                {
                    errorMessages.put("phonenumbermsg", "Dit telefon nummer må kun indeholde 8 tal");
                }
                // email must have a @ and letters
                if (!Validation.validateTextContainsAtAndLetter(email))
                {
                    errorMessages.put("emailmsg", "Din email skal indeholde '@' og bogstaver");
                }
                // email already in use
                if (UserMapper.emailExists(email, connectionPool))
                {
                    errorMessages.put("emailmsg", "Email er i brug");
                }
                // both passwords must match
                if (!Validation.validateEqualPasswords(password1, password2))
                {
                    if (errorMessages.containsKey("passwordmsg"))
                    {
                        errorMessages.put("passwordmsg", errorMessages.get("passwordmsg") + "Dine to passwords matchter ikke! ");
                    } else
                    {
                        errorMessages.put("passwordmsg", "Dine to passwords matcher ikke! ");
                    }
                }
                // password must consist one uppercase letter
                if (!Validation.validateOneUppercaseLetterPassword(password1))
                {
                    if (errorMessages.containsKey("passwordmsg"))
                    {
                        errorMessages.put("passwordmsg", errorMessages.get("passwordmsg") + "Password skal bestå af mindst et stort bogstav. ");
                    } else
                    {
                        errorMessages.put("passwordmsg", "Password skal bestå af mindst et stort bogstav. ");
                    }
                }
                // password must be minimum 4 letters long
                if (!Validation.validateLengthOfPassword(password1))
                {
                    if (errorMessages.containsKey("passwordmsg"))
                    {
                        errorMessages.put("passwordmsg", errorMessages.get("passwordmsg") + "Password skal mindst være 4 bogstaver langt. ");
                    } else
                    {
                        errorMessages.put("passwordmsg", "Password skal mindst være 4 bogstaver langt. ");
                    }
                }
                // password must minumum contain 1 number
                if (!Validation.validatePasswordContainsNumber(password1))
                {
                    if (errorMessages.containsKey("passwordmsg"))
                    {
                        errorMessages.put("passwordmsg", errorMessages.get("passwordmsg") + "Password skal indeholde mindst et tal. ");
                    } else
                    {
                        errorMessages.put("passwordmsg", "Password skal indeholde mindst et tal. ");
                    }
                }
                // password must minimum contain 1 sign
                if (!Validation.validatePasswordContainsSign(password1))
                {
                    if (errorMessages.containsKey("passwordmsg"))
                    {
                        errorMessages.put("passwordmsg", errorMessages.get("passwordmsg") + "Password skal mindst indeholde et tegn");
                    } else
                    {
                        errorMessages.put("passwordmsg", "Password skal mindst indeholde et tegn");
                    }
                }
                if (!errorMessages.isEmpty())
                {
                    ctx.attribute("errormessages", errorMessages);
                    ctx.render("createuserpage.html");
                    return;
                }
                firstName = Caps.firstLetterToUppercase(firstName);
                lastName = Caps.firstLetterToUppercase(lastName);
                address = Caps.firstLetterToUppercase(address);
            }

        } catch (NumberFormatException n)
        {
            User guest = new User(firstName, lastName, address, zipCode, phoneNumber, email);
            errorMessages.put("validinfomsg", "Alle felter skal være udfyldt");
            ctx.sessionAttribute("currentCreateUser", guest);
            ctx.attribute("errormessages", errorMessages);
            ctx.render("createuserpage.html");

        } catch (Exception e)
        {
            errorMessages.put("chaosmsg", "Panik, alt gik galt");
            ctx.attribute("errormessages", errorMessages);
            ctx.render("createuserpage.html");
        }
        if (errorMessages.isEmpty())
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
                ctx.attribute("message", "Noget fik galt! Prøv igen. ");
                ctx.render("createuserpage.html");
            }
        }
    }
}

