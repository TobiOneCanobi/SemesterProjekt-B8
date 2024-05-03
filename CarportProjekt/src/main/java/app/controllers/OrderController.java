package app.controllers;

import app.entities.Order;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class OrderController
{
    public static void addRoutes(Javalin app, ConnectionPool connectionPool)
    {
        app.get("orderoverviewadmin", ctx -> orderOverviewAdmin(ctx, connectionPool));

    }



    public static void orderOverviewAdmin(Context ctx, ConnectionPool connectionPool)
    {
        try
        {
            List<Order> orderList = OrderMapper.loadOrdersForAdmin(connectionPool);
            ctx.attribute("orders", orderList);
            //Ændre til den rigtige html fil
            ctx.render("adminoverview.html");
        } catch (Exception e)
        {
            ctx.attribute("message", "Failed to get orders");
            e.printStackTrace();
            //Ændre til den rigtige html fil
            ctx.render("adminoverview.html");
        }
    }





}
