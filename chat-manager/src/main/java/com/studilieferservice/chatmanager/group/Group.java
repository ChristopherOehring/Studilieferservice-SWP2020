package com.studilieferservice.chatmanager.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.studilieferservice.chatmanager.message.ChatMessage;
import com.studilieferservice.chatmanager.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "shopping_group")
//table name cannot be "group" as this word is an SQL statement and thus reserved
@JsonIgnoreProperties(ignoreUnknown = true)
public class Group {

    @Id
    private String id;

    @NotNull
    private String name;

    @ManyToMany
    @JoinTable(name = "group_user",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    //@JsonManagedReference("group") //to avoid recursion in JSON
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "group")
    @JsonManagedReference("group") //to avoid recursion in JSON
    private List<ChatMessage> messages = new ArrayList<>();

    public Group() {}

    public Group(String id, String name) {
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

    public Set<User> getUsers() {
        return users;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        message.setGroup(this);
    }

    public void removeMessage(ChatMessage message) {
        message.setGroup(null);
        messages.remove(message);
    }

    public void addUser(User user) {
        users.add(user);
        user.getGroups().add(this);
    }

    public void removeUser(User user) {
        user.getGroups().remove(this);
        users.remove(user);
    }

    public String messagesToString() {
        StringBuilder messagesString = new StringBuilder();

        for (ChatMessage m : messages) {
            messagesString.append("Message [id=").append(m.getId()).append(", user=").append(m.getUser()).
                    append(", content=\"").append(m.getContent()).append("\"], ");
        }
        if (messagesString.length() > 0) {
            messagesString.setLength(messagesString.length() - 2);
        }

        return String.format(
                "Group [id='%s', name='%s', messagesSize='%d', messages=[%s]]",
                id, name, messages.size(), messagesString);
    }

    @Override
    public String toString() {
        StringBuilder usersString = new StringBuilder();

        for (User u : users) {
            usersString.append("User [id=").append(u.getId()).append(", name=").append(u.getName()).append("], ");
        }
        if (usersString.length() > 0) {
            usersString.setLength(usersString.length() - 2);
        }

        return String.format(
                "Group [id='%s', name='%s', messagesSize='%d', usersSize='%d', users=[%s]]",
                id, name, messages.size(), users.size(), usersString);
    }
}