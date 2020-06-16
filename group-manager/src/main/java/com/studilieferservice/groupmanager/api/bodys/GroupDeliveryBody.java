package com.studilieferservice.groupmanager.api.bodys;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Manuel Jirsak
 * @version 1.0
 */
public class GroupDeliveryBody {
    private String groupId;
    private String date;
    private String place;

    public GroupDeliveryBody(@JsonProperty("groupId") String groupId,
                             @JsonProperty("deliveryDate") String date,
                             @JsonProperty("deliveryPlace") String place){
        this.groupId = groupId;
        this.date = date;
        this.place = place;

    }

    public String getGroupId() {
        return groupId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
