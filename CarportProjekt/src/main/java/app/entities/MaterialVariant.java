package app.entities;

public class MaterialVariant
{
    private int materialVariantId;
    private int length;
    Material material;
    private int price;

    public MaterialVariant(int materialVariantId, int length, Material material, int price)
    {
        this.materialVariantId = materialVariantId;
        this.length = length;
        this.material = material;
        this.price = price;
    }

    public int getMaterialVariantId()
    {
        return materialVariantId;
    }

    public int getLength()
    {
        return length;
    }

    public Material getMaterial()
    {
        return material;
    }

    public int getPrice()
    {
        return price;
    }

    @Override
    public String toString()
    {
        return "MaterialVariant{" +
                "materialVariantId=" + materialVariantId +
                ", length=" + length +
                ", materialId=" + materialId +
                ", price=" + price +
                '}';
    }
}
