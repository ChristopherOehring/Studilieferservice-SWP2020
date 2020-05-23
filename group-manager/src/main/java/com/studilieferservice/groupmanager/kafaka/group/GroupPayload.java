package com.studilieferservice.groupmanager.kafaka.group;

import com.studilieferservice.groupmanager.persistence.User;

public class GroupPayload {
    private String id;

    private String name;

    private User creator;

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
