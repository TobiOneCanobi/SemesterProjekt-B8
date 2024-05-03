package app.persistence;

import app.entities.User;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper
{

    public static User loginRetriever(String email, String password, ConnectionPool connectionPool) throws DatabaseException
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

}
