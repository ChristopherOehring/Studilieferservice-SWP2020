package com.studilieferservice.groupmanager.api.bodys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class CreateGroupBody {
    private String groupName;
    private String ownerEmail;

    public CreateGroupBody(@JsonProperty("groupName") String groupName,
                           @JsonProperty("email") String ownerEmail) {
        this.groupName = groupName;
        this.ownerEmail = ownerEmail;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }
}
