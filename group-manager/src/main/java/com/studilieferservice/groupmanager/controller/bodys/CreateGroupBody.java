package com.studilieferservice.groupmanager.controller.bodys;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is a template that is used in the {@link com.studilieferservice.groupmanager.controller.GroupController}
 * for processing JSON-payload for creating groups
    <pre>
        Example:
                {
                "groupName":"my new group",
                "email":"max.mustermann@tu-ilmenau.de"
                }
    </pre>
 * @author Christopher Oehring
 * @version 1.1 6/18/20
 */
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
