package com.studilieferservice.groupmanager.controller.bodys;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is a template that is used in the {@link com.studilieferservice.groupmanager.controller.GroupController}
 * to delete a group, therefore only the group id needs to be provided
    <pre>
        Example:
                {
                "groupId":"81fce800-3cf9-4583-8ed6-56326c1d3163"
                }
    </pre>
 * @author Christopher Oehring
 * @version 1.1 6/18/20
 */
public class DeleteGroupBody {
    private String value;

    public DeleteGroupBody(@JsonProperty("groupId") String value){
            this.value = value;
        }

    public String getValue() {
        return value;
    }
}
