package com.studilieferservice.usermanager.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private boolean isSignedIn ;

    @NotEmpty
    @NotNull
    @Size(min=2)
    private String firstName;

    @NotEmpty
    @NotNull
    @Size(min=2)
    private String lastName;

    @NotEmpty
    @NotNull
    @Size(min=2)
    private String userName;

    @NotEmpty
    @NotNull
    private String street;

    @NotEmpty
    @NotNull
    private String city;

    @NotEmpty
    @NotNull
    private String zip;

    @Id
    @Email
    @NotEmpty
    @Column (unique=true)
    private String email;

    @NotEmpty
    @NotNull
    @Size(min=8)
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

    public User(String firstName, String email, String password) {
        this.firstName = firstName;
        this.email = email;
        this.password = password;
    }

    public User() { }

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
}