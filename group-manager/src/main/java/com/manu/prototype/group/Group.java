package com.manu.prototype.group;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Group {

    private final UUID groupId;
    private final String name;
    private List<String> users;


    public Group(@JsonProperty("id") UUID groupId,
                 @JsonProperty("name") String name) {
        this.groupId = groupId;
        this.name = name;
        users = new ArrayList<>();
    }

    public UUID getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public List<String> getUsers() {
        return users;
    }

    public List<String> addUser(String user) {
        users.add(user);
        return users;
    }

    public List<String> removeUser(String user) {
        users.remove(user);
        return users;
    }


    @Override
    public String toString() {
        return "Group{" +
                "groupId=" + groupId +
                ", name='" + name + '\'' +
                ", Users=" + users +
                '}';
    }
}
