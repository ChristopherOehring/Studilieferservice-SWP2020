package com.studilieferservice.groupmanager.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.studilieferservice.groupmanager.persistence.User;

public class PutUserBody {
    private String id;
    private User user;

    /**
     * Used to process JSON-Payload from HTTP-Requests, User user consists of the last three parts of the JSON-Payload
     * @param id Group-ID
     * @param userId User-ID in the form of an email-address
     * @param firstname User's first name
     * @param lastname User's last name
     */
    public PutUserBody(@JsonProperty("id")String id,
                       @JsonProperty("user") String userId,
                       @JsonProperty("firstname") String firstname,
                       @JsonProperty("lastname") String lastname,
                       @JsonProperty("username") String username) {
        this.id = id;
        this.user = new User(userId, firstname, lastname, username);
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getUserID() {
        return user.getEmail();
    }

    public String getUserFirstName() {
        return user.getFirstName();
    }

    public String getUserLastName() {
        return user.getLastName();
    }

    public String getUserName() {
        return user.getUserName();
    }
}
