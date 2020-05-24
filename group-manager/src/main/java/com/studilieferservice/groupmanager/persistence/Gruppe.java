package com.studilieferservice.groupmanager.persistence;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "gruppe")
public class Gruppe {
    /**
     * basic structure for groups
     *      String id: group id, is randomly chosen when creating a new group
     *      String groupname: name of the group, has to be set when creating a new group
     *      Users are saved in a list of type User
     */

    @Id
    private String id;

    @NotNull
    private String groupName;

    @ManyToOne
    private User owner;

    @ManyToMany
    private List<User> userList = new ArrayList<>();

    @ManyToMany
    private List<User> adminList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public void removeUser(User user) {
        userList.remove(user);
    }

    public List<User> getUsers() {
        return userList;
    }

    public void updateUser(User oldUser, User newUser) {
        if (this.userList.contains(oldUser)) {
            userList.remove(oldUser);
            userList.add(newUser);
        }
    }

    public List<User> getAdmins() {
        return adminList;
    }

    public void addAdmin(User admin) {
        this.adminList.add(admin);
    }

    public void removeAdmin(User admin) {
        this.adminList.remove(admin);
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public boolean isOwner(User user) {
        return user==this.owner;
    }
}
