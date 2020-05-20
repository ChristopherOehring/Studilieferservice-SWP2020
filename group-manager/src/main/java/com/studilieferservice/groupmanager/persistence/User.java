package com.studilieferservice.groupmanager.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Id;

// Useless for now as Users are saved in a string -> no! not anymore!!!

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    /**
     * just another constructor for creating users
     * @param id user-id, which has to be a valid email address
     * @param firstName user's first name
     * @param lastName user's last name
     */
    public User(String id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * It looks like this constructor isn't used, but it is necessary, because a default constructor is needed (InstantiationException: No default constructor for entity)
     */
    public User() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
