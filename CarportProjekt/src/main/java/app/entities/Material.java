package app.entities;

public class Material
{
    private int materialId;
    private String description;
    private String length;
    private String height;
    private String width;
    private int quantity;
    private String unit;
    private String type;
    private int price;

    public Material(int materialId, String description, String length, String height, String width, int quantity, String unit, String type, int price)
    {
        this.materialId = materialId;
        this.description = description;
        this.length = length;
        this.height = height;
        this.width = width;
        this.quantity = quantity;
        this.unit = unit;
        this.type = type;
        this.price = price;
    }


    public int getMaterialId()
    {
        return materialId;
    }

    public String getDescription()
    {
        return description;
    }

    public String getLength()
    {
        return length;
    }

    public String getHeight()
    {
        return height;
    }

    public String getWidth()
    {
        return width;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public String getUnit()
    {
        return unit;
    }

    public String getType()
    {
        return type;
    }

    public int getPrice()
    {
        return price;
    }

    @Override
    public String toString()
    {
        return "Material{" + "materialId=" + materialId + ", description='" + description + '\'' + ", length='" + length + '\'' + ", height='" + height + '\'' + ", width='" + width + '\'' + ", quantity=" + quantity + ", unit='" + unit + '\'' + ", type='" + type + '\'' + ", price=" + price + '}';
    }
}