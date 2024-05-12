package app.controllers;

import app.entities.Material;
import app.entities.Order;
import app.entities.OrderItem;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.services.CarportSvg;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderController
{
    public static void addRoutes(Javalin app, ConnectionPool connectionPool)
    {
        app.get("orderoverviewadmin", ctx -> orderOverviewAdmin(ctx, connectionPool));
        app.get("orderoverviewcustomer", ctx -> orderOverviewCustomer(ctx, connectionPool));
        app.post("CreateOrder", ctx -> CreateOrder(ctx, connectionPool));
        app.get("designcarport", ctx -> showCarport(ctx));
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
        //Material list eller orderitem list?
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
        List<OrderItem> orderItemList = ctx.sessionAttribute("orderItemList");
        if (orderItemList == null)
        {
            return 0;
        }
        int totalPrice = 0;
        for (OrderItem orderItem : orderItemList)
        {
            int orderItemsListTotal = orderItem.getMaterial().getQuantity() * orderItem.getMaterial().getPrice();
            totalPrice += orderItemsListTotal;
        }
        return totalPrice;
    }

    public static void showCarport(Context ctx) {
        Locale.setDefault(Locale.US);
        try {
            int length = Integer.parseInt(ctx.queryParam("length"));
            int width = Integer.parseInt(ctx.queryParam("width"));
            CarportSvg svg = new CarportSvg(width,length);
            ctx.attribute("svg", svg.toString());
        } catch (NumberFormatException e) {
            ctx.attribute("message", "Ugyldig længde eller bredde. Indtast venligst gyldige tal.");
            ctx.attribute("svg", "");
        }
        // Render the HTML template
        ctx.render("designcarport.html");
    }
}
