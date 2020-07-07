package com.studilieferservice.shoppinglistmanager.controller.bodies;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroupBody {

    public String groupId;

    public GroupBody(@JsonProperty("groupId") String groupId) {
        this.groupId = groupId;
    }
}
