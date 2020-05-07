package com.manu.prototype.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class PutUserBody {
    private UUID id;
    private String user;

    public PutUserBody(@JsonProperty("id")UUID id,
                       @JsonProperty("user") String user) {
        this.id = id;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public String getUser() {
        return user;
    }
}
