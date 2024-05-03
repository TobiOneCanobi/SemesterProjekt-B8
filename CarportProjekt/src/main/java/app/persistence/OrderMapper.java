package app.persistence;

import app.entities.Order;
import app.exceptions.DatabaseException;

import java.sql.*;
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

    public static Order createOrder(int userId, ConnectionPool connectionPool) throws DatabaseException
    {
        Order newOrder = null;
        String sql = "INSERT INTO orders (user_id) VALUES (?)";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            ps.setInt(1, userId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 1)
            {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next())
                {
                    int newOrderId = rs.getInt(1);
                    newOrder = new Order(newOrderId, userId);
                }
            } else
            {
                throw new DatabaseException("Error inserting order for user ID: " + userId);
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Database connection error", e.getMessage());
        }
        return newOrder;
    }

    public static void createOrderItem(ConnectionPool connectionPool, String description, int orderId, int materialId) throws SQLException
    {
        String insertOrderLineSQL = "INSERT INTO public.orderline (description, order_id, material_id) VALUES (?, ?, ?, ?);";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertOrderLineSQL))
        {
            pstmt.setString(1, description);
            pstmt.setInt(2, orderId);
            pstmt.setInt(3, materialId);
            pstmt.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
            throw e;
        }
    }




}
