package com.studilieferservice.groupmanager.api.bodys;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteGroupBody {
    private String value;

    public DeleteGroupBody(@JsonProperty("groupId") String value){
            this.value = value;
        }

    public String getValue() {
        return value;
    }
}
