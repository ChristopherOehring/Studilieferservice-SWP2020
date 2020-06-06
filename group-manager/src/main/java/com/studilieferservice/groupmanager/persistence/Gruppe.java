package com.studilieferservice.groupmanager.persistence;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

// TODO: 5/31/20 Fix the version, so that it only goes up by one if something actually changes. (not important)
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


    public boolean addMember(User user) {
        if(owner.equals(user) || adminList.contains(user) || memberList.contains(user)) return false;
        version++;
        return memberList.add(user);
    }

    public boolean removeMember(User user) {
        if(memberList.remove(user)){
            version++;
            return true;
        }
        return false;
    }

    public boolean removeAdminOrMember(User user){
        version++;
        return (memberList.remove(user) | adminList.remove(user));
    }

    public List<User> getMembers() {
        return memberList;
    }

    public void updateMember(User oldMember, User newMember) {
        if (this.memberList.contains(oldMember)) {
            memberList.remove(oldMember);
            memberList.add(newMember);
            version++;
        }
    }

    public List<User> getAdmins() {
        return adminList;
    }

    // TODO: 5/31/20 Should this really be possible?:
    public boolean addAdmin(User admin) {
        if(adminList.contains(admin) || memberList.contains(admin) || owner.equals(admin)) {
            return false;
        }
        version++;
        return this.adminList.add(admin);
    }

    public boolean removeAdmin(User admin) {
        if (this.adminList.remove(admin)) {
            version++;
            return true;
        }
        return false;
    }

    public void setOwner(User owner) {
        version++;
        removeAdminOrMember(owner);
        this.owner = owner;
    }

    public boolean promote(User user){
        if(memberList.remove(user)) {
            adminList.add(user);
            version++;
            return true;
        }
        return false;
    }

    public boolean demote(User user){
        if(adminList.remove(user)) {
            memberList.add(user);
            version++;
            return true;
        }
        return false;
    }

// lookup
    public boolean isOwner(User user) {
        return user ==this.owner;
    }

    public String getPermissions(User user){
        if(memberList.contains(user)) return "member";
        if(adminList.contains(user)) return "admin";
        if(owner == user) return "owner";
        return null;
    }

    public User getOwner() {
        return owner;
    }
}
