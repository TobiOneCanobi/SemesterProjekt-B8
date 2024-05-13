package app.persistence;

import app.entities.Material;
import app.entities.MaterialVariant;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialMapper
{
    public static List<MaterialVariant> getVariantsByProductIdAndMinLength(int minLength, int materialId, ConnectionPool connectionPool) throws DatabaseException
    {
        List<MaterialVariant> materialVariantList = new ArrayList<>();
        String sql = "SELECT * FROM material_variant " +
                "INNER join material m USING(material_id) " +
                "WHERE material_id = ? AND length >= ?";
        try(Connection connection = connectionPool.getConnection())
        {
            PreparedStatement ps = connectionPool.getConnection().prepareStatement(sql);
            ps.setInt(1, materialId);
            ps.setInt(2, minLength);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next())
            {
                int variantId = resultSet.getInt("material_variant_id");
                int material_id = resultSet.getInt("material_id");
                int length = resultSet.getInt("length");
                String name = resultSet.getString("name");
                String unit = resultSet.getString("unit");
                int price = resultSet.getInt("price");
                Material material = new Material(materialId,name,unit);
                MaterialVariant materialVariant = new MaterialVariant(variantId, length, material, price);
                materialVariantList.add(materialVariant);
            }
        }
        catch (SQLException e)
        {
            throw new DatabaseException("Could not get users from the database", e.getMessage());
        }
        return materialVariantList;
    }
}
