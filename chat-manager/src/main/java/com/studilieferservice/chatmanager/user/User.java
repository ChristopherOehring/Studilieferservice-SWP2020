package com.studilieferservice.chatmanager.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.studilieferservice.chatmanager.group.Group;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
//table name cannot be "user" as this word is reserved in Postgres
@Table(name = "shopping_user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    //email address
    private String id;

    @NotNull
    private String name;

    @ManyToMany(mappedBy = "users")
    private Set<Group> groups = new HashSet<>();

    public User() {}

    public User(@JsonProperty("email") String id,
                @JsonProperty("userName") String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        StringBuilder groupsString = new StringBuilder();

        for(Group g : groups) {
            groupsString.append("Group [id=").append(g.getId()).append(", name=").append(g.getName()).append("], ");
        }
        if (groupsString.length() > 0) {
            groupsString.setLength(groupsString.length() - 2);
        }

        return String.format(
                "User [id='%s', name='%s', groupsSize='%d', groups=[%s]]",
                id, name, groups.size(), groupsString);
    }
}