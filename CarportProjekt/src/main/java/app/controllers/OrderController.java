package app.controllers;

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

import java.util.List;
import java.util.Locale;

public class OrderController
{

    public static void addRoutes(Javalin app, ConnectionPool connectionPool)
    {
        app.get("adminoverview", ctx -> orderOverviewAdmin(ctx, connectionPool));
        app.post("showPartsList", ctx -> showPartsList(ctx, connectionPool));
         app.get("orderoverviewcustomer", ctx -> orderOverviewCustommer(ctx, connectionPool));
        // app.post("CreateOrder", ctx -> CreateOrder(ctx, connectionPool));
        app.get("generateSvg", ctx -> showCarport(ctx));
        app.post("updateOrder", ctx -> updateOrder(ctx, connectionPool));
        app.post("editOrder", ctx -> editOrder(ctx, connectionPool));
        app.post("deleteorder", ctx -> deleteOrder(ctx, connectionPool));
        app.post("sendRequest", ctx -> sendRequest(ctx, connectionPool));
        app.get("carportLengthList", ctx -> carportLengthList(ctx, connectionPool));
        app.get("designcarport", ctx -> carportLengthList(ctx, connectionPool));


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

    public static void orderOverviewCustommer(Context ctx, ConnectionPool connectionPool)
    {
        User currentUser = ctx.sessionAttribute("currentUser");
        try
        {
            List<Order> orderListCustomer = OrderMapper.getAllOrdersCustomer( currentUser.getUserId(),connectionPool);
            ctx.attribute("orderListCustomer", orderListCustomer);
            ctx.render("customeroverview.html");
        } catch (Exception e)
        {
            ctx.attribute("message", "Failed to get orders");
            e.printStackTrace();
            ctx.render("customeroverview.html");
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

    private static void carportLengthList(Context ctx, ConnectionPool connectionPool)
    {
        try
        {

            List<Integer> carportLengthList = OrderMapper.getMaterialVariantFromMaterialId(connectionPool);

            ctx.attribute("carportlengthlist", carportLengthList);
            ctx.render("designcarport.html");

        } catch (DatabaseException e)
        {
            throw new RuntimeException();
        }
    }

    private static void showPartsList(Context ctx, ConnectionPool connectionPool)
    {
        int orderId = Integer.parseInt(ctx.formParam("orderIdChosen"));
        try
        {

            List<OrderItem> orderItemList = OrderMapper.getOrderItemsByOrderId(orderId, connectionPool);

            if (orderItemList.isEmpty())
            {
                ctx.render("showpartslist.html");
                return;
            }
            OrderItem orderItem = orderItemList.get(0);

            ctx.attribute("width", orderItem.getOrder().getCarportWidth());
            ctx.attribute("length", orderItem.getOrder().getCarportLength());
            ctx.attribute("orderItemList", orderItemList);
            ctx.render("showpartslist.html");

        } catch (DatabaseException e)
        {
            // måske også en numberformat exception??
            throw new RuntimeException();
        }
    }

    private static void sendRequest(Context ctx, ConnectionPool connectionPool)
    {

        int width = Integer.parseInt(ctx.formParam("width"));
        int length = Integer.parseInt(ctx.formParam("length"));
        // width = ctx.sessionAttribute("width");
        //int length = ctx.sessionAttribute("length");
        //Boolean installationFee = ctx.formParam("");
        int status = 1;
        int totalPrice = 19999;
        User user = ctx.sessionAttribute("currentUser");

        Order order = new Order(100, width, length, false, status, totalPrice, user);
        try
        {

           // order = OrderMapper.insertOrder(order, connectionPool);

            //to do
            // calculate order items (parts list)

            Calculator calculator = new Calculator(width, length, connectionPool);
            calculator.calcCarport(order);

            // save parts in db

            // create message to customer and render order / send confirmation
            // er for test skal render en anden side
            System.out.println("test succes");
            ctx.render("index.html");

        } catch (DatabaseException e)
        {
            throw new RuntimeException();

        }


    }

    private static void deleteOrder(Context ctx, ConnectionPool connectionPool)
    {
        try
        {
            int orderId = Integer.parseInt(ctx.formParam("orderid"));
            OrderMapper.delete(orderId, connectionPool);
            List<OrderItem> orderItemList = OrderMapper.getOrderItemsByOrderId(orderId, connectionPool);
            ctx.attribute("orderItemlist", orderItemList);
            orderOverviewAdmin(ctx, connectionPool);
        } catch (DatabaseException | NumberFormatException e)
        {
            ctx.attribute("message", e.getMessage());
            orderOverviewAdmin(ctx, connectionPool);
        }
    }


/*
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
    public static void showCarport(Context ctx)
    {
        Locale.setDefault(Locale.US);
        try
        {
            int length = Integer.parseInt(ctx.queryParam("length"));
            int width = Integer.parseInt(ctx.queryParam("width"));
            if (length > 200 && length < 900 || width > 200 && width < 900)
            {
                ctx.attribute("message1", "Ugyldig længde eller bredde. <br>" +
                        "Indtast venligst gyldige tal. <br>" +
                        "Længde og bredde skal være mellem 200 og 900 cm.");
                ctx.attribute("svg", "");
                ctx.render("designcarport.html");
                return;
            }
            else
            {
                CarportSvg svg = new CarportSvg(width, length);
                ctx.attribute("svg", svg.toString());
            }
        } catch (NumberFormatException e)
        {
            ctx.attribute("message", "Ugyldig længde eller bredde. <br>" +
                    "Indtast venligst gyldige tal. <br>" +
                    "Længde og bredde skal være mellem 200 og 900 cm.");
            ctx.attribute("svg", "");
        }
        // Render the HTML template
        ctx.render("designcarport.html");
    }

    private static void updateOrder(Context ctx, ConnectionPool connectionPool)
    {
        try
        {
            int orderId = Integer.parseInt(ctx.formParam("orderId"));
            int carportWidth = Integer.parseInt(ctx.formParam("carportWidth"));
            int carportLength = Integer.parseInt(ctx.formParam("carportLength"));
            boolean installation = Boolean.parseBoolean(ctx.formParam("installationFee"));
            int status = Integer.parseInt(ctx.formParam("status"));
            int totalPrice = Integer.parseInt(ctx.formParam("totalPrice"));
            OrderMapper.updateOrder(orderId, carportWidth, carportLength, installation, status, totalPrice, connectionPool);
            List<Order> orderList = OrderMapper.getAllOrders(connectionPool);
            ctx.attribute("orderList", orderList);
            ctx.render("adminoverview.html");
        } catch (DatabaseException | NumberFormatException e)
        {
            ctx.attribute("message", e.getMessage());
            System.out.println("Error in updateOrder: " + e.getMessage());
            ctx.render("updateorder.html");
        }
    }

    private static void editOrder(Context ctx, ConnectionPool connectionPool)
    {
        try
        {
            int orderId = Integer.parseInt(ctx.formParam("orderId"));
            Order order = OrderMapper.getOrderById(orderId, connectionPool);
            ctx.attribute("order", order);
            ctx.render("updateorder.html");
        } catch (NumberFormatException e)
        {
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
            System.out.println("Error in editOrder" + e.getMessage());
        } catch (DatabaseException e)
        {
            throw new RuntimeException(e);
        }
    }
}
