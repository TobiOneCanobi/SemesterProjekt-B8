package app.entities;

public class OrderItem
{
    private int orderItemId;
    private String description;
    private int orderId;
    private int materialId;

    public OrderItem(int orderItemId, String description, int orderId, int materialId)
    {
        this.orderItemId = orderItemId;
        this.description = description;
        this.orderId = orderId;
        this.materialId = materialId;
    }

    public int getOrderItemId()
    {
        return orderItemId;
    }

    public String getDescription()
    {
        return description;
    }

    public int getOrderId()
    {
        return orderId;
    }

    public int getMaterialId()
    {
        return materialId;
    }

    @Override
    public String toString()
    {
        return "OrderItem{" + "orderItemId=" + orderItemId + ", description='" + description + '\'' + ", orderId=" + orderId + ", materialId=" + materialId + '}';
    }
}
