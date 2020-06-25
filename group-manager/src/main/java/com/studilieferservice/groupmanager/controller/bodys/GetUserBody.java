package com.studilieferservice.groupmanager.controller.bodys;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is a template that is used in the {@link com.studilieferservice.groupmanager.controller.GroupController}
 * when only the email of a specific user needs to be provided
    <pre>
        Example:
                {
                "email":"max.mustermann@tu-ilmenau.de"
                }
    </pre>
 * @author Christopher Oehring
 * @version 1.1 6/18/20
 */
public class GetUserBody {
    private String value;

    public GetUserBody(@JsonProperty("email") String value){
            this.value = value;
        }

    public String getValue() {
        return value;
    }
}
