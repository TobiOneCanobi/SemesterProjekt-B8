package app.persistence;

import app.entities.*;
import app.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapperTest
{
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    @BeforeAll
    static void setupClass()
    {
        try (Connection connection = connectionPool.getConnection())
        {
            try (Statement stmt = connection.createStatement())
            {
                // The test schema is already created, so we only need to delete/create test tables
                stmt.execute("DROP TABLE IF EXISTS test.users");
                stmt.execute("DROP TABLE IF EXISTS test.orders");
                stmt.execute("DROP TABLE IF EXISTS test.order_item");
                stmt.execute("DROP SEQUENCE IF EXISTS test.users_user_id_seq CASCADE;");
                stmt.execute("DROP SEQUENCE IF EXISTS test.orders_order_id_seq CASCADE;");
                stmt.execute("DROP SEQUENCE IF EXISTS test.order_item_order_item_id_seq CASCADE;");
                // Create tables as copy of original public schema structure
                stmt.execute("CREATE TABLE test.users AS (SELECT * from public.users) WITH NO DATA");
                stmt.execute("CREATE TABLE test.orders AS (SELECT * from public.orders) WITH NO DATA");
                stmt.execute("CREATE TABLE test.order_item AS (SELECT * from public.order_item) WITH NO DATA");
                // Create sequences for auto generating id's for users and orders
                stmt.execute("CREATE SEQUENCE test.users_user_id_seq");
                stmt.execute("ALTER TABLE test.users ALTER COLUMN user_id SET DEFAULT nextval('test.users_user_id_seq')");
                stmt.execute("CREATE SEQUENCE test.orders_order_id_seq");
                stmt.execute("ALTER TABLE test.orders ALTER COLUMN order_id SET DEFAULT nextval('test.orders_order_id_seq')");
                stmt.execute("CREATE SEQUENCE test.order_item_order_item_id_seq");
                stmt.execute("ALTER TABLE test.order_item ALTER COLUMN order_item_id SET DEFAULT nextval('test.order_item_order_item_id_seq')");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            fail("Database connection failed");
        }
    }

    @BeforeEach
    void setUp()
    {
        try (Connection connection = connectionPool.getConnection())
        {
            try (Statement stmt = connection.createStatement())
            {
                // Remove all rows from all tables
                stmt.execute("DELETE FROM test.orders");
                stmt.execute("DELETE FROM test.users");
                stmt.execute("DELETE FROM test.order_item");

                stmt.execute("INSERT INTO test.users (user_id, first_name, last_name, address, zip_code, phone_number, email, passwords, role) " +
                        "VALUES  (1, 'pepande', 'pandestejsen', 'pepandevej 20', 2600, 12341234, 'pepande@pepande.dk', 'Pepande2!', 'customer')");

                stmt.execute("INSERT INTO test.orders (order_id, carport_width, carport_length, installation_fee, status, user_id, total_price) " +
                        "VALUES (1, 1, 1, false, 1, 1, 10)");

                stmt.execute("INSERT INTO test.order_item (order_item_id, order_id, material_variant_id, quantity, description) " +
                        "VALUES (1, 1, 1, '2', 'Hej med dig')");
                // Set sequence to continue from the largest member_id
                stmt.execute("SELECT setval('test.order_item_order_item_id_seq', COALESCE((SELECT MAX(order_item_id) + 1 FROM test.order_item), 1), false)");
                stmt.execute("SELECT setval('test.orders_order_id_seq', COALESCE((SELECT MAX(order_id) + 1 FROM test.orders), 1), false)");
                stmt.execute("SELECT setval('test.users_user_id_seq', COALESCE((SELECT MAX(user_id) + 1 FROM test.users), 1), false)");
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
            fail("Database connection failed");
        }
    }

    @Test
    void getAllOrders()
    {
        try
        {
            int expected = 1;
            List<Order> actualOrders = OrderMapper.getAllOrders(connectionPool);
            assertEquals(expected, actualOrders.size());
        } catch (DatabaseException e)
        {
            fail("Database fejl: " + e.getMessage());
        }
    }

    @Test
    void insertOrder()
    {
        try
        {
            User user = new User(1, "pepande", "pandestejsen", "pepandevej 20", 2600, 12341234, "pepande@pepande.dk", "Pepande2!", "customer");
            Order newOrder = new Order(44, 44, true, 2, 40, user.getUserId());
            newOrder = OrderMapper.insertOrder(newOrder, user, connectionPool);
            Order actualOrder = OrderMapper.getOrderById(newOrder.getOrderId(), connectionPool);
            assertEquals(newOrder, actualOrder);
        } catch (DatabaseException e)
        {
            fail("Database fejl: " + e.getMessage());
        }
    }

    @Test
    void getOrderById()
    {
        try
        {

            Order expected = new Order(1, 1, 1, false, 1, 10);
            Order actualOrder = OrderMapper.getOrderById(1, connectionPool);
            assertEquals(expected, actualOrder);
        } catch (DatabaseException e)
        {
            fail("Database fejl: " + e.getMessage());
        }
    }

    @Test
    void delete()
    {
        try
        {
            List<Order> actualOrders = OrderMapper.getAllOrders(connectionPool);
            boolean actualOrder = actualOrders.stream().anyMatch(order -> order.getOrderId() == 1);
            assertTrue(actualOrder);
            OrderMapper.delete(1, connectionPool);
            List<Order> deletedOrders = OrderMapper.getAllOrders(connectionPool);
            boolean deletedOrder = deletedOrders.stream().noneMatch(order -> order.getOrderId() == 1);
            assertTrue(deletedOrder);
            assertEquals(actualOrders.size() - 1, deletedOrders.size());
        } catch (DatabaseException e)
        {
            fail("Database fejl: " + e.getMessage());
        }
    }

    @Test
    void updateOrder()
    {
        try
        {
            Order expectedOrder = new Order(1, 1, 1, false, 1, 10);
            OrderMapper.updateOrder(expectedOrder.getOrderId(), expectedOrder.setCarportWidth(5), expectedOrder.setCarportLength(5), expectedOrder.setInstallationFee(true), expectedOrder.setOrderStatusId(2), expectedOrder.setTotalPrice(50), connectionPool);
            Order orderFromDatabase = OrderMapper.getOrderById(1, connectionPool);
            assertEquals(expectedOrder, orderFromDatabase);
        } catch (DatabaseException e)
        {
            fail("Database fejl: " + e.getMessage());
        }
    }
}