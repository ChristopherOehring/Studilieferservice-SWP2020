package com.studilieferservice.usermanager.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;
/**
 *  basic structure for user
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table (name = "user1")

public class User {

    public User(String firstName,String lastName ,String userName,String city,String street, String zip,
                String email, String password , boolean isSignedIn) {
        this.firstName = firstName;
        this.lastName  =lastName;
        this.userName = userName;
        this.city = city;
        this.street =street ;
        this.zip = zip;
        this .isSignedIn = isSignedIn;
        this.email = email;
        this.password = password;
    }

    public User() { }

    private boolean isSignedIn ;

    @NotEmpty
    @NotNull
    @Size(min=2)
    @Column(name = "firstName")
    private String firstName;

    @NotEmpty
    @NotNull
    @Size(min=2)
    @Column(name = "lastName")

    private String lastName;

    @NotEmpty
    @NotNull
    @Size(min=2)
    @Column(name = "userName")

    private String userName;

    @NotEmpty
    @NotNull
    @Column(name = "street")

    private String street;

    @NotEmpty
    @NotNull
    @Column(name = "city")

    private String city;

    @NotEmpty
    @NotNull
    @Digits(integer = 5,fraction = 0)
    @Column(name = "zip")

    private String zip;

    @Id
    @Email
    @NotEmpty
    @Column(name = "email")

    private String email;

    @NotEmpty
    @NotNull
    @Size(min=8)
    @Column(name = "password")

    private String password;

    public boolean isSignedIn() {
        return isSignedIn;
    }

    public void setSignedIn(boolean signedIn) {
        isSignedIn = signedIn;
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

    public void setStreet(String address) {
        this.street = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}