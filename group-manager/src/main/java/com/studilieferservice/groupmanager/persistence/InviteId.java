package com.studilieferservice.groupmanager.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class InviteId implements Serializable {

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "email")
    private String email;

    public InviteId(){}

    public InviteId(String groupId, String email) {
        this.groupId = groupId;
        this.email = email;
    }

    public String getGroupId() {
        return groupId;
    }

    private void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getEmail() {
        return email;
    }

    private void setEmail(String userId) {
        this.email = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InviteId inviteId = (InviteId) o;
        return groupId.equals(inviteId.groupId) &&
                email.equals(inviteId.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, email);
    }
}
