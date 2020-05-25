package com.studilieferservice.usermanager.kafka.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.studilieferservice.usermanager.kafka.KafkaMessage;

public class UserKafkaMessage extends KafkaMessage<UserPayload> {
    protected UserKafkaMessage(@JsonProperty("id") String id,
                               @JsonProperty("key")String key,
                               @JsonProperty("time")String time,
                               @JsonProperty("type")String type,
                               @JsonProperty("version")Long version,
                               @JsonProperty("payload")UserPayload payload) {
        super(id, key, time, type, version, payload);
    }
}
