package com.studilieferservice.groupmanager.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO: 5/31/20 Fix the version, so that it only goes up by one if something actually changes. (not important)

// TODO: 6/09/20 Rethink the way GroupEvents are created, as groupService.save() is not necessary to make changes persistent

// TODO: rename this to Group to stick with naming conventions through out the projects

/**
 * The Structure that represents groups
 */

@Entity(name = "Gruppe")
@Table(name = "gruppe")
public class Gruppe {

    /**
     * The identifying UUID for this group, as a String
     */
    @Id
    @Column(name = "group_id")
    private String id;

    /**
     * The display name of this group
     */
    @NotNull
    private String groupName;

    @ManyToOne
    private User owner;

    @ManyToMany
    private List<User> memberList = new ArrayList<>();

    @ManyToMany
    private List<User> adminList = new ArrayList<>();

    /**
     * All outgoing invites of this group
     */
    @JsonIgnore
    @OneToMany(
            mappedBy = "group",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    List<Invite> invites = new ArrayList<>();
    
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

    public List<Invite> getInvites() {
        return invites;
    }

    public void setInvites(List<Invite> invites) {
        this.invites = invites;
    }

    public boolean addInvite(Invite invite){
        return invites.add(invite);
    }

    public boolean removeInvite(Invite invite) {
        return invites.remove(invite);
    }

    @Override
    public String toString() {
        return "Gruppe{" +
                "id='" + id + '\'' +
                ", groupName='" + groupName + '\'' +
                ", owner=" + owner +
                ", memberList=" + memberList +
                ", adminList=" + adminList +
                ", invites=" + invites +
                ", version=" + version +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gruppe gruppe = (Gruppe) o;
        return version == gruppe.version &&
                id.equals(gruppe.id) &&
                groupName.equals(gruppe.groupName) &&
                owner.equals(gruppe.owner) &&
                Objects.equals(memberList, gruppe.memberList) &&
                Objects.equals(adminList, gruppe.adminList) &&
                Objects.equals(invites, gruppe.invites);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupName, owner, memberList, adminList, invites, version);
    }
}
