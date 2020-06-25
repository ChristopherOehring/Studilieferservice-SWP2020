package com.studilieferservice.groupmanager.controller.bodys;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a template that is used in the {@link com.studilieferservice.groupmanager.controller.GroupController}
 * for processing JSON-payload regarding delivery data and saving them to the given group
 * the delivery date must match the form DD.MM.YYYY
 * the city name must only contain letters (upper/lower case), dashes and spaces
 * the zip code must only contain digits
 * the street name must only contain letters (upper/lower case), dashes and spaces
 * the house number must only contain digits followed by maximum one letter (upper/lower case, e.g. for multiple entrances per house)
    <pre>
        Example:
                {
                "groupId":"81fce800-3cf9-4583-8ed6-56326c1d3163",
                "deliveryDate":"28.11.2020",
                "city":"Ilmenau",
                "zip":"98693",
                "street":"My nice street",
                "houseNumber":"69a"
                }
    </pre>
 * @author Manuel Jirsak
 * @version 1.2 6/18/20
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
