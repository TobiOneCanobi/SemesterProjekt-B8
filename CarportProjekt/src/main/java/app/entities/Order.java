package app.entities;

public class Order
{
    //Order fields
    private int orderId;
    private int totalPrice;
    private boolean installationFee;
    private String status;
    private int userId;

    //User fields
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private int zipCode;

    public Order(int orderId, int userId)
    {
        this.orderId = orderId;
        this.userId = userId;
    }

    public Order(int orderId, String firstName, String lastName, String email, String phone, String address, int zipCode, int totalPrice, String status)
    {
        this.orderId = orderId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.zipCode = zipCode;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public int getOrderId()
    {
        return orderId;
    }

    public int getTotalPrice()
    {
        return totalPrice;
    }

    public boolean isInstallationFee()
    {
        return installationFee;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public int getUserId()
    {
        return userId;
    }

    @Override
    public String toString()
    {
        return "Order{" + "orderId=" + orderId + ", totalPrice=" + totalPrice + ", installationFee=" + installationFee + ", status='" + status + '\'' + ", userId=" + userId + '}';
    }
}