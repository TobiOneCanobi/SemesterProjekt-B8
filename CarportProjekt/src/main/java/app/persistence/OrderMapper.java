package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper
{

    public static List<Order> getAllOrders(ConnectionPool connectionPool) throws DatabaseException
    {
        List<Order> orderList = new ArrayList<>();
        String sql = "SELECT * FROM orders inner join users using(user_id)";
        try (
                Connection connection = connectionPool.getConnection();
                var prepareStatement = connection.prepareStatement(sql);
                var resultSet = prepareStatement.executeQuery()
        )
        {
            while (resultSet.next())
            {
                int userId = resultSet.getInt("user_id");
                String email = resultSet.getString("email");
                String password = resultSet.getString("passwords");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String address = resultSet.getString("address");
                int phoneNumber = resultSet.getInt("phone_number");
                int zipCode = resultSet.getInt("zip_code");
                String role = resultSet.getString("role");

                int orderId = resultSet.getInt("order_id");
                int carportWidth = resultSet.getInt("carport_width");
                int carportLength = resultSet.getInt("carport_length");
                boolean installationFee = resultSet.getBoolean("installation_fee");
                int status = resultSet.getInt("status");
                int totalPrice = resultSet.getInt("total_price");

                User user = new User(userId, firstName, lastName, address, zipCode, phoneNumber, email, password, role);
                Order order = new Order(orderId, carportWidth, carportLength, installationFee, status, totalPrice, user);
                orderList.add(order);

            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Could not get users from the database", e.getMessage());
        }
        return orderList;
    }

    public static List<OrderItem> getOrderItemsByOrderId(int orderId, ConnectionPool connectionPool) throws DatabaseException
    {
        List<OrderItem> orderItemList = new ArrayList<>();
        String sql = "Select * FROM parts_list_view where order_id = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        )
        {
            preparedStatement.setInt(1, orderId);
            var rs = preparedStatement.executeQuery();
            while (rs.next())
            {
                int carportWidth = rs.getInt("carport_width");
                int carportLength = rs.getInt("carport_length");
                boolean installationFee = rs.getBoolean("installation_fee");
                int status = rs.getInt("status");
                int totalPrice = rs.getInt("total_price");
                Order order = new Order(orderId, carportWidth, carportLength, installationFee, status, totalPrice,null );

                int materialId = rs.getInt("material_id");
                String name = rs.getString("name");
                String unit = rs.getString("unit");
                Material material = new Material(materialId, name, unit);

                int materialVariantId = rs.getInt("material_variant_id");
                int length = rs.getInt("length");
                int price = rs.getInt("price");
                MaterialVariant materialVariant = new MaterialVariant(materialVariantId, length, material, price);

                int orderItemId = rs.getInt("order_item_id");
                int quantity = rs.getInt("quantity");
                String description = rs.getString("description");
                OrderItem orderItem = new OrderItem(orderItemId, order, materialVariant, quantity, description);
                orderItemList.add(orderItem);
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Could not get users from the database", e.getMessage());
        }

        return orderItemList;
    }

    public static Order insertOrder(Order order, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "INSERT INTO orders (carport_width, carport_length, installation_fee, status, user_id, total_price)" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
            {
                ps.setInt(1, order.getCarportWidth());
                ps.setInt(2, order.getCarportLength());
                ps.setBoolean(3, order.isInstallationFee());
                ps.setInt(4, 1);
                ps.setInt(5, order.getUser().getUserId());
                ps.setInt(6, order.getTotalPrice());
                ps.executeQuery();
                ResultSet keySet = ps.getGeneratedKeys();
                if (keySet.next())
                {
                    Order newOrder = new Order(keySet.getInt(1), order.getCarportWidth(),
                            order.getCarportLength(), order.isInstallationFee(), order.getOrderStatusId(),
                            order.getTotalPrice(), order.getUser());
                    return newOrder;
                } else
                {
                    return null;
                }
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Could not get users from the database", e.getMessage());
        }
    }

    public static void insertOrderItems(List<OrderItem> orderItems, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "INSERT INTO order_item (order_id, material_variant_id, quantity, description)" +
                "VALUES (?, ?, ?, ?)";
        try (Connection connection = connectionPool.getConnection())
        {
            for (OrderItem orderItem : orderItems)
            {
                try (PreparedStatement ps = connection.prepareStatement(sql))
                {
                    ps.setInt(1, orderItem.getOrder().getOrderId());
                    ps.setInt(2, orderItem.getMaterialVariant().getMaterialVariantId());
                    ps.setInt(3, orderItem.getQuantity());
                    ps.setString(4, orderItem.getDescription());
                    ps.executeQuery();
                }
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Could not get users from the database", e.getMessage());
        }
    }

    public static void delete(int orderId, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "DELETE FROM orders WHERE order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setInt(1, orderId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected != 1)
            {
                throw new DatabaseException("Fejl ved sletning af en ordre!");
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Fejl ved sletning af en ordre", e.getMessage());
        }
    }


    /*
    public static List<Order> loadOrdersForAdmin(ConnectionPool connectionPool) throws DatabaseException
    {
        List<Order> loadOrdersForAdminList = new ArrayList<>();
        String sql = "SELECT \n" +
                "    o.order_id,\n" +
                "    u.first_name,\n" +
                "    u.last_name,\n" +
                "    u.email,\n" +
                "    u.phone_number,\n" +
                "    u.address,\n" +
                "    u.zip_code,\n" +
                "    o.total_price,\n" +
                "    o.status\n" +
                "FROM \n" +
                "    public.orders o\n" +
                "JOIN \n" +
                "    public.users u ON o.user_id = u.user_id;\n";
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
                String phoneNumber = rs.getString("phone_number");
                String address = rs.getString("address");
                int zipCode = rs.getInt("zip_code");
                int totalPrice = rs.getInt("total_price");
                String status = rs.getString("status");
                Order order = new Order(orderId, firstName, lastName, email, phoneNumber, address, zipCode, totalPrice, status);
                loadOrdersForAdminList.add(order);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            throw new DatabaseException("Fejl ved indlæsning af ordrer.", e.getMessage());
        }
        return loadOrdersForAdminList;
    }


    public static List<Order> loadOrdersForCustomer(ConnectionPool connectionPool, int userId) throws DatabaseException
    {
        List<Order> loadOrdersForCustomerList = new ArrayList<>();
        String sql = "SELECT\n" +
                "    users.user_id,\n" +
                "    users.email,\n" +
                "    users.first_name,\n" +
                "    users.last_name,\n" +
                "    users.address,\n" +
                "    users.phone_number,\n" +
                "    users.zip_code,\n" +
                "    orders.order_id,\n" +
                "    orders.total_price,\n" +
                "    orders.installation_fee,\n" +
                "    orders.status\n" +
                "FROM\n" +
                "    public.users\n" +
                "JOIN\n" +
                "    public.orders ON users.user_id = orders.user_id\n" +
                "WHERE\n" +
                "    users.user_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                int orderId = rs.getInt("order_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String address = rs.getString("address");
                int zipCode = rs.getInt("zip_code");
                int totalPrice = rs.getInt("total_price");
                String status = rs.getString("status");

                Order order = new Order(orderId, firstName, lastName, email, phoneNumber, address, zipCode, totalPrice, status);
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

    public static void createOrderItem(ConnectionPool connectionPool, int orderId, String description, int materialId) throws SQLException
    {
        String insertOrderLineSQL = "INSERT INTO public.orderline (description, order_id, material_id) VALUES (?, ?, ?, ?);";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertOrderLineSQL))
        {
            pstmt.setInt(1, orderId);
            pstmt.setString(2, description);
            pstmt.setInt(3, materialId);
            pstmt.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
            throw e;
        }
    }
*/
}
