package com.studilieferservice.groupmanager.controller.bodys;

/**
 * This is a template that is used in the {@link com.studilieferservice.groupmanager.controller.WebController}
 * to get necessary information from the html form for creating a group
 * @author Christopher Oehring
 * @version 1.1 6/18/20
 */
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
