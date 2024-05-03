package app.controllers;

import app.persistence.ConnectionPool;
import io.javalin.Javalin;

public class UserController
{

    public static void addRoutes(Javalin app, ConnectionPool connectionPool)
    {
        app.post("login", ctx -> login(ctx, connectionPool));
        app.get("logout", ctx -> logout(ctx));
        app.post("createuser", ctx -> login(ctx, connectionPool));
        app.get("homepage", ctx -> ctx.render("homepage.html"));

    }



}
