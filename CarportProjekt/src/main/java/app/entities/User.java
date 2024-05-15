package app.entities;

import java.util.Objects;

public class User
{
    private int userId;
    private String firstName;
    private String lastName;
    private String address;
    private int zipCode;
    private int phoneNumber;
    private String email;
    private String password;
    private String role;

    public User(int userId, String firstName, String lastName, String address, int zipCode, int phoneNumber, String email, String password, String role)
    {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.zipCode = zipCode;
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
    public User(String firstName, String lastName, String address, int zipCode, int phoneNumber, String email)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
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
                ", phoneNumber=" + phoneNumber +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && zipCode == user.zipCode && phoneNumber == user.phoneNumber && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(address, user.address) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(userId, firstName, lastName, address, zipCode, phoneNumber, email, password, role);
    }
}
