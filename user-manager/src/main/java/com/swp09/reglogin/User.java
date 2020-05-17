package com.swp09.reglogin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "User")
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
    private String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
    public User()
    {


    }


}