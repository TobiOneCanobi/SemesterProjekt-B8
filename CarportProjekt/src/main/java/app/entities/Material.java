package app.entities;

public class Material
{
    private int materialId;
    private String description;
    private String unit;

    public Material(int materialId, String description, String unit)
    {
        this.materialId = materialId;
        this.description = description;
        this.unit = unit;
    }

    public int getMaterialId()
    {
        return materialId;
    }

    public String getDescription()
    {
        return description;
    }

    public String getUnit()
    {
        return unit;
    }

    @Override
    public String toString()
    {
        return "Material{" +
                "materialId=" + materialId +
                ", description='" + description + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}