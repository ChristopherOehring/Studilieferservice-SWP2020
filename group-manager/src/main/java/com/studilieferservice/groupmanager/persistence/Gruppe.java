package com.studilieferservice.groupmanager.persistence;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "gruppe")
public class Gruppe {

    @Id
    private String id;

    @NotNull
    private String groupName;

/*    removed because a many to many relation would be necessary
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "gruppe_id", referencedColumnName = "id")
    private List<User> users = new ArrayList<>();   */

    private String users = "";

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

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public void addUser(String user){
        users = users + ":" + user + ":";
    }

    public void removeUser(String user){
        users = users.replace(":" + user + ":", "");
    }

    @Override
    public String toString() {
        return "Gruppe{" +
                "id='" + id + '\'' +
                ", groupName='" + groupName + '\'' +
                ", users='" + users + '\'' +
                '}';
    }
}
