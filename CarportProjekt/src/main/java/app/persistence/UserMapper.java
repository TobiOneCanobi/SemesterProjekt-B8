package app.persistence;

import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper
{

    public static User login(String eMail, int passWord, ConnectionPool connectionPool) throws DatabaseException
    {
        String sql = "select * from users where email=? and passwords=?";

        try (Connection connection = connectionPool.getConnection(); PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1, eMail);
            ps.setString(2, passWord);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                int UserId = rs.getInt("user_id");
                String firstName = rs.getString(first_name);
                String lastName = rs.getString(last_name);
                String Address = rs.getString(address);
                String phoneNumber = rs.getString(phone);
                int zipCode = rs.getInt("zip_code");
                return new User(userId, eMail, passWord);
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
