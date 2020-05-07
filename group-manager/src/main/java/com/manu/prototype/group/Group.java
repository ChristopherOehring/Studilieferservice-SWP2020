package com.manu.prototype.group;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
@Entity

public class Group {

    @Id
    private UUID id;

    @NotNull
    private String name;

    private List<String> users;

    public Group(String name){
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public boolean addUser(String user){
        return users.add(user);
    }

    public boolean removeUser(String user){
        return users.remove(user);
    }
}
