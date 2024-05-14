package app.controllers;

import app.entities.Material;
import app.entities.Order;
import app.entities.OrderItem;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.services.Calculator;
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
        app.get("adminoverview", ctx -> orderOverviewAdmin(ctx, connectionPool));
        app.get("showPartsList", ctx -> showPartsList(ctx,connectionPool));
       // app.get("orderoverviewcustomer", ctx -> orderOverviewCustomer(ctx, connectionPool));
       // app.post("CreateOrder", ctx -> CreateOrder(ctx, connectionPool));
        app.get("designcarport", ctx -> showCarport(ctx));
    }


    public static void orderOverviewAdmin(Context ctx, ConnectionPool connectionPool)
    {
        try
        {
            List<Order> orderList = OrderMapper.getAllOrders(connectionPool);
            ctx.attribute("orderList", orderList);
            ctx.render("adminoverview.html");
        } catch (Exception e)
        {
            ctx.attribute("message", "Failed to get orders");
            e.printStackTrace();
            ctx.render("adminoverview.html");
        }
    }
/*
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
*/
    private static void showPartsList(Context ctx, ConnectionPool connectionPool)
    {
        int orderId = Integer.parseInt(ctx.formParam("orderIdChosen"));
        try
        {
            List<OrderItem> orderItemList = OrderMapper.getOrderItemsByOrderId(orderId,connectionPool);

            if(orderItemList.isEmpty())
            {
                ctx.render("showpartslist.html");
                return;
            }
         OrderItem orderItem = orderItemList.get(0);

            ctx.attribute("width", orderItem.getOrder().getCarportWidth());
            ctx.attribute("length", orderItem.getOrder().getCarportLength());
            ctx.attribute("orderItemList",orderItemList);
            ctx.render("showpartslist.html");

        }
        catch (DatabaseException e)
        {
            // måske også en numberformat exception??
            throw new RuntimeException();
        }
    }

private static void sendRequest(Context ctx, ConnectionPool connectionPool)
{

        int width = ctx.sessionAttribute("width");
        int length = ctx.sessionAttribute("length");
        Boolean installationFee = ctx.sessionAttribute("installationFee");
        int status = 1;
        int totalPrice = 19999;
        User user = ctx.sessionAttribute("CurrentUser");
        //User user = new User(100,"tester1","tester1","test",2600,87654321,"testemail","testpassword","customer");
        Order order = new Order(100, width, length,false, status, totalPrice, user);
    try
    {
        order = OrderMapper.insertOrder(order,connectionPool);

        //to do
        // calculate order items (parts list)

        Calculator calculator = new Calculator(width,length,connectionPool);
        calculator.calcCarport(order);



        // save parts in db


        // create message to customer and render order / send confirmation
        ctx.render("requestconfirmation.html");

    }
    catch (DatabaseException e)
    {
        throw new RuntimeException();

    }



}

/*
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
*/
    public static void showCarport(Context ctx) {
        Locale.setDefault(Locale.US);
        try {
            int length = Integer.parseInt(ctx.queryParam("length"));
            int width = Integer.parseInt(ctx.queryParam("width"));
            if (length < 200 || length > 900 || width < 200 || width > 900) {
                ctx.attribute("message", "Ugyldig længde eller bredde. Indtast venligst gyldige tal.");
                ctx.attribute("svg", "");
                ctx.render("designcarport.html");
                return;
            }
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
