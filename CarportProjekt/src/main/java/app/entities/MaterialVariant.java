package app.entities;

public class MaterialVariant
{
    private int materialVariantId;
    private int length;
    private int materialId;
    private int price;

    public MaterialVariant(int materialVariantId, int length, int materialId, int price)
    {
        this.materialVariantId = materialVariantId;
        this.length = length;
        this.materialId = materialId;
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

    public int getMaterialId()
    {
        return materialId;
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
