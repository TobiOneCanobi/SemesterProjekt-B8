package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

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

                ctx.redirect("/index");
            } else
            {
                ctx.attribute("message", "noget er gået galt");
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
        // Get form parameters

        String firstName = ctx.formParam("firstname");
        String lastName = ctx.formParam("lastname");
        String address = ctx.formParam("address");
        int zipCode = Integer.parseInt(ctx.formParam("zipcode"));
        String city = ctx.formParam("city");
        int phoneNumber = Integer.parseInt(ctx.formParam("phonenumber"));
        String email = ctx.formParam("email");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");
        if (!email.contains("@"))
        {
            ctx.attribute("message", "Din email skal indeholde '@'! Prøv igen.");
            ctx.render("createuser.html");
        } else if (!password1.equals(password2))
        {
            ctx.attribute("message", "Dine to passwords matcher ikke! Prøv igen");
            ctx.render("createuser.html");
        } else if (!Pattern.matches(".*[\\p{Lu}\\p{N}æøåÆØÅ].*",password1) || password1.length() < 4)
        {
            ctx.attribute("message", " Kun bogstaver og tal, skal mindst være 4 bogstaver langt");
            ctx.render("createuser.html");

        } else if (password1.equals(password2))
        {
            try
            {
                UserMapper.createUser(firstName, lastName, address, zipCode, city, phoneNumber, email, password1, "customer", connectionPool);
                ctx.attribute("message", "Du er hermed oprettet med email: " + email +
                        ". Nu kan du logge på.");
                ctx.render("loginpage.html");
            } catch (DatabaseException e)
            {
                ctx.attribute("message", "Dit email er allerede i brug. Prøv igen, eller log ind");
                ctx.render("createuser.html");
            }
        } else
        {
            ctx.attribute("Noget gik galt - Udfyld det forfra");
            ctx.render("createuser.html");
        }
    }


}
