package com.studilieferservice.groupmanager.kafka.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.studilieferservice.groupmanager.kafka.KafkaMessage;
import com.studilieferservice.groupmanager.persistence.User;

/**
 * this is used to read the KafkaMessages produced by the user-manager
 */

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