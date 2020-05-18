package com.studilieferservice.groupmanager.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PutUserBody {
    private String id;
    private String user;

    public PutUserBody(@JsonProperty("id")String id,
                       @JsonProperty("user") String user) {
        this.id = id;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }
}
