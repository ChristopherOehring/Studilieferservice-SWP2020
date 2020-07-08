package com.studilieferservice.groupmanager.controller.bodys;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * This is a template that is used in the {@link com.studilieferservice.groupmanager.controller.GroupController}
 * whenever the input of a groupId and a email is required
    <pre>
        Example:
                {
                "groupId":"81fce800-3cf9-4583-8ed6-56326c1d3163",
                "email":"max.mustermann@tu-ilmenau.de"
                }
    </pre>
 * @author Christopher Oehring
 * @version 1.1 6/18/20
 */
public class GroupAndUserBody {

    private String groupId;
    private String email;

    public GroupAndUserBody(@JsonProperty("groupId") String groupId,
                            @JsonProperty("email") String email){
        this.groupId = groupId;
        this.email = email;
    }

    public GroupAndUserBody(){}

    public String getGroupId() {
        return groupId;
    }

    public String getEmail() {
        return email;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}