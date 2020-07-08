package com.studilieferservice.chatmanager.controller.bodies;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderBody {
    public String groupId;
    public String userId;
    public String date;
    public String address;

    public OrderBody(@JsonProperty("groupId") String groupId,
                     @JsonProperty("userId") String userId,
                     @JsonProperty("date") String date,
                     @JsonProperty("address") String address) {
       this.groupId = groupId;
       this.userId = userId;
       this.date = date;
       this.address = address;
    }
}
