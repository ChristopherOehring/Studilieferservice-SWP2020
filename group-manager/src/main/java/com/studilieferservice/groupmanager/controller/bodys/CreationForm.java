package com.studilieferservice.groupmanager.controller.bodys;

public class CreationForm {
    public String groupName;
    public String user;

    public String getGroupName() {

        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CreationForm{" +
                "groupName='" + groupName + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
