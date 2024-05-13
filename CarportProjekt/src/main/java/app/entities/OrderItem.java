package app.entities;

public class OrderItem
{
    private int orderItemId;
    private Order order;
    private MaterialVariant materialVariant;
    private int quantity;
    private String description;

    public OrderItem(int orderItemId, Order order, MaterialVariant materialVariant, int quantity, String description)
    {
        this.orderItemId = orderItemId;
        this.order = order;
        this.materialVariant = materialVariant;
        this.quantity = quantity;
        this.description = description;
    }

    public int getOrderItemId()
    {
        return orderItemId;
    }

    public Order getOrder()
    {
        return order;
    }

    public MaterialVariant getMaterialVariant()
    {
        return materialVariant;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public String toString()
    {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", order=" + order +
                ", materialVariant=" + materialVariant +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                '}';
    }
}
