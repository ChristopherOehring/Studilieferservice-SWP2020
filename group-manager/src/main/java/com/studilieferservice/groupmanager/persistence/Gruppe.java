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
    private List<User> memberList = new ArrayList<>();

    @ManyToMany
    private List<User> adminList = new ArrayList<>();
    
    private long version;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Gruppe() {
        this.version = 0;
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
        version++;
        this.groupName = groupName;
    }

    public void addUser(User user) {
        version++;
        memberList.add(user);
    }

    public void removeUser(User user) {
        version++;
        memberList.remove(user);
    }

    public boolean removeAdminOrMember(User user){
        version++;
        return memberList.remove(user) || adminList.remove(user);
    }

    public List<User> getUsers() {
        return memberList;
    }

    public void updateUser(User oldUser, User newUser) {
        if (this.memberList.contains(oldUser)) {
            memberList.remove(oldUser);
            memberList.add(newUser);
        }
    }

    public List<User> getAdmins() {
        return adminList;
    }

    public void addAdmin(User admin) {
        version++;
        this.adminList.add(admin);
    }

    public void removeAdmin(User admin) {
        version++;
        this.adminList.remove(admin);
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        version++;
        this.owner = owner;
    }

    public boolean isOwner(User user) {
        return user==this.owner;
    }

    public String getPermissions(User user){
        if(memberList.contains(user)) return "member";
        if(adminList.contains(user)) return "admin";
        if(owner == user) return "owner";
        return null;
    }

    public boolean promote(User user){
        if(memberList.remove(user)) {
            adminList.add(user);
            return true;
        }
        return false;
    }

    public boolean demote(User user){
        if(adminList.remove(user)) {
            memberList.add(user);
            return true;
        }
        return false;
    }
}
