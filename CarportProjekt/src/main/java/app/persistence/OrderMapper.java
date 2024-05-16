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






  public static List<Order> getAllOrdersCustomer(int userId, ConnectionPool connectionPool) throws DatabaseException
{
    List<Order> orderList = new ArrayList<>();
    String sql = "SELECT orders.order_id, orders.carport_width, orders.carport_length, orders.installation_fee, orders.status, orders.total_price " +
            "FROM orders " +
            "INNER JOIN users ON orders.user_id = users.user_id " +
            "WHERE users.user_id = ?;";
    try (
            Connection connection = connectionPool.getConnection();
            var prepareStatement = connection.prepareStatement(sql)
    )
    {
        prepareStatement.setInt(1, userId);
        try (var resultSet = prepareStatement.executeQuery())
        {
            while (resultSet.next())
            {
                int orderId = resultSet.getInt("order_id");
                int carportWidth = resultSet.getInt("carport_width");
                int carportLength = resultSet.getInt("carport_length");
                boolean installationFee = resultSet.getBoolean("installation_fee");
                int status = resultSet.getInt("status");
                int totalPrice = resultSet.getInt("total_price");


                Order order = new Order(orderId, carportWidth, carportLength, installationFee, status, totalPrice);
                orderList.add(order);
            }
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
                Order order = new Order(orderId, carportWidth, carportLength, installationFee, status, totalPrice, null );

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
                " VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            ps.setInt(1, order.getCarportWidth());
            ps.setInt(2, order.getCarportLength());
            ps.setBoolean(3, order.isInstallationFee());
            ps.setInt(4, 1);  // Ensure '1' is the correct status ID
            ps.setInt(5, order.getUser().getUserId());
            ps.setInt(6, order.getTotalPrice());

            int affectedRows = ps.executeUpdate(); // Use executeUpdate for INSERT, UPDATE, DELETE
            if (affectedRows == 0) {
                throw new DatabaseException("Inserting order failed, no rows affected.");
            }

            try (ResultSet keySet = ps.getGeneratedKeys()) {
                if (keySet.next()) {
                    Order newOrder = new Order(keySet.getInt(1), order.getCarportWidth(),
                            order.getCarportLength(), order.isInstallationFee(), 1, // Assuming '1' is the correct initial status
                            order.getTotalPrice(), order.getUser());
                    return newOrder;
                } else {
                    throw new DatabaseException("Failed to retrieve ID for new order.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error inserting order into database", e.getMessage());
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
        String sql = "DELETE FROM order_item WHERE order_id = ?";

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

        String sql2 = "DELETE FROM orders WHERE order_id = ?";

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql2)
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

*/
        public static void updateOrder(int orderId, int carportWidth, int carportLength, boolean installationFee, int orderStatusId, int totalPrice, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "UPDATE orders " +
                "SET carport_width = ?, carport_length = ?, installation_fee = ?,  status = ?, total_price = ? " +
                "WHERE order_id = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setInt(1, carportWidth);
            ps.setInt(2, carportLength);
            ps.setBoolean(3, installationFee);
            ps.setInt(4, orderStatusId);
            ps.setInt(5, totalPrice);
            ps.setInt(6, orderId);

        int rowsAffected = ps.executeUpdate();
        if (rowsAffected != 1)
        {
            throw new DatabaseException("Fejl i opdatering af en order");
        }
    } catch (SQLException e)
    {
        throw new DatabaseException("Fejl i opdatering af en order", e.getMessage());
    }
}

    public static Order getOrderById(int orderId, ConnectionPool connectionPool) throws DatabaseException
    {
        Order order = null;
        String sql = "select * from orders where order_id = ?";
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        )
        {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                int id = rs.getInt("order_id");
                int carportWidth = rs.getInt("carport_width");
                int carportLength = rs.getInt("carport_length");
                boolean installationFee = rs.getBoolean("installation_fee");
                int status = rs.getInt("status");
                int totalPrice = rs.getInt("total_price");
                order = new Order(id, carportWidth, carportLength, installationFee, status, totalPrice);
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Fejl ved hentning af task med id = " + orderId, e.getMessage());
        }
        return order;
    }
}
