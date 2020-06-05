package com.studilieferservice.groupmanager.controller.bodys;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetUserBody {
    private String value;

    public GetUserBody(@JsonProperty("email") String value){
            this.value = value;
        }

    public String getValue() {
        return value;
    }
}
