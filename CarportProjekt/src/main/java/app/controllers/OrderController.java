package app.controllers;

import app.entities.Material;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
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
        app.post("CreateOrder", ctx -> CreateOrder(ctx, connectionPool));
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


    public static void CreateOrder(Context ctx, ConnectionPool connectionPool)
    {
        User currentUser = ctx.sessionAttribute("currentUser");
        try
        {
            Order newOrder = OrderMapper.createOrder(currentUser.getUserId(), connectionPool);

            addOrderItems(ctx, newOrder.getOrderId(), connectionPool);


            List<?> materialItemList = new ArrayList<>();
            ctx.sessionAttribute("MaterialItemList", materialItemList);

            ctx.attribute("orderId", newOrder.getOrderId());

            int totalPrice = calculateTotalPrice(ctx);
            System.out.print("Total price: " + totalPrice);

            //Sæt til den rigtige side
            ctx.render("confirmation.html");
        } catch (DatabaseException | SQLException e)
        {
            e.printStackTrace();
            ctx.attribute("message", "Fejl ved opdatering af saldo eller oprettelse af ordre.");
            //Sæt til den rigtige side
            ctx.render("shoppingcart.html");
        }
    }

    public static void addOrderItems(Context ctx, int orderId, ConnectionPool connectionPool) throws SQLException
    {
        List<Material> materialslist = ctx.sessionAttribute("Materialslist");

        if (materialslist != null)
        {
            for (Material material : materialslist)
            {
                OrderMapper.createOrderItem(connectionPool, orderId, material.getDescription(), material.getMaterialId());
            }
        }
    }

    public static int calculateTotalPrice(Context ctx)
    {
        List<Material> materialsList = ctx.sessionAttribute("materialsList");
        if (materialsList == null)
        {
            return 0;
        }
        int totalPrice = 0;
        for (Material material : materialsList)
        {
            int materialsListTotal = material.getQuantity() * material.getPrice();
            totalPrice += materialsListTotal;
        }
        return totalPrice;
    }
}
