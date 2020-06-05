package com.studilieferservice.groupmanager.controller.bodys;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupAndUserBody {

    private String groupId;
    private String email;

    public GroupAndUserBody(@JsonProperty("groupId") String groupId,
                            @JsonProperty("email") String email){
        this.groupId = groupId;
        this.email = email;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getEmail() {
        return email;
    }
}