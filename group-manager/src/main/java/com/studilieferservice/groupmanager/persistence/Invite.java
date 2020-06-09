package com.studilieferservice.groupmanager.persistence;

import com.studilieferservice.groupmanager.service.GroupService;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity(name = "Invite")
@Table(name = "invite")
public class Invite {
    @EmbeddedId
    private InviteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("groupId")
    private Gruppe group;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("email")
    private User user;

    @Column(name = "createdOn")
    private Date createdOn = new Date();

    @Column(name = "groupName")
    private String groupName;

    public Invite() {}

    public Invite(Gruppe group, User user) {
        this.group = group;
        this.user = user;
        this.id = new InviteId(group.getId(), user.getEmail());
        this.groupName = group.getGroupName();
    }

    public InviteId getId() {
        return id;
    }

    private void setId(InviteId id) {
        this.id = id;
    }

    public Gruppe getGroup() {
        return group;
    }

    private void setGroup(Gruppe group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    private void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    private void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invite invite = (Invite) o;
        return id.equals(invite.id) &&
                group.equals(invite.group) &&
                user.equals(invite.user) &&
                createdOn.equals(invite.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, group, user, createdOn);
    }

    @Override
    public String toString() {
        return "Invite{" +
                "id=" + id +
                ", group=" + group.getId() +
                ", user=" + user.getEmail() +
                ", createdOn=" + createdOn +
                '}';
    }
}
