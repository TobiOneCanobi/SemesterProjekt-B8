package app.entities;

public class User {

    private int userId;
    private String firstName;
    private String lastName;
    private String address;
    private int zipCode;
    private String city;
    private int phoneNumber;
    private String email;
    private String password;
    private String role;


    public User(int userId, String firstName, String lastName, String address, int zipCode, String city, int phoneNumber, String email, String password, String role)
    {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.role = role;

    }

    public User(int userId, String email, String password, String role)
    {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getUserId()
    {
        return userId;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getAddress()
    {
        return address;
    }

    public int getZipCode()
    {
        return zipCode;
    }

    public String getCity()
    {
        return city;
    }

    public int getPhoneNumber()
    {
        return phoneNumber;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }
    public String getRole()
    {
        return role;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", zipCode=" + zipCode +
                ", city='" + city + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
