package app.controllers;

import app.entities.Order;
import app.entities.OrderItem;
import app.entities.User;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderController
{
    public static void addRoutes(Javalin app, ConnectionPool connectionPool)
    {
        app.get("orderoverviewadmin", ctx -> orderOverviewAdmin(ctx, connectionPool));
        app.get("orderoverviewcustomer", ctx -> orderOverviewCustomer(ctx, connectionPool));

    }


    public static void orderOverviewAdmin(Context ctx, ConnectionPool connectionPool)
    {
        try
        {
            List<Order> orderList = OrderMapper.loadOrdersForAdmin(connectionPool);
            ctx.attribute("orders", orderList);
            ctx.render("adminoverview.html");
        } catch (Exception e)
        {
            ctx.attribute("message", "Failed to get orders");
            e.printStackTrace();
            ctx.render("adminoverview.html");
        }
    }

    public static void orderOverviewCustomer(Context ctx, ConnectionPool connectionPool)
    {
        User user = ctx.sessionAttribute("currentUser");

        try
        {
            int userId = user.getUserId();
            List<Order> orderListCustommer = OrderMapper.loadOrdersForCustomer(connectionPool, userId);
            ctx.attribute("customerorders", orderListCustommer);
            ctx.render("customeroverview.html");
        } catch (Exception e)
        {
            ctx.attribute("message", "Failed to get orders");
            e.printStackTrace();
            ctx.render("custommeroverview.html");
        }
    }



}
