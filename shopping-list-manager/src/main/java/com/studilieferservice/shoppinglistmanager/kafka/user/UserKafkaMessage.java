package com.studilieferservice.shoppinglistmanager.kafka.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.studilieferservice.shoppinglistmanager.kafka.KafkaMessage;
import com.studilieferservice.shoppinglistmanager.user.User;

public class UserKafkaMessage extends KafkaMessage<User> {

    public UserKafkaMessage(@JsonProperty("id") String id,
                            @JsonProperty("key") String key,
                            @JsonProperty("time") String time,
                            @JsonProperty("type") String type,
                            @JsonProperty("version") Long version,
                            @JsonProperty("payload") User payload) {
        super(id, key, time, type, version, payload);
    }
}

/*
User payload:
    String firstName;
    String lastName;
    String userName;
    String street;
    String city;
    String zip;
    String email;
*/
