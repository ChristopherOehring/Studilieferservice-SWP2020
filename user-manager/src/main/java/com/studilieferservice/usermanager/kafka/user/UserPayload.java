package com.studilieferservice.usermanager.kafka.user;

/**
 * the basic structure of the kafka message payload
 *
 * @author Seraj Hadros
 * @version 1.1 6/23/20
 */
public class UserPayload {
    private String firstName;
    private String lastName;
    private String userName;
    private String street;
    private String city;
    private String zip;
    private String email;

    public UserPayload(String firstName,
                       String lastName,
                       String userName, String street, String city, String zip, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.street = street;
        this.city = city;
        this.zip = zip;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
