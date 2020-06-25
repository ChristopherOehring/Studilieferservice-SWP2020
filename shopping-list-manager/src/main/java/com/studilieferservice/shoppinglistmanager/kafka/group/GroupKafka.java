package com.studilieferservice.shoppinglistmanager.kafka.group;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

//this class is only for converting kafka payload for groups into an object
//groups via kafka have their users in multiple lists and store only their emails
public class GroupKafka {

    private String id;
    private String groupName;
    //members are represented by their emails (=ids)
    private String owner;
    private List<String> userList;
    private List<String> adminList;

    public GroupKafka(@JsonProperty("id") String id,
                      @JsonProperty("groupName") String groupName,
                      @JsonProperty("owner") String owner,
                      @JsonProperty("userList") List<String> userList,
                      @JsonProperty("adminList") List<String> adminList) {
        this.id = id;
        this.groupName = groupName;
        this.owner = owner;
        this.userList = userList;
        this.adminList = adminList;
    }

    //converts the multiple lists for the users into one list
    public List<String> getAllMembers() {
        List<String> membersString = userList;
        membersString.add(owner);
        membersString.addAll(adminList);

        return membersString;
    }

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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    public List<String> getAdminList() {
        return adminList;
    }

    public void setAdminList(List<String> adminList) {
        this.adminList = adminList;
    }
}
