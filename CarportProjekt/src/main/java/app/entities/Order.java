package app.entities;

public class Order
{
    //Order fields
    private int orderId;
    private int carportWidth;
    private int carportLength;
    private boolean installationFee;
    private int orderStatusId;
    private int totalPrice;
    private User user;

    public Order(int orderId, int carportWidth, int carportLength, boolean installationFee, int orderStatusId, int totalPrice, User user)
    {
        this.orderId = orderId;
        this.carportWidth = carportWidth;
        this.carportLength = carportLength;
        this.installationFee = installationFee;
        this.orderStatusId = orderStatusId;
        this.totalPrice = totalPrice;
        this.user = user;
    }

    public int getOrderId()
    {
        return orderId;
    }

    public int getCarportWidth()
    {
        return carportWidth;
    }

    public int getCarportLength()
    {
        return carportLength;
    }

    public boolean isInstallationFee()
    {
        return installationFee;
    }

    public int getOrderStatusId()
    {
        return orderStatusId;
    }

    public int getTotalPrice()
    {
        return totalPrice;
    }

    public User getUser()
    {
        return user;
    }

    @Override
    public String toString()
    {
        return "Order{" +
                "orderId=" + orderId +
                ", carportWidth=" + carportWidth +
                ", carportLength=" + carportLength +
                ", installationFee=" + installationFee +
                ", orderStatusId=" + orderStatusId +
                ", totalPrice=" + totalPrice +
                ", user=" + user +
                '}';
    }
}