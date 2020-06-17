package com.studilieferservice.groupmanager.api.bodys;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Manuel Jirsak
 * @version 1.1
 */
public class GroupDeliveryBody {
    private String groupId;
    private String date;
    private String city;
    private String zipCode;
    private String street;
    private String houseNumber;

    public GroupDeliveryBody(@JsonProperty("groupId") String groupId,
                             @JsonProperty("deliveryDate") String date,
                             @JsonProperty("city") String city,
                             @JsonProperty("zip") String zipCode,
                             @JsonProperty("street") String street,
                             @JsonProperty("houseNumber") String houseNumber){
        this.groupId = groupId;
        this.date = date;
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.houseNumber = houseNumber;

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

    public List<String> getPlace() {
        List<String> place = new ArrayList<>();
        place.add(city);
        place.add(zipCode);
        place.add(street);
        place.add(houseNumber);
        return place;
    }

    public void setPlace(List<String> place) {
        this.city = place.get(0);
        this.zipCode = place.get(1);
        this.street = place.get(2);
        this.houseNumber = place.get(3);
    }
}
