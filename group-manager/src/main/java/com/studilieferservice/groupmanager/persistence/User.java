package com.studilieferservice.groupmanager.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;

// Useless for now as Users are saved in a string

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
