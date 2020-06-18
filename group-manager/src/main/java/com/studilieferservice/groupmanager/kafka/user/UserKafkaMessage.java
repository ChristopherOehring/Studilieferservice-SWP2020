package com.studilieferservice.groupmanager.kafka.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.studilieferservice.groupmanager.kafka.KafkaMessage;
import com.studilieferservice.groupmanager.persistence.User;

/**
 * This Class is used to read the KafkaMessages produced by the user-manager (which arrive in JSON format)
 * @author Christopher Oehring
 * @version 1.1 6/18/20
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