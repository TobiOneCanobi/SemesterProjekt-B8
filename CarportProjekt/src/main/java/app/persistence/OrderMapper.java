package app.persistence;

import app.entities.Order;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper
{
    public static List<Order> loadOrdersForAdmin(ConnectionPool connectionPool) throws DatabaseException
    {
        List<Order> loadOrdersForAdminList = new ArrayList<>();
        String sql = "SELECT \n" +
                "    o.order_id,\n" +
                "    u.first_name,\n" +
                "    u.last_name,\n" +
                "\tu.email,\n" +
                "    u.phone,\n" +
                "\tu.address,\n" +
                "    u.zip_code,\n" +
                "\to.total_price,\n" +
                "\to.status\n" +
                "FROM \n" +
                "    public.orders o\n" +
                "JOIN \n" +
                "    public.users u ON o.order_id = u.user_id;\n";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                int orderId = rs.getInt("order_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                int zipCode = rs.getInt("zip_code");
                int totalPrice = rs.getInt("total_price");
                String status = rs.getString("status");
                Order order = new Order(orderId, firstName, lastName, email, phone, address, zipCode, totalPrice, status);
                loadOrdersForAdminList.add(order);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            throw new DatabaseException("Fejl ved indlæsning af ordrer.", e.getMessage());
        }
        return loadOrdersForAdminList;
    }

    public static List<Order> loadOrdersForCustomer(ConnectionPool connectionPool) throws DatabaseException
    {
        List<Order> loadOrdersForCustomerList = new ArrayList<>();
        String sql = "SELECT \n" +
                "    o.order_id,\n" +
                "    u.first_name,\n" +
                "    u.last_name,\n" +
                "\tu.email,\n" +
                "    u.phone,\n" +
                "\tu.address,\n" +
                "    u.zip_code,\n" +
                "\to.total_price,\n" +
                "\to.status\n" +
                "FROM \n" +
                "    public.orders o\n" +
                "JOIN \n" +
                "    public.users u ON o.order_id = u.user_id;\n";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                int orderId = rs.getInt("order_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                int zipCode = rs.getInt("zip_code");
                int totalPrice = rs.getInt("total_price");
                String status = rs.getString("status");
                Order order = new Order(orderId, firstName, lastName, email, phone, address, zipCode, totalPrice, status);
                loadOrdersForCustomerList.add(order);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            throw new DatabaseException("Fejl ved indlæsning af ordrer.", e.getMessage());
        }
        return loadOrdersForCustomerList;
    }




}
