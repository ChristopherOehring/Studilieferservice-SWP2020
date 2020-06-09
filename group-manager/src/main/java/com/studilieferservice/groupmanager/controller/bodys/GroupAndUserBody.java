package com.studilieferservice.groupmanager.controller.bodys;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 *  This is a template that is used in the {@link com.studilieferservice.groupmanager.controller.GroupController}
 *  whenever the input of a groupId and a email is required
    <pre>
        Example:
                {
                "groupId":"81fce800-3cf9-4583-8ed6-56326c1d3163",
                "email":"max.mustermann@tu-ilmenau.de"
                }
    </pre>
 */
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