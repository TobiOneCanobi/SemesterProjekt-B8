package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper
{

    public static User login(String email, String password, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "SELECT * FROM users WHERE email=? AND passwords=?";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                int userId = rs.getInt("user_id");
                String role = rs.getString("role");

                return new User(userId, email, password, role);
            } else
            {
                throw new DatabaseException("Fejl i login. Prøv venligst igen");
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Database fejl", e.getMessage());
        }
    }

    public static boolean emailExists(String email, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "SELECT COUNT(*) AS COUNT FROM users WHERE email = ?";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e)
        {
            throw new DatabaseException("Database error", e.getMessage());
        }
        return false;
    }

    public static void createUser(String firstName, String lastName, String address, int zipCode, int phoneNumber, String email, String password, String role, ConnectionPool connectionPool) throws DatabaseException
    {
        if (emailExists(email, connectionPool))
        {
            throw new DatabaseException("Email bliver allerede brugt.");
        }
        String sql = "INSERT INTO users (first_name, last_name, address, zip_code, phone_number, email, passwords, role) VALUES (?,?,?,?,?,?,?,?)";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql))
        {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, address);
            ps.setInt(4, zipCode);
            ps.setInt(5, phoneNumber);
            ps.setString(6, email);
            ps.setString(7, password);
            ps.setString(8, role);

            int rowsAffected = ps.executeUpdate();



            if (rowsAffected == 0)
            {
                throw new DatabaseException("Fejl ved oprettelse af bruger. Nogle felter er ikke udfyldt");
            }
        } catch (SQLException e)
        {
            String msg = "Der er sket en fejl. Data er ikke indtastet korrekt";
            if (e.getMessage().startsWith("ERROR: duplicate key value "))
            {
                msg = "Værdien er allerede brugt. Skriv noget andet";
            }
            throw new DatabaseException(msg, e.getMessage());
        }

    }

}
