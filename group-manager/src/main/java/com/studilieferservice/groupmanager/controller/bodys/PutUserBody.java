package com.studilieferservice.groupmanager.controller.bodys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.studilieferservice.groupmanager.persistence.User;

/**
 * @author Christopher Oehring
 * @author Manuel Jirsak
 * @version 1.3 6/18/20
    <pre>
        Example:
                {
                "id":"81fce800-3cf9-4583-8ed6-56326c1d3163",
                "user":"max.mustermann@tu-ilmenau.de",
                "firstname":"Max",
                "lastname":"Mustermann",
                "username":"Moritz"
                }
    </pre>
 */
//TODO do we still need this?
//TODO should id be renamed to groupId and userId to email??
@JsonIgnoreProperties(ignoreUnknown = true)
public class PutUserBody {
    private String id;
    private User user;

    /**
     * This is a template that is used in the {@link com.studilieferservice.groupmanager.controller.GroupController}
     * to process JSON-Payload from HTTP-Requests, User user consists of the last four parts of the JSON-Payload
     * @param id Group-ID
     * @param userId User-ID in the form of an email-address
     * @param firstname User's first name
     * @param lastname User's last name
     * @param username the screen name of the user
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
