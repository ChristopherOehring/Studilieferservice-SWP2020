package com.studilieferservice.groupmanager.kafka.group;

import java.util.List;

/**
 * This class is a representation of the relevant information of a {@link com.studilieferservice.groupmanager.persistence.Gruppe}, for use in a {@link GroupKafkaMessage}
 * @author Christopher Oehring
 * @version 1.1 6/18/20
 */

public class GroupPayload {
    private String id;

    private String groupName;

    private String owner;

    private List<String> userList;

    private List<String> adminList;

    public GroupPayload(String id, String groupName, String owner, List<String> userList,
                        List<String> adminList) {
        this.id = id;
        this.groupName = groupName;
        this.owner = owner;
        this.userList = userList;
        this.adminList = adminList;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
